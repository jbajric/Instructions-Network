import React from 'react'
import axios from 'axios'

class Requests extends React.Component {

    constructor() {
        super();
    }

    getCookieHeader = () => {
        return {
            withCredentials: true,
            headers: {"Access-Control-Allow-Origin": "*", 'Access-Control-Allow-Credentials': true, 'Content-Type': 'application/json'}
        }
    }

    getAuthorizationHeader = (token) => {
        return {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        }
    }

    handleError = (error, callback, failurecallback) => {
        if (error.response == null)
            callback("Please check your internet connection!", "warning", null);
        else if (failurecallback != null)
            failurecallback(error.response.data.message);
        else 
            callback(error.response.data.message, "warning", null);
    }

    sendPostRequest(url, params, headers, successcallback, failurecallback) {
        axios
            .post(url, params, headers)
            .then((response) => { successcallback(response); })
            .catch(error => { this.handleError(error, successcallback, failurecallback); });
    }

    sendGetRequest(url, params, callback) {
        axios
            .get(url, params)
            .then((response) => { callback(null, null, response.data); })
            .catch(error => { this.handleError(error, callback, callback); });
    }

    sendPutRequest(url, params, headers, successcallback, failurecallback) {
        axios
            .put(url, params, headers)
            .then((response) => { successcallback(response); })
            .catch(error => { this.handleError(error, successcallback, failurecallback); });
    }

    sendDeleteRequest(url, params, successcallback, failurecallback) {
        axios
            .delete(url, params)
            .then((response) => { successcallback(response); })
            .catch(error => { this.handleError(error, successcallback, failurecallback); });
    }
}

export default new Requests();