import React from 'react';
import 'App.css';

import { ProtectedRoute } from "./protected.route";
import { BrowserRouter, Route, Switch, Link } from "react-router-dom";

import Login from 'pages/login/login'
import Registration from 'pages/registration/registration'
import Profile from 'pages/profile/profile';
import MainPage from 'pages/main-page/main-page';
import Help from 'pages/help/help';
import NotFound from 'pages/not-found/not-found';
import Reservations from 'pages/reservations/reservations';
import ReservationsInstructor from "./pages/reservationsInstructor/reservationsInstructor";
import AppointmentsInstructor from "./pages/appointmentsInstructor/appointmentsInstructor";

import { Layout, Menu, Avatar } from 'antd';
import 'styles/main-page/main-page.css';
import { UsergroupAddOutlined, FieldTimeOutlined, DesktopOutlined, QuestionCircleFilled, SettingFilled, CloseCircleFilled, CalendarOutlined, ScheduleOutlined  } from '@ant-design/icons';
import auth from "./api/auth";
import { getUser } from "./api/auth";
import AppointmentsStudent from "./pages/appointmentsStudent/appointmentsStudent";
import Appointment from "./pages/appointment/appointment";
import FreeTimes from "./pages/freeTimes/freeTimes";

const checkChangedTabs = () => {
  let href = window.location.href.split('/');
  href = href[href.length - 1];
  return href;
}

const { SubMenu } = Menu;
const {Content, Sider } = Layout;
let userName = "";
if (getUser() != null && getUser().name != null) userName = getUser().name + " " + getUser().surname;

class App extends React.Component {

  state = {
    currentLocation: ''
  }

  clickedMenu = () => {
    this.setState({ currentLocation: checkChangedTabs() });
  }


  componentDidMount() {
    this.setState({ currentLocation: checkChangedTabs() });
    setInterval(() => {
      this.setState({ currentLocation: checkChangedTabs() });
    }, 500);
  }

  render() {

    return (
        <BrowserRouter>
          <div className="App">
            <Layout style={{ height: '100vh' }}>
              <Sider style={{ paddingTop: '15px', }}
                     breakpoint="lg"
                     collapsedWidth="0"
                     onBreakpoint={broken => {
                     }}
                     onCollapse={(collapsed, type) => {
                     }}
              >
                <div id='siderGlavni'>
                  <Avatar size="large" src="/avataricon.png"/>
                </div>
                <div className="logo" />
                <Menu theme="dark" mode="inline" defaultSelectedKeys={['/' + this.state.currentLocation]} selectedKeys={['/' + this.state.currentLocation]} onClick={() => { this.clickedMenu(); }}>
                  <SubMenu style={{ textAlign: 'center' }}
                    title={
                      <span id="imeKorisnika"> {userName} </span>
                    } 
                  >
                    <Menu.ItemGroup key='Username' style={{ textAlign: 'left' }}>
                      <Menu.Item key='/profile'>
                        <Link to='./profile'>
                          <SettingFilled />
                          <span className="nav-text">Profile</span>
                        </Link>
                      </Menu.Item>
                      <Menu.Item key='LogOut' onClick={() => {
                        auth.logout(() => {
                          window.location.href = '/'
                        });
                      }}>
                        <CloseCircleFilled />
                        Log Out</Menu.Item>
                    </Menu.ItemGroup>
                  </SubMenu>
                  <Menu.Item className="subMenuItem" key="/app">
                    <Link to='./app'>
                      <DesktopOutlined />
                      <span className="nav-text">Main Page</span>
                    </Link>
                  </Menu.Item>
                  {getUser() && getUser().role.length > 0 && getUser().role[0].name==='STUDENT' 
                  ? (
                    <Menu.Item className="subMenuItem" key="/freeTimes">
                        <Link to='./freeTimes'>
                          <ScheduleOutlined/>
                          <span className="nav-text">Timetable</span>
                        </Link>
                    </Menu.Item>)
                    :
                    <Menu.Item className="subMenuItem" key="/appointmentsInstructor">
                    <Link to='./appointmentsInstructor'>
                      <CalendarOutlined />
                      <span className="nav-text">My Appointments</span>
                    </Link>
                  </Menu.Item>}
                  {getUser() && getUser().role.length > 0 && getUser().role[0].name==='STUDENT' 
                  ? (
                  <Menu.Item className="subMenuItem" key="/appointmentsStudent">
                    <Link to='./appointmentsStudent'>
                      <CalendarOutlined />
                      <span className="nav-text">Appointments</span>
                    </Link>
                  </Menu.Item>)
                  :
                  <Menu.Item className="subMenuItem" key="/reservationsInstructor">
                    <Link to='./reservationsInstructor'>
                      <FieldTimeOutlined/>
                      <span className="nav-text">Reservations</span>
                    </Link>
                  </Menu.Item>}
                  {getUser() && getUser().role.length > 0 && getUser().role[0].name==='STUDENT' ? (<Menu.Item className="subMenuItem" key="/reservations">
                    <Link to='./reservations'>
                      <FieldTimeOutlined/>
                      <span className="nav-text">My Reservations</span>
                    </Link>
                  </Menu.Item>)
                  : null}
                  <Menu.Item key="/help" className="subMenuItem">
                    <Link to="./help">
                      <QuestionCircleFilled />
                      <span className="nav-text">About Us</span>
                    </Link>
                  </Menu.Item>
                </Menu>
              </Sider>
              <Layout>
                <div id="NaslovApp">
                  <div><UsergroupAddOutlined /> Instructions Network</div>
                </div>
                <Content id="bodyMain">
                  <Switch>
                    <Route exact path="/" component={Login} />
                    <Route exact path="/login" component={Login} />
                    <Route exact path="/registration" component={Registration} />
                    <ProtectedRoute exact path="/app" component={MainPage} />
                    <ProtectedRoute exact path="/reservations" component={Reservations} />
                    <ProtectedRoute exact path="/appointmentsInstructor" component={AppointmentsInstructor} />
                    <ProtectedRoute exact path="/appointmentsStudent" component={AppointmentsStudent} />
                    <ProtectedRoute exact path="/reservationsInstructor" component={ReservationsInstructor} />
                    <ProtectedRoute exact path="/createAppointment" component={Appointment} />
                    <ProtectedRoute exact path="/help" component={Help} />
                    <ProtectedRoute exact path="/profile" component={Profile} />
                    <ProtectedRoute exact path="/freeTimes" component={FreeTimes} />

                    <Route path="*" component={NotFound} />
                  </Switch>
                </Content>
              </Layout>
            </Layout>
          </div>
        </BrowserRouter>
    );}
}

export default App;