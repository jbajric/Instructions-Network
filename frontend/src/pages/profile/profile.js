import React from 'react';
import auth, {getToken, getUser} from 'api/auth';
import 'App.css';
import { Form, Input } from 'antd'
import 'antd/dist/antd.css';
import { Button } from 'reactstrap';
import axios from "axios";
import 'styles/profile/profile.css';
import { TrademarkOutlined, CommentOutlined, UserOutlined, MailOutlined } from '@ant-design/icons';

let user = getUser();
let hostURL = "http://localhost:8662";

class Profile extends React.Component {

  removeUser(){
    if(user.role[0].name==='STUDENT')
      axios.delete("http://localhost:8081/student/"+user.id, {headers: {'Authorization': 'Bearer '+ getToken()}})
            .then(auth.logout(() => {window.location.href = '/'}))
            .catch();
    else
      axios.delete("http://localhost:8081/instructor/"+user.id, {headers: {'Authorization': 'Bearer '+ getToken()}})
            .then(auth.logout(() => { window.location.href = '/' }))
            .catch();
  }

  onFinish = values => {
    if (user.name != values.name || user.surname != values.lastname || user.email != values.email) {
      if(user.role[0].name==='STUDENT')
        axios.put("http://localhost:8081/student/"+user.id, { first_name: values.name,
                                                              last_name: values.lastname,
                                                              email: values.email,
                                                              username: user.username,
                                                              password: user.password,
                                                              roles: [user.role[0]]},
                { headers: {'Authorization': 'Bearer '+ getToken()}}).then( user.name=values.name,
                                                                            user.surname=values.lastname,
                                                                            localStorage.setItem('user', JSON.stringify(user)),
                                                                            window.alert('Successful update!'),
                                                                            window.location = "/app");
      else
        axios.put("http://localhost:8081/instructor/"+user.id,  { first_name: values.name,
                                                                  last_name: values.lastname,
                                                                  email: values.email,
                                                                  username: user.username,
                                                                  password: user.password,
                                                                  roles: [user.role[0]]},
                  { headers: {'Authorization': 'Bearer '+ getToken()}}).then( user.name=values.name,
                                                                              user.surname=values.lastname,
                                                                              localStorage.setItem('user', JSON.stringify(user)),
                                                                              window.alert('Successful update!'),
                                                                              window.location = "/app");
    }
    else window.alert('You haven\'t updated any field!');
  };


  render() {
    return (
        <div id="profilFormadiv">
          <article size="80%" className="mw6 center bg-white shadow-5 br3 pa3 pa4-ns mv3 ba b--black-10">
            <h2> User profile </h2>
            <Form name="profilForma" initialValues={{ remember: true }} onFinish={this.onFinish}>
              
              <Form.Item name="name"
                      rules={[{ min:4, message: 'Your first name must be longer than 4!',}, 
                              { required: true, message: 'Please input your first name!' },
                              { pattern: "^[A-Za-z '.-]*$", message: 'First name should contain only letters and {\', ., -}, with the first letter capitalized!',}
                            ]}
                      initialValue={user.name}
              >
                <Input prefix={<UserOutlined className="site-form-item-icon" placeholder="First name" style={{paddingRight:"20px",}} />}/>
              </Form.Item>

              <Form.Item name="lastname"
                      rules={[{ min:4, message: 'Your last name must be longer than 4!',}, 
                              { required: true, message: 'Please input your last name!' },
                              { pattern: "^[A-Za-z '.-]*$", message: 'Last name should contain only letters and {\', ., -}, with the first letter capitalized!',}
                            ]}
                      initialValue={user.surname}
              >
                <Input prefix={<UserOutlined className="site-form-item-icon"  placeholder="Last name" style={{paddingRight:"20px",}}/>}/>
              </Form.Item>
              <Form.Item  name="email"
                          rules={[{ type: 'email', message: 'The input is not valid e-mail!',},
                                  { required: true, message: 'Please input your e-mail!',},
                                ]}
                          initialValue={user.email}
              >
                <Input prefix={<MailOutlined className="site-form-item-icon" placeholder="E-mail" style={{paddingRight:"20px",}}/>}/>
              </Form.Item>
              <Form.Item name="username" initialValue={user.username}>
                <Input disabled={true} prefix={<CommentOutlined className="site-form-item-icon" style={{paddingRight:"20px",}} />}/>
              </Form.Item>

              <Form.Item name="role" initialValue={user.role[0].name}>
                <Input disabled={true} prefix={<TrademarkOutlined className="site-form-item-icon" style={{paddingRight:"20px",}}/>}/>
              </Form.Item>
      
            <Form.Item size="small">
              <Button htmlType="submit" style={{marginRight:"20px"}}> Update account </Button>
              <Button onClick={(e) => {if (window.confirm('Are you sure you want to delete your account?')) this.removeUser(e)}}>Delete account</Button>
            </Form.Item>
          </Form>
          </article>
        </div>
    );
  };
};
        
export default Profile;
