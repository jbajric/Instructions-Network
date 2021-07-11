import React, {Component} from "react";
import {Card, CardText, CardBody, CardTitle, Button} from 'reactstrap';
import 'styles/appointments/appointments.css';

class AppointmentCardInstructor extends Component{
    constructor(props) {
        super(props);
    }
    render() {
        return(
            <div id="componentContent">
                <Card>
                    <CardBody>
                        <CardTitle tag="h6">Subject: {this.props.reservation.predmet}</CardTitle>
                        <CardText>Date: {this.props.reservation.datum}</CardText>
                        <CardText>Time: {this.props.reservation.vrijeme}</CardText>
                        <CardText>Location: {this.props.reservation.lokacija}</CardText>
                        <CardText>Price: {this.props.reservation.cijena+'KM'}</CardText>
                        <Button onClick={(e)=> {if (window.confirm('Are you sure you want to delete this appointment?')) this.props.removeReservation(this.props.reservation.id)}}>Delete appointment</Button>
                    </CardBody>
                </Card>
            </div>
        )
    }
}

export default AppointmentCardInstructor;