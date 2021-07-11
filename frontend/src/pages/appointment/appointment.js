import React from 'react';
import axios from 'axios';
import { Form, Input, Button, DatePicker, TimePicker, Select, InputNumber } from 'antd';
import { Link } from "react-router-dom";
import { LeftSquareOutlined, CalendarOutlined, EnvironmentOutlined, DollarOutlined} from '@ant-design/icons';

import 'styles/appointments/appointments.css';
import 'react-confirm-alert/src/react-confirm-alert.css';
import {getToken, getUser} from "../../api/auth";

let hostURL = "http://localhost:8662"
const Moment = require('moment');
const MomentRange = require('moment-range');
const moment = MomentRange.extendMoment(Moment);
const Option = Select;
let user = getUser();

function disabledDate(current) {    return current <= moment().endOf('day'); }

class Appointment extends React.Component{
    state = { selectedSubject:"", subjects: [{name: "", id: 0}], appointments: [] };

    componentDidMount() {
        axios.get(hostURL + "/korisnik/allSubjectNames")
            .then(data => { for (let i=0; i < data.data.length; i++) 
                                this.state.subjects[i] = { name: data.data[i], id: i+1 };})
            .catch(error => { console.log(error);});
        axios.get(hostURL + "/termini/appointmentsByInstructor/"+user.id).then(
            res=>{
                for (let i=0; i<res.data.length;i++){
                    axios.get(hostURL + "/korisnik/subject/"+res.data[i].subject_id).then(
                        res3=>{
                            this.state.appointments.push({
                                id: res.data[i].id,
                                datum: res.data[i].date,
                                start_time: res.data[i].start_time,
                                end_time: res.data[i].end_time,
                                predmet: res3.data.subject_name
                            });
                        }
                    ).catch(error=>{this.setState({ isLoaded: true, error});});
                }
            }).catch(error=>{this.setState({ isLoaded: true, error});});
    }

    checkForExistingAppointment = (date, startTime, endTime) => {
        for (let i = 0; i < this.state.appointments.length; i++) {
            let newRangeOfPeriod = moment.range(moment(startTime, 'HH:mm'), moment(endTime, 'HH:mm'));
            let rangeOfPeriod = moment.range(moment(this.state.appointments[i].start_time, 'HH:mm'), moment(this.state.appointments[i].end_time, 'HH:mm'));
            console.log(newRangeOfPeriod, rangeOfPeriod);
            if (this.state.appointments[i].datum === date && rangeOfPeriod.overlaps(newRangeOfPeriod)) 
                return true;
        }
        return false;
    }

    onFinish = values => {
        let day = values.date._d.getDate();
        day = day.toString();
        if(day.length===1)day='0'+day;
        let month = values.date._d.getMonth()+1;
        month=month.toString();
        if(month.length===1)month='0'+month;
        let year = values.date._d.getYear();
        year=year.toString();
        year=year.substr(1, year.length-1);
        year='20'+year;
        let date = day+'.'+month+'.'+year;

        let startTimeHours = values.time[0]._d.getHours();
        let startTimeMin = values.time[0]._d.getMinutes();
        startTimeHours = startTimeHours.toString();
        startTimeMin = startTimeMin.toString();
        if(startTimeMin.length===1)startTimeMin='0'+startTimeMin;
        if(startTimeHours.length===1)startTimeHours='0'+startTimeHours;
        let startTime = startTimeHours+':'+startTimeMin;

        let endTimeHours = values.time[1]._d.getHours();
        let endTimeMin = values.time[1]._d.getMinutes();
        endTimeHours = endTimeHours.toString();
        endTimeMin = endTimeMin.toString();
        if(endTimeMin.length===1)endTimeMin='0'+endTimeMin;
        if(endTimeHours.length===1)endTimeHours='0'+endTimeHours;
        let endTime = endTimeHours+':'+endTimeMin;

        if (this.checkForExistingAppointment(date, startTime, endTime)) {
            window.alert('You are creating an overlapping appointment!');
            window.location.reload();
        }

        axios.get(hostURL + '/korisnik/allSubjects').then(
            res=>{
                let subjectId=0;
                for(let i=0; i<res.data.length; i++)
                    if(res.data[i].subject_name===values.subject)
                        subjectId=res.data[i].id;
                axios.post('http://localhost:8083/appointment', {
                    date: date,
                    start_time: startTime,
                    end_time: endTime,
                    location: values.location,
                    price:	parseInt(values.price),
                    available: true,
                    instructor_id: getUser().id,
                    subject_id: subjectId
                }, {
                    headers: {
                        'Authorization': 'Bearer ' + getToken()
                    }}).then(res=>{ window.location = "/appointmentsInstructor"; }).catch(error=>{});
            }
        ).catch()
    
    };

    optionChanged = e => {
        this.state.selectedSubject=e;
    }

    onChangePrice = e =>{
        this.state.price = e;
    }

    render() {
        return (
            <div>
                <div>
                    <article className="mw6 center bg-white shadow-5 br3 pa3 pa4-ns mv3 ba b--black-10">
                        <h3>Create new appointment</h3>
                        <Form id="forma"
                              name="normal_appointment"
                              className="appointment-form"
                              initialValues={{
                                  remember: true,
                              }}
                              onFinish={this.onFinish}
                        >
                            <Form.Item>
                                <Link to={'/appointmentsInstructor'}> <LeftSquareOutlined /> </Link>
                            </Form.Item>
                            <Form.Item
                                name="subject"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please select a subject!',
                                    },
                                ]}
                            >
                                <Select
                                    showSearch
                                    style={{ width: 250 }}
                                    placeholder="Select a subject"
                                    onChange={this.optionChanged}
                                    >
                                    {this.state.subjects.map(temp =>  <Option key={temp.id} value={temp.name}> {temp.name} </Option>)}
                                </Select>
                            </Form.Item>

                            <Form.Item
                                name="date"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please define the date!',
                                    },
                                ]} 
                            >
                                <DatePicker style={{ width: 250 }} prefix={<CalendarOutlined className="site-form-item-icon" />} format="DD.MM.yyyy" disabledDate={disabledDate} />
                            </Form.Item>

                            <Form.Item
                                name="time"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please define the time period!',
                                    },
                                ]} 
                            >
                                <TimePicker.RangePicker style={{ width: 250 }} format="HH:mm" />
                            </Form.Item>
                            <Form.Item
                                name="location"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please input the location!',
                                    },          
                                    { 
                                        pattern: "^([A-Za-z]*[0-9]*[@#^&]*)$",
                                        message: 'Location name should contain only letters, numbers or: {\'@\', \'#\', \'^\', \'&\'}!',
                                    },
                                ]}
                            >
                                <Input style={{ width: 250 }} prefix={<EnvironmentOutlined className="site-form-item-icon" />} placeholder="Location" />
                            </Form.Item>

                            <Form.Item
                                name="price"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please input the price!',
                                    }
                                ]}
                            >
                                <InputNumber style={{ width: 250 }} prefix={<DollarOutlined className="site-form-item-icon" />} min={1} max={100} onChange={this.onChangePrice} placeholder="Price" />
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" htmlType="submit">
                                    Create
                                </Button>
                            </Form.Item>

                        </Form>
                    </article>
                </div>
            </div>
        );
    }
};

export default Appointment;
