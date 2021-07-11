import React from 'react';
import { getToken } from 'api/auth';
import { Card } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import ToolkitProvider from 'react-bootstrap-table2-toolkit';
import filterFactory, { textFilter } from 'react-bootstrap-table2-filter';
import paginationFactory from 'react-bootstrap-table2-paginator';
import { DeleteOutlined } from '@ant-design/icons';
import axios from 'axios';
import { Button } from 'antd';

import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css'
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css'
import '../styles/freeTimes/freeTimes.css';


function handleDeletePeriod (id) {
    axios.delete("http://localhost:8082/freeTime/" + id, {
        headers: { 'Authorization': 'Bearer '+ getToken()}}).then( window.location.reload()).catch(); 
}


function FreetimesTable(props) {

    const columns = [
        {
            dataField: 'date',
            text: 'Date',
            sort: true,
            filter: textFilter()
        },
        {
            dataField: 'start_time',
            text: 'Start time',
            sort: true,
            filter: textFilter()
        },
        {
            dataField: 'end_time',
            text: 'End time',
            sort: true,
            filter: textFilter()
        },
        {
            dataField: 'a',
            text: 'Delete',
            formatter: (value, row) => {
                return <Button onClick=
                        {() => {if (window.confirm('Are you sure you want to delete this period?'))
                                    handleDeletePeriod(row.id)}}>
                                        <DeleteOutlined style={{ position:"relative", margin:"0 auto", color: "red"}}/> </Button>
            }
        }
    ];

    const options = {
            paginationSize: 2,
            pageStartIndex: 0,
            hidePageListOnlyOnePage: true,
            prePageText: 'Back',
            nextPageText: 'Next',
            sizePerPageList:[ {
                text: '5 elements', value: 5
              }, {
                text: '10 elements', value: 10
              }, {
                text: '15 elements', value: 15
              }, {
                text: 'All elements', value: props.freeTimes.length
              } ],
    };


    return (
        <div id = "freeTimesTable">
            <ToolkitProvider
                keyField="date"
                data={ props.freeTimes }
                columns={ columns }
                search
                >
                {
                    propsdata => (
                    <div id = "freeTimesTableNamediv">
                        <div id = "sadrzaj">
                            <Card id ="freeTimesTableName">
                                <Card.Body> <h2>Timetable</h2></Card.Body>
                            </Card>
                            <BootstrapTable { ...propsdata.baseProps } filter={ filterFactory() } pagination={ paginationFactory(options) } />
                        </div>
                    </div>
                    )
                }
            </ToolkitProvider>
        </div>
    )




}

export default FreetimesTable;