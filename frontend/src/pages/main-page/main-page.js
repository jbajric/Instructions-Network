import React from 'react';
import 'react-confirm-alert/src/react-confirm-alert.css';
import 'App.css';
import 'styles/main-page/main-page.css';
import { getUser } from 'api/auth';
import {Card} from "react-bootstrap";
import {Col, Container, Row} from "reactstrap";
import CustomizedTimeline from "../../components/timeline";

let username = "";

if (getUser() != null && getUser().name != null && getUser().surname != null) {
    username = getUser().name + " " + getUser().surname;
}

function MainPage(props) {

    return (
        <div>
            <div style={{
                backgroundImage: "url(home1.jpg)",
                backgroundPosition: 'center',
                backgroundSize: 'cover',
                backgroundRepeat: 'no-repeat',
                height: "500px"
            }}>
                <h1 style={{fontSize:"25px", paddingTop:"200px"}}>Welcome to Instructions Network, {username}</h1>
                <p>The best instruction platform</p>
            </div>
                <div style={{backgroundColor:"white"}}>
                    <p style={{paddingTop:"30px"}}>
                        WHO WE ARE
                    </p>
                    <p style={{marginRight: "0px", fontSize:"15px", paddingBottom:"30px"}}>
                        Improving knowledge and skills and continuous work on one's own improvement is the foundation of every person's success.
                        This fact was our guiding idea in creating this unique web platform, which will certainly enable our users to improve their knowledge,
                        skills and their own competencies in a simple and efficient way.
                        Instruction Network is an ideal partner for all those who want to learn, but also for professionals who want to share their knowledge.
                    </p>
                    <CustomizedTimeline></CustomizedTimeline>
                    <br/>
                </div>
                <div style={{textAlign: "center", paddingBottom: "20px"}}>
                <p style={{paddingTop:"30px"}}>
                    INSTRUCTIONS FROM ALL AREAS
                </p>
                <Container style={{margin: "auto"}} fluid>
                    <Row>

                        <Col sm="4">
                            <Card style={{ width: '25rem' }}>
                                <Card.Img variant="top" src="school2.jpg" />
                                <Card.Body>
                                    <Card.Text>
                                        School subjects for schools and colleges
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col sm="4">
                            <Card style={{ width: '25rem' }}>
                                <Card.Img variant="top" src="bussiness.jpg" />
                                <Card.Body>
                                    <Card.Text>
                                        Business education and skills development
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col sm="4">
                            <Card style={{ width: '25rem' }}>
                                <Card.Img variant="top" src="design.jpg" />
                                <Card.Body>
                                    <Card.Text>
                                        Training in world languages, IT disciplines, design, art, etc.
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>

                    </Row>


                </Container>

            </div>
        </div>


    );
};
 
export default MainPage;