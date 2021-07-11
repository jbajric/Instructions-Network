import React, { useEffect, useState }  from 'react';
import Alert from 'components/Alert';
import Requests from 'api/requests';
import { Form, Input, Button, Radio } from 'antd';
import { Link } from "react-router-dom";
import auth from "api/auth";
import { UserOutlined, LockOutlined, MailOutlined, LeftSquareOutlined, EditOutlined } from '@ant-design/icons';

import 'react-confirm-alert/src/react-confirm-alert.css';
import 'styles/registration/registration.css';

let hostURL = "http://localhost:8662";

class Registration extends React.Component{

    onFinish = values => {
        auth.registration(values);
    };

    render() {
        return (
            <div id="okvir">
                <Alert message={Register.message} showAlert={Register.show} variant={Register.variant} onShowChange={Register.setShow} />
                <div id="registrationforma">
                    <h2>Instructions Network Registration</h2>
                    <article className="mw6 center bg-white shadow-5 br3 pa3 pa4-ns mv3 ba b--black-10">
                        <Form id="forma"
                              name="normal_login"
                              className="login-form"
                              initialValues={{
                                  remember: true,
                              }}
                            onFinish={this.onFinish}
                        >
                            <Form.Item>
                                <Link to={''}> <LeftSquareOutlined /> </Link>
                            </Form.Item>
                            <Form.Item
                                name="firstname"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please input your first name!',
                                    },     
                                    {
                                        min:4,
                                        message: 'Your first name must be longer than 4!',

                                    },                      
                                    { 
                                        pattern: "^[A-Za-z '.-]*$",
                                        message: 'First name should contain only letters and {\', . , -}, with the first letter capitalized!',
                                    },
                                ]}
                            >
                                <Input prefix={<EditOutlined className="site-form-item-icon" />} placeholder="First name" />
                            </Form.Item>
                            <Form.Item
                                name="lastname"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please input your last name!',
                                    },                             
                                    { 
                                        pattern: "^[A-Za-z '.-]*$",
                                        message: 'Last name should contain only letters and {\', ., -}, with the first letter capitalized!',
                                    },
                                ]}
                            >
                                <Input prefix={<EditOutlined className="site-form-item-icon" />} placeholder="Last name" />
                            </Form.Item>
                            <Form.Item
                                name="email"
                                rules={[
                                    {
                                        type: 'email',
                                        message: 'The input is not valid e-mail!',
                                    },
                                    {
                                        required: true,
                                        message: 'Please input your e-mail!',
                                    },
                                ]}
                            >
                                <Input prefix={<MailOutlined className="site-form-item-icon" />} placeholder="E-mail" />
                            </Form.Item>
                            <Form.Item
                                name="username"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please input your username!',
                                    },                            
                                    { 
                                        pattern: "^[a-zA-Z0-9_.-]+$",
                                        message: 'Username should contain only letters, numbers or: {\'-\', \'.\', \'_\'}!',
                                    },
                                ]}
                            >
                                <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Username" />
                            </Form.Item>
                            <Form.Item
                                name="password"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please input your password!',
                                    },  
                                    {
                                        min:8,
                                        message: 'Your password must be longer than 8!',

                                    },                      
                                    { 
                                        pattern: "^([A-Za-z]*[0-9]*[@#$%^&+=]*)$",
                                        message: 'Password should contain only letters, numbers or: {\'@\', \'#\', \'$\', \'%\', \'^\', \'&\', \'+\', \'=\'}!',
                                    },
                                ]}
                            >
                                <Input.Password
                                    prefix={<LockOutlined className="site-form-item-icon" />}
                                    type="password"
                                    placeholder="Password"
                                />
                            </Form.Item>
                            <Form.Item name="radio">
                                <Radio.Group name="radiogroup" defaultValue={1}>
                                    <Radio value={1}>Instructor</Radio>
                                    <Radio value={2}>Student</Radio>
                                </Radio.Group>
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" htmlType="submit">
                                    Register
                                </Button>
                            </Form.Item>
                        </Form>
                    </article>
                    <h5 style={{color:"white"}}>NWT 2021</h5>
                </div>
            </div>
        );
    }
};

export default Registration;


function Register() {

    const [subjects, setSubjects] = useState([]);
    const [loading, setLoading] = useState(false);
    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    useEffect(() => {
        getSubjectNames()
    }, []);

    const getSubjectNames = () => {
        setLoading(true);
        Requests.sendGetRequest(hostURL + "/korisnik/allSubjectNames",
            {},
            (message, variant, data) => {
                setLoading(false);
                handleAlerts(setShow, setMessage, setVariant, setSubjects, message, variant, data);
            }
        );
    }

    const handleAlerts = (setShow, setMessage, setVariant, setData, message, variant, data) => {
        if (message != null) {
            setShow(true);
            setMessage(message);
            setVariant(variant);
        } else {
            setShow(false);
            setData(data);
        }
    }
}


