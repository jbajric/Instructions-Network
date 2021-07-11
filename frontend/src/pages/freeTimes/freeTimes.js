import React from 'react';
import 'react-confirm-alert/src/react-confirm-alert.css';
import 'styles/freeTimes/freeTimes.css';
import axios from 'axios';
import { getUser, getToken } from 'api/auth';
import FreeTimesTable from 'components/FreeTimesTable';
import { CalendarOutlined } from '@ant-design/icons';
import { Form, Button, DatePicker, TimePicker} from 'antd';
import Spinner from 'react-bootstrap/Spinner'
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css'
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css'

const Moment = require('moment');
const MomentRange = require('moment-range');
const moment = MomentRange.extendMoment(Moment);
const hostURL = "http://localhost:8662";

function disabledDate(current) { return current < moment().endOf('day');}


class FreeTimes extends React.Component {

    constructor() {
        super();
        this.state = {
            isLoaded: false,
            error:null,
            freeTimes:[],
            id: getUser().id
        }
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
        let date1 = day+'.'+month+'.'+year;

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

        let potentialPeriod = {date: date1,
                                start_time: startTime,
                                end_time: endTime,
                                student_id: getUser().id}
        
        let overlap = false;
        for (let i = 0; i < this.state.freeTimes.length; i++) {
            if (this.state.freeTimes[i].date === potentialPeriod.date)
                if (potentialPeriod.start_time < this.state.freeTimes[i].end_time && this.state.freeTimes[i].start_time < potentialPeriod.end_time) {
                    overlap = true;
                    break;
                }
        }
        if (overlap === false) 
            axios.post('http://localhost:8082/freeTime', { 
                                                            date: potentialPeriod.date,
                                                            start_time: potentialPeriod.start_time,
                                                            end_time: potentialPeriod.end_time,
                                                            student_id: getUser().id}
                                                            ,
                                                            { headers: { 'Authorization': 'Bearer ' + getToken()}})
            .then(res=>{window.location = "/freeTimes";}).catch(error=>{});
       else
            window.confirm('You have entered an overlapping period!');
    }
  

    componentDidMount()  {
        axios.get(hostURL + "/raspored/freeTimesByStudent/" + this.state.id).then(
            res => {
                const freeTimes = res.data;
                this.setState( {freeTimes});
                this.state.isLoaded = true;
            }
        ).catch(error=>{
                     this.setState({
                         isLoaded: true,
                         error
                     });
         });
    }

    showFreeTime() {
         return this.state.freeTimes.map((ft, index) => {
             const {date, start_time, end_time} = ft
             return (
                 <tr key={date}>
                     <td>{date}</td>
                     <td>{start_time}</td>
                     <td>{end_time}</td>
                 </tr>
             )
         })
     }

    render() {
        return(
            <div>
                {this.state.isLoaded ? (this.state.freeTimes.length > 0 
                                        ? <div>
                                                <FreeTimesTable freeTimes={this.state.freeTimes} />
                                                <div style={{ position:"relative", marginLeft:"60%"}}id="formaKartica">
                                                    <article id = "karticaFormeFreeTimes" className="mw6 center bg-white shadow-5 br3 pa3 pa4-ns mv3 ba b--black-10">
                                                        <h5>Add a period of free time</h5>
                                                        <Form id="forma" name="normal_freetime" className="freetime-form" onFinish={this.onFinish} initialValues={{ remember: true, }}>
                                                            <Form.Item name="date" rules={[ { required: true, message: 'Please define the date!', }, ]}>
                                                                <DatePicker disabledDate={disabledDate} format="DD.MM.yyyy" prefix={<CalendarOutlined className="site-form-item-icon" />}/>
                                                            </Form.Item>
                                                            <Form.Item name="time" rules={[ { required: true, message: 'Please define the time period!', }, ]} >
                                                                <TimePicker.RangePicker format="HH:mm" />
                                                            </Form.Item>
                                                            <Form.Item>
                                                                <Button type="primary" htmlType="submit"> Submit </Button>
                                                            </Form.Item>
                                                        </Form>
                                                    </article>
                                                </div>
                                            </div>
                                        : <div>
                                            <h2 style={{paddingTop:"20px",  margin: "0 auto"}}>No freetimes have been defined.</h2>
                                            <div style={{paddingTop:"50px"}}id="formaKartica">
                                                <article style={{top:"50%",margin: "0 auto"}} id = "karticaFormeFreeTimes" className="mw6 center bg-white shadow-5 br3 pa3 pa4-ns mv3 ba b--black-10">
                                                    <h5>Add a period of free time</h5>
                                                    <Form id="forma" name="normal_freetime" className="freetime-form" onFinish={this.onFinish} initialValues={{ remember: true, }}>
                                                        <Form.Item name="date" rules={[ { required: true, message: 'Please define the date!', }, ]}>
                                                            <DatePicker disabledDate={disabledDate} format="DD.MM.yyyy" prefix={<CalendarOutlined className="site-form-item-icon" />}/>
                                                        </Form.Item>
                                                        <Form.Item name="time" rules={[ { required: true, message: 'Please define the time period!', }, ]} >
                                                            <TimePicker.RangePicker format="HH:mm" />
                                                        </Form.Item>
                                                        <Form.Item>
                                                            <Button type="primary" htmlType="submit"> Submit </Button>
                                                        </Form.Item>
                                                    </Form>
                                                </article>
                                            </div>
                                          </div>)
                                    : <Spinner animation="border" />
                }
            </div>
        )
    }

}

export default FreeTimes;