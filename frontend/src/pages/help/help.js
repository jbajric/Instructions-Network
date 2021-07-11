import React from 'react';
import 'react-confirm-alert/src/react-confirm-alert.css';
import 'App.css';
import 'styles/main-page/main-page.css';
import 'styles/help/help.css';
import { getUser } from 'api/auth';
import {Card} from "react-bootstrap";
import {Col, Container, Row} from "reactstrap";

let username;

if (getUser() != null && getUser().name != null && getUser().surname != null) {
    username = getUser().name + " " + getUser().surname;
}

function MainPage(props) {

    return (
        <div id="aboutus">
            <div id="slikaaboutus" style={{ backgroundImage: "url(cyanorange.png)", backgroundPosition: 'center', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', height: "250px"}}>
                <h1 style={{fontSize:"40px",  position: "center", paddingTop:"20px", color: "white"}}>About us</h1>
                <p style={{width: "850px", color:"white", margin: "auto",backgroundColor:"whitesmoke", padding:"10px", fontSize:"20px", color:"royalblue"}}>
                        We are a group of young and motivated people who came up with the idea to help others exchange knowledge, skills and ideas.
                        All this is possible in a very simple way. Our mission is to help our customers schedule appointments from the comfort of their home.
                </p>
            </div>


            <div id="slikeClanova" style={{textAlign: "center", paddingBottom: "20px"}}>
                <p style={{paddingTop:"20px"}}>
                   OUR TEAM
                </p>
                <Container id="containerSlika" style={{margin: "auto"}} fluid>
                    <Row>

                        <Col sm="4">
                            <Card style={{ width: '25rem', }}>
                                <Card.Img variant="top" src="avatarElma.png" />
                                <Card.Body>
                                    <Card.Text>
                                        Elma Bejtović
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col sm="4">
                            <Card style={{ width: '25rem' }}>
                                <Card.Img variant="top" src="avatarJasmin.png"/>
                                <Card.Body>
                                    <Card.Text>
                                        Jasmin Bajrić
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col sm="4">
                            <Card style={{ width: '25rem' }}>
                                <Card.Img variant="top" src="avatarNejira.png" />
                                <Card.Body>
                                    <Card.Text>
                                        Nejira Musić
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>

                    </Row>


                </Container>

            </div>
            <div>

            </div>



        </div>


    );
};

export default MainPage;