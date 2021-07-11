import React, {Component} from "react";
import {Card, CardText, CardBody, CardTitle, CardImg, Button} from 'reactstrap';
import {Rating} from '@material-ui/lab';
import 'styles/appointments/appointments.css'

class AppointmentCardStudent extends Component{
    constructor(props) {
        super(props);
    }
    render() {
        return(
            <div style={{display: "flex", marginTop:"2%", height:"95%", boxSizing:"border-box", padding:"1px"}}>
                <Card style={{height:"100%"}}>
                    <div style={{top:"50%", position:"absolute", transform: "translateY(-50%)", margin:"auto"}}>
                    <CardImg style={{height:"20%", width: "50%", margin: "0 auto"}} src="User_Avatar.png" alt="Card image cap" />
                    <CardBody>
                        <CardTitle tag="h5">{this.props.reservation.instruktor}</CardTitle>
                        <Rating allowHalf  name="half-rating-read" defaultValue={this.props.reservation.rating} size="large" precision={0.1} readOnly />
                    </CardBody>
                    </div>
                </Card>
                <Card style={{height: "100%"}}>
                    <CardBody>
                        <CardTitle tag="h6">{this.props.reservation.predmet}</CardTitle>
                        <CardText>Date: {this.props.reservation.datum}</CardText>
                        <CardText>Time: {this.props.reservation.vrijeme}</CardText>
                        <CardText>Location: {this.props.reservation.lokacija}</CardText>
                        <CardText>Price: {this.props.reservation.cijena+'KM'}</CardText>
                        <Button onClick={(e)=> {if (window.confirm('Are you sure you want to book this appointment?')) this.props.createReservation(this.props.reservation.id)}}>Book</Button>
                    </CardBody>
                </Card>
            </div>
        )
    }
}

export default AppointmentCardStudent;