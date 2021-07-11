import axios from 'axios';
import React from 'react';
import { message } from 'antd';

// return the user data from the session storage
export const getUser = () => {

    const userStr = localStorage.getItem('user');
    if (userStr) return JSON.parse(userStr);
    else return null;
}

// return the token from the session storage
export const getToken = () => {
    return localStorage.getItem('token') || null;
}


// remove the token and user from the session storage
export const removeUserSession = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
}

// set the token and user from the session storage
export const setUserSession = (token, user) => {
    
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
}

class Auth extends React.Component {

    constructor() {
        super();
        this.state = {
            showWarning: false,
            error: ''
        };
        if (getToken()) {
            this.authenticated = true;
        } else {
            this.authenticated = false;
        }
    }



    login(values){
        axios.post('http://localhost:8081/login', {
            username: values.username,
            password: values.password,
        }).then(response=>{
            let user = {
                username: values.username,
                password: values.password,
            }
            axios.get('http://localhost:8081/instructors/authusername/'+values.username)
                .then(res=>{

                    let user = {
                        id: res.data.id,
                        username: res.data.username,
                        email: res.data.email,
                        name: res.data.first_name,
                        surname: res.data.last_name,
                        role: res.data.roles,
                        password: res.data.password,
                    }
                    setUserSession(response.data.accessToken, user);
                    this.authenticated = true;
                    window.location.href = '/app'
                }).catch(error1=>{
                axios.get('http://localhost:8081/students/authusername/'+values.username)
                    .then(resp=>{
                        let user = {
                            id: resp.data.id,
                            username: resp.data.username,
                            email: resp.data.email,
                            name: resp.data.first_name,
                            surname: resp.data.last_name,
                            role: resp.data.roles,
                            password: resp.data.password,
                        }
                        setUserSession(response.data.accessToken, user);
                        this.authenticated = true;
                        window.location.href = '/app'
                    }).catch(error=>{
                    message.error(error.response.data.message);

                });
            });
        }).catch(error => {
            if (error.response == null) {
                message.error("Please check your internet connection!");
                return;
            }
            if (error.response.status === 401)
                message.error("Wrong Username or Password!");
            else
                message.error(error.response.data.message);
        });
    }


    registration(values){
        let radio;
        radio = values.radio;
        console.log(values.radio);
        if(radio===undefined || radio===null)radio=1;
        console.log(values.radio);
        console.log(typeof values.radio)
        if(radio===1){
            axios.post('http://localhost:8081/registerInstructor', {
                first_name: values.firstname,
                last_name: values.lastname,
                email: values.email,
                username: values.username,
                password: values.password,
                roles: [{
                    "id": 1,
                    "name": "INSTRUCTOR"
                }]
            }).then(response=>{
                this.login(values);
            }).catch(error1=>{
                if (error1.response == null) {
                    message.error("Please check your internet connection!");
                    return;
                }
                else
                    message.error(error1.response.data.message);
            });
        }
        else{
            axios.post('http://localhost:8081/registerStudent', {
                first_name: values.firstname,
                last_name: values.lastname,
                email: values.email,
                username: values.username,
                password: values.password,
                roles: [{
                    "id": 2,
                    "name": "STUDENT"
                }]

            }).then(response=>{
                this.login(values);
            }).catch(error1=>{
                if (error1.response == null) {
                    message.error("Please check your internet connection!");
                    return;
                }
                else
                    message.error(error1.response.data.message);
            });

        }
    }

    logout(cb) {
        removeUserSession();
        this.authenticated = false;
        cb();
    }

    isAuthenticated() {
        return this.authenticated;
    }
}

export default new Auth();
