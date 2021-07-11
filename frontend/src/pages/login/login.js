import React from 'react';
import { Form, Input, Button, Checkbox } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import 'styles/login/login.css';
import auth from "../../api/auth";
import { Link } from "react-router-dom";

class Login extends React.Component {
  
    onFinish = values => {
      auth.login(values);
    };


  render() {
  return (
    <div id="okvir">
    <div id="loginforma">
      <h2>Instructions Network Login</h2>
      <article className="mw6 center bg-white shadow-5 br3 pa3 pa4-ns mv3 ba b--black-10">
    
    <Form id="forma"
      name="normal_login"
      className="login-form"
      initialValues={{
        remember: true,
      }}
      onFinish={this.onFinish}
    >
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
      <Form.Item>
        <Form.Item name="remember" valuePropName="checked" noStyle>
          <Checkbox>Remember me</Checkbox>
        </Form.Item>
      </Form.Item>      
      <Form.Item>
          <Button type="primary" htmlType="submit" className="login-form-button" > 
            Log in
          </Button>
        </Form.Item>
    </Form>
    
    <Form.Item>
      New user?
      <Link to='/registration'>
        <span className="login-form-forgot"> Register now!</span>
      </Link>
    </Form.Item>
    </article>
    <h5 style={{color:"white"}}>NWT 2021</h5>
    </div>
    </div>
  );
}
};

export default Login;