import React, {Component, useEffect, useState} from 'react';
import {Container, Row, Col} from "reactstrap";
import {getToken, getUser} from 'api/auth';
import {FileSearchOutlined, PlusOutlined} from '@ant-design/icons';

import 'react-confirm-alert/src/react-confirm-alert.css';
import 'App.css';
import 'styles/appointments/appointments.css';
import axios from "axios";
import AppointmentCardInstructor from "../../components/appointmentCardInstructor";
import Spinner from 'react-bootstrap/Spinner'
import {Input} from "antd";
import moment from "moment"

const hostURL = "http://localhost:8662";

class AppointmentsInstructor extends Component{
    constructor() {
        super();
        this.state = {
            isLoaded: false,
            error:null,
            appointments:[],
            sortedAppointments: [],
            id: getUser().id,
            search: ""
        }
    }

    onChange = values => {
        this.state.search = values.target.value;
    }

    componentDidMount() {
        axios.get(hostURL + "/termini/appointmentsByInstructor/"+this.state.id).then(
            res=>{
                for (let i=0; i<res.data.length;i++){
                    axios.get(hostURL + "/korisnik/subject/"+res.data[i].subject_id).then(
                        res3=>{
                            this.state.appointments.push({
                                id: res.data[i].id,
                                datum: res.data[i].date,
                                vrijeme: res.data[i].start_time + '-' + res.data[i].end_time,
                                predmet: res3.data.subject_name,
                                cijena: res.data[i].price,
                                lokacija: res.data[i].location
                            });
                        }
                    ).catch(error=>{this.setState({ isLoaded: true, error});});
                }
            }).catch(error=>{this.setState({ isLoaded: true, error});});
        }
  
    removeReservation(id){
        axios.delete("http://localhost:8083/appointment/"+id, {
            headers: {
                'Authorization': 'Bearer '+ getToken()
            }}).then(
            res3=>{
                this.setState({appointments: this.state.appointments.filter(reservation=>reservation.id!==id)});
            }
        ).catch(error=>{this.setState({ isLoaded: true, error});});
    }

    createAppointment(){ window.location = '/createAppointment'; }

    render() {

        let reservationCards = this.state.appointments.filter(res => res.predmet.toLowerCase().includes(this.state.search.toLowerCase()) ||
                                                                     res.lokacija.toLowerCase().includes(this.state.search.toLowerCase())).map(reservation =>{
            return(
                <Col sm="3">
                    <AppointmentCardInstructor key={reservation.id} removeReservation={this.removeReservation.bind(this)} reservation={reservation}/>
                </Col>
            )
        })
        if (reservationCards.length == this.state.appointments.length && reservationCards.length > 0)
            this.state.isLoaded = true;
        return (
            <div>
                <div className="divIcon">
                    <PlusOutlined onClick={this.createAppointment} className="plusIcon"/>
                </div>
                <div>
                    {this.state.isLoaded 
                        ? (reservationCards.length > 0
                                                ? <div>
                                                    <div id="searchDiv">
                                                        <div id="subjectDiv">
                                                            <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<FileSearchOutlined />}/>
                                                        </div>
                                                    </div>
                                                    <Container fluid> <Row> {reservationCards} </Row> </Container> 
                                                </div>
                                                : <div>
                                                    <div id="searchDiv">
                                                        <div id="subjectDiv">
                                                            <Input onChange={this.onChange} id="searchBarSubject" size="large" placeholder="Search" prefix={<FileSearchOutlined />}/>
                                                        </div>
                                                    </div>
                                                    <h2 style={{top:"50%", display: "inline-block", margin: "0 auto"}}> No appointments have been defined.</h2>
                                                </div>)
                        : <Spinner animation="border" />
                    }      
                </div>
            </div>
        )
    }
}

export default AppointmentsInstructor;