import React, { Component } from 'react';
import { Container, Row, Col } from "reactstrap";
import ReservationCardInstructor from "../../components/ReservationCardInstructor";
import { getUser } from 'api/auth';

import 'react-confirm-alert/src/react-confirm-alert.css';
import 'App.css';
import 'styles/reservations/reservations.css';
import axios from "axios";
import Spinner from 'react-bootstrap/Spinner'
import {Input} from "antd";
import {FileSearchOutlined} from "@ant-design/icons";

const hostURL = "http://localhost:8662";

class ReservationsInstructor extends Component{
    constructor() {
        super();
        this.state = {
            isLoaded: false,
            error:null,
            reservations:[],
            id: getUser().id,
            search: "",
            hasReservations: false
        }
    }

    onChange = values => { this.state.search = values.target.value;}

    componentDidMount() {
        axios.get(hostURL + "/rezervacije/allReservations").then(
            res=>{
                for (let i=0; i<res.data.length;i++){
                    axios.get(hostURL + "/termini/appointmentById/"+res.data[i].appointment_id).then(
                        res1=> {
                            if(res1.data.instructor_id===this.state.id){
                                this.setState({hasReservations: true});
                                axios.get(hostURL + "/korisnik/student/" + res.data[i].student_id).then(
                                    res2 => {
                                        axios.get(hostURL + "/korisnik/subject/" + res1.data.subject_id).then(
                                            res3 => {
                                                this.state.reservations.push({
                                                    id: res.data[i].id,
                                                    datum: res1.data.date,
                                                    vrijeme: res1.data.start_time + '-' + res1.data.end_time,
                                                    predmet: res3.data.subject_name,
                                                    student: res2.data.first_name + ' ' + res2.data.last_name,
                                                    cijena: res1.data.price + 'KM',
                                                    lokacija: res1.data.location
                                                });
                                            }
                                        ).catch(error => {this.setState({isLoaded: true, error});});
                                    }).catch(error => {this.setState({isLoaded: true, error});});
                            }}).catch(error => {this.setState({isLoaded: true, error});});
                }
            }).catch(error=>{this.setState({isLoaded: true, error});});
    }

    render() {
        let reservationCards = this.state.reservations.filter(res => res.predmet.toLowerCase().includes(this.state.search.toLowerCase()) || 
                                                                     res.student.toLowerCase().includes(this.state.search.toLowerCase()) ||
                                                                     res.lokacija.toLowerCase().includes(this.state.search.toLowerCase())).map(reservation =>{
            return(
                <Col sm="3">
                    <ReservationCardInstructor reservation={reservation}/>
                </Col>
            )
        })
        if (reservationCards.length === this.state.reservations.length && (this.state.reservations.length > 0 || this.state.hasReservations === true))
            this.state.isLoaded = true;
        return(
            <div>
                {this.state.isLoaded && reservationCards.length > 0 
                                        ? <div id="content">
                                            <div id="searchDiv">
                                                <div id="subjectDiv">
                                                    <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<FileSearchOutlined />}/>
                                                </div>
                                            </div>
                                            <Container fluid> <Row> {reservationCards} </Row> </Container>
                                        </div>
                                        : (this.state.hasReservations
                                            ?  <div id="content">
                                                    <div id="searchDiv">
                                                        <div id="subjectDiv">
                                                            <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<FileSearchOutlined />}/>
                                                        </div>
                                                    </div>
                                                <Spinner animation="border" />
                                                </div>
                                            :  <div id="content">
                                                    <div id="searchDiv">
                                                        <div id="subjectDiv">
                                                            <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<FileSearchOutlined />}/>
                                                        </div>
                                                    </div>
                                                    <h2 style={{top:"50%", display: "inline-block", margin: "0 auto"}}>No reservations have been defined.</h2>
                                                </div>)
                }
            </div>
        )
    }
}

export default ReservationsInstructor;