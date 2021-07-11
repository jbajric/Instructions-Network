import React, { Component } from 'react';
import {Container, Row, Col} from "reactstrap";
import {getToken, getUser} from 'api/auth';
import {SearchOutlined, DollarOutlined } from '@ant-design/icons';

import 'react-confirm-alert/src/react-confirm-alert.css';
import 'App.css';
import 'styles/appointments/appointments.css';
import axios from "axios";
import Spinner from 'react-bootstrap/Spinner'
import AppointmentCardStudent from "../../components/appointmentCardStudent";
import { Input, Slider } from "antd";

const hostURL = "http://localhost:8662";

class AppointmentsStudent extends Component{
    constructor() {
        super();
        this.state = {
            isLoaded: false,
            error:null,
            reservations:[],
            id: getUser().id,
            rating: 0,
            search: "",
            minPrice: 0,
            maxPrice: 200
        }
    }

    onChange = values => {
        this.state.search = values.target.value;
    }

    onSliderChange = values => {
        this.state.maxPrice = values[1];
        this.state.minPrice = values[0];
    }

    componentDidMount() {
        axios.get(hostURL + "/termini/allAppointments").then(
            res=>{
                for (let i=0; i<res.data.length;i++){
                    axios.get(hostURL + "/korisnik/subject/"+res.data[i].subject_id).then(
                        res3=>{
                            axios.get(hostURL + "/korisnik/instructor/"+res.data[i].instructor_id).then(
                                res2=>{
                                    axios.get(hostURL + "/korisnik/averageRating/"+ res.data[i].instructor_id).then(
                                        res1=>{
                                            this.state.reservations.push({
                                                id: res.data[i].id,
                                                datum: res.data[i].date,
                                                vrijeme: res.data[i].start_time + '-' + res.data[i].end_time,
                                                predmet: res3.data.subject_name,
                                                cijena: res.data[i].price,
                                                lokacija: res.data[i].location,
                                                instruktor: res2.data.first_name + ' ' + res2.data.last_name,
                                                rating: parseFloat(res1.data)
                                            });
                                        }
                                    ).catch();

                                }
                            ).catch();

                        }
                    ).catch(error=>{this.setState({ isLoaded: true, error});});
                }
            }).catch(error=>{this.setState({ isLoaded: true, error});});
    }

    createReservation(id){

        axios.get(hostURL + "/termini/appointmentById/"+id).then(response=>{
            let appointment = response.data;
            console.log(response.data);
            if(appointment.available){
                axios.put("http://localhost:8083/appointment/"+id, {
                        date: appointment.date,
                        start_time: appointment.start_time,
                        end_time: appointment.end_time,
                        location: appointment.location,
                        price:	appointment.price,
                        available: false,
                        instructor_id: appointment.instructor_id,
                        subject_id: appointment.subject_id

                    }, {
                        headers: {
                            'Authorization': 'Bearer '+ getToken()
                        }}
                )
                    .then(
                        res1=>{
                            axios.post("http://localhost:8084/reservation", { appointment_id: id, student_id: getUser().id}
                                                                           , {headers: { 'Authorization': 'Bearer '+ getToken()
                                                                                        }})
                                 .then(res=>{window.location = '/reservations';})
                                 .catch(error=>{console.log(error);});
                        })
                    .catch();
            }
            else {
                alert("Appointment is not available!");
                window.location = '/appointmentsStudent';
            }
        }).catch();
    }


    render() {
        let reservationCards;

        this.state.search.length > 0 
        ? reservationCards = this.state.reservations.filter(res => res.predmet.toLowerCase().includes(this.state.search.toLowerCase()) ||
                                                                   res.instruktor.toLowerCase().includes(this.state.search.toLowerCase()) ||
                                                                   res.lokacija.toLowerCase().includes(this.state.search.toLowerCase()) &&
                                                                   (res.cijena >= this.state.minPrice && res.cijena <= this.state.maxPrice))
                                                     .map(reservation =>{ return( <Col sm="3">
                                                                                     <AppointmentCardStudent key={reservation.id}
                                                                                                             createReservation={this.createReservation.bind(this)}
                                                                                                             reservation={reservation}/>
                                                                                    </Col>)
                                                                        }
                                                        )
        
        : reservationCards = this.state.reservations.filter(res => res.cijena >= this.state.minPrice && res.cijena <= this.state.maxPrice)
                                                    .map(reservation =>{return( <Col sm="3">
                                                                                    <AppointmentCardStudent key={reservation.id}
                                                                                                            createReservation={this.createReservation.bind(this)}
                                                                                                            reservation={reservation}/>
                                                                                 </Col>)
                                                                        }
                                                        )
        if (reservationCards.length == this.state.reservations.length && reservationCards.length > 0)
            this.state.isLoaded = true;
        return (
            <div>
                <div>
                    {this.state.isLoaded ? ( reservationCards.length > 0 
                                                ? <div id="content">
                                                        <div style={{display:"flex"}}>
                                                            <div id="searchDiv" style={{paddingTop:"10px"}}>
                                                                <div id="subjectDiv">
                                                                    <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<SearchOutlined />}/>
                                                                </div>
                                                            </div>
                                                            <div style={{width:"30%", paddingLeft:"30px", marginBottom:"3%"}}>
                                                                <h5> <DollarOutlined/> Range of prices</h5>
                                                                <Slider tooltipVisible={true} tooltipPlacement="bottom" range draggableTrack={true} defaultValue={[1, 100]} onChange={this.onSliderChange} />
                                                            </div>
                                                        </div>
                                                        <Container fluid> <Row> {reservationCards} </Row> </Container>
                                                   </div>
                                                    : <div id="content">
                                                        <div style={{display:"flex"}}>
                                                        <div id="searchDiv" style={{paddingTop:"10px"}}>
                                                            <div id="subjectDiv">
                                                                <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<SearchOutlined />}/>
                                                            </div>
                                                        </div>
                                                        </div>
                                                        <h2 style={{top:"50%", display: "inline-block", margin: "0 auto"}}>No appointments have been defined.</h2>
                                                      </div>)
                        : <Spinner animation="border" />
                    }
                </div>
            </div>
        )
    }
}

export default AppointmentsStudent; 