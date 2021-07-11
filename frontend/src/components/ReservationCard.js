import React, {Component} from "react";
import {Card, CardText, CardBody, CardTitle, CardSubtitle, Button} from 'reactstrap';
import { Rate } from 'antd';

class ReservationCard extends Component{
    constructor() {
        super();
        this.state = { value: 3 }
    }
    
    handleChange = value => {
        this.setState({ value });
    };

    render() {
        return(
            <div id="content">
                <Card>
                    <CardBody id="karticaRezervacije">
                        <CardTitle tag="h6">Subject: {this.props.reservation.predmet}</CardTitle>
                        <br/>
                        <CardSubtitle tag="h6" className="mb-2 text-muted">Instructor: {this.props.reservation.instruktor}</CardSubtitle>
                        <br/>
                        <CardText>Date: {this.props.reservation.datum}</CardText>
                        <CardText>Time: {this.props.reservation.vrijeme}</CardText>
                        <CardText>Location: {this.props.reservation.lokacija}</CardText>
                        <CardText>Price: {this.props.reservation.cijena}</CardText>
                        <Button onClick={(e)=> {if (window.confirm('Are you sure you want to cancel this reservation?')) this.props.removeReservation(this.props.reservation.id)}}>Cancel reservation</Button>
                    </CardBody>
                </Card>
                <div className="divRating">
                    <Rate onChange={this.handleChange} value={this.state.value} style={{marginBottom:"10px"}}/>
                    <Button onClick={()=> { this.props.giveRating(this.props.reservation, this.state.value)}} style={{marginLeft:"20px", marginBottom:"10px"}}>Submit</Button>
                </div>
            </div>
        )
    }
}

export default ReservationCard;