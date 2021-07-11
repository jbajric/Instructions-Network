import React, { Component } from 'react';
import {Container, Row, Col} from "reactstrap";
import ReservationCard from "../../components/ReservationCard";
import {getToken, getUser} from 'api/auth';
import 'react-confirm-alert/src/react-confirm-alert.css';
import 'App.css';
import 'styles/reservations/reservations.css';
import axios from "axios";
import Spinner from 'react-bootstrap/Spinner'
import { FileSearchOutlined } from '@ant-design/icons';
import { Input } from 'antd';

const hostURL = "http://localhost:8662";
let user = getUser();

class Reservations extends Component{
    constructor() {
        super();
        this.state = {
            isLoaded: false,
            error:null,
            reservations:[],
            id: getUser().id,
            search: ""
        }
    }

    onChange = values => { this.state.search = values.target.value;}

    componentDidMount() {
        axios.get(hostURL + "/rezervacije/reservationsByStudent/"+this.state.id).then(
            res=>{
                for (let i=0; i<res.data.length;i++){
                    axios.get(hostURL + "/termini/appointmentById/"+res.data[i].appointment_id).then(
                        res1=>{
                            axios.get(hostURL + "/korisnik/instructor/"+res1.data.instructor_id).then(
                                res2=>{
                                    axios.get(hostURL + "/korisnik/subject/"+res1.data.subject_id).then(
                                        res3=>{
                                            this.state.reservations.push({
                                                id: res.data[i].id,
                                                datum: res1.data.date,
                                                vrijeme: res1.data.start_time + '-' + res1.data.end_time,
                                                predmet: res3.data.subject_name,
                                                instruktor: res2.data.first_name+ ' ' + res2.data.last_name,
                                                cijena: res1.data.price+'KM',
                                                lokacija: res1.data.location
                                            });
                                        }).catch(error=>{this.setState({isLoaded: true, error});})
                                }).catch(error=>{this.setState({isLoaded: true, error});});
                        }).catch(error=>{this.setState({isLoaded: true, error});});
                }
            }).catch(error=>{this.setState({isLoaded: true, error});});
    }

    removeReservation(id){

        axios.get(hostURL + "/rezervacije/reservationById/"+id).then(res=>{
            axios.delete("http://localhost:8084/reservation/"+id, {headers: {  'Authorization': 'Bearer '+ getToken()}})
                .then(

                    res1=>{
                        axios.get(hostURL + "/termini/appointmentById/"+res.data.appointment_id).then(response=>{
                            let appointment = response.data;

                            axios.put("http://localhost:8083/appointment/"+res.data.appointment_id, {
                                    date: appointment.date,
                                    start_time: appointment.start_time,
                                    end_time: appointment.end_time,
                                    location: appointment.location,
                                    price:	appointment.price,
                                    available: true,
                                    instructor_id: appointment.instructor_id,
                                    subject_id: appointment.subject_id

                                }, {
                                    headers: {
                                        'Authorization': 'Bearer '+ getToken()
                                    }}
                            )
                                .then(
                                    res1=>{
                                        this.setState({ reservations: this.state.reservations.filter(reservation=>reservation.id!==id)})
                                    })

                                .catch();

                        }).catch();

                        }
                )
                .catch( error=>{this.setState({isLoaded: true, error}); });
        }).catch();



    }


    giveRating(reservation, ratingStars){
        let instructor, student;
        axios.get(hostURL + "/korisnik/instructorByName/"+reservation.instruktor)
            .then(response1 => {instructor=response1.data;
                                axios.get(hostURL + "/korisnik/student/"+user.id)
                                        .then(response => { student = { first_name: response.data.first_name,
                                                                        last_name: response.data.last_name,
                                                                        email: response.data.email,
                                                                        username: user.username,
                                                                        id: user.id,
                                                                        password: user.password,
                                                                        roles: [user.role[0]]
                                                                    };
                                                            axios.post("http://localhost:8081/rating/", {rating: ratingStars, instructors: instructor, students: student}
                                                                                                      , {headers: { 'Authorization': 'Bearer '+ getToken()}}
                                                                      ).then(this.removeReservation(reservation.id));
                                                         }
                                            );
        });
    }

    render() {
        let reservationCards = this.state.reservations.filter(res => res.predmet.toLowerCase().includes(this.state.search.toLowerCase()) || 
                                                                     res.instruktor.toLowerCase().includes(this.state.search.toLowerCase()) ||
                                                                     res.lokacija.toLowerCase().includes(this.state.search.toLowerCase())).map(reservation =>{
            return(
                <Col sm="3">
                    <ReservationCard key={reservation.id} removeReservation={this.removeReservation.bind(this)} giveRating={this.giveRating.bind(this)} reservation={reservation}/>
                </Col>
            )
        })
        if (reservationCards.length === this.state.reservations.length && reservationCards.length > 0) 
            this.state.isLoaded = true;
        return (
            <div>
                {this.state.isLoaded ? ( reservationCards.length > 0
                        ? <div id="content">
                            <div id="searchDiv"> 
                                <div id="subjectDiv"> 
                                    <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<FileSearchOutlined />}/>
                                </div>
                            </div>
                            <Container fluid> <Row> {reservationCards} </Row> </Container>
                        </div>
                       : <div id="content">
                           <div id="searchDiv"> 
                               <div id="subjectDiv"> 
                                   <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<FileSearchOutlined />}/>
                               </div>
                           </div>
                           <h2 style={{top:"50%", display: "inline-block", margin: "0 auto"}}>No reservations have been defined.</h2>
                        </div>)
                : <Spinner animation="border" />}
            </div>
        )
    }
}

export default Reservations;