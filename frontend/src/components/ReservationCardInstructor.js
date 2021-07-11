import React, {Component} from "react";
import {Card, CardText, CardBody, CardTitle, CardSubtitle } from 'reactstrap';

class ReservationCardInstructor extends Component{
    constructor(props) {
        super(props);
    }
    render() {
        return(
            <div>
                <Card>
                    <CardBody>
                        <CardTitle tag="h6">Subject: {this.props.reservation.predmet}</CardTitle>
                        <CardSubtitle tag="h6" className="mb-2 text-muted">Student: {this.props.reservation.student}</CardSubtitle>
                        <CardText>Date: {this.props.reservation.datum}</CardText>
                        <CardText>Time: {this.props.reservation.vrijeme}</CardText>
                        <CardText>Location: {this.props.reservation.lokacija}</CardText>
                        <CardText>Price: {this.props.reservation.cijena}</CardText>
                    </CardBody>
                </Card>
            </div>
        )
    }
}

export default ReservationCardInstructor;