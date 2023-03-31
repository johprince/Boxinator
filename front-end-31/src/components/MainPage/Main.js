import React, { useState,useEffect } from 'react';
import './style.css'
import { useNavigate, useLocation } from "react-router-dom";
import ReactDOM from 'react-dom';
import AllShipment from '../Shipment/AllShipment';
import AdminPanel from '../AdminPanel/Adminpanel';




function Main() {
  const [token, setToken] = useState("");
  const [User_Id, setUser_Id] = useState("");
  const [accountType, setAccountType] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [error, setError] = useState("");
  const[email, setEmail] = useState("");
  const[smnt, setShipment] = useState(false);


  const location = useLocation();
  const shipment = location.state;

  async function getUserInfo(token, account_id) {
    try {
      const response = await fetch('http://localhost:8080/account/' + account_id, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
          'Authorization':'Bearer ' + token
        },
      });

      if (response.status === 401 || response.status === 400) {
        throw new Error('UNAUTHORIZED');
      } else if (!response.ok) {
        throw new Error('Error: try again');
      }

      const json = await response.json();
      setFirstName(json.firstName);
      setLastName(json.lastName);
      setEmail(json.email);
      setAccountType(json.accountType)
      setError('');
      
      return json;
    } catch (err) {
      console.log(err);
      setError("Error: try again");
      return null;
    }
  }

useEffect(() => {
  async function fetchData() {
    try {
      const response = await fetch('http://localhost:8080/account/' + localStorage.getItem('User_Id'), {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('auth_token')
        },
      });
      if (response.status === 401 || response.status === 400) {
        throw new Error('UNAUTHORIZED');
      } else if (!response.ok) {
        throw new Error('Error: try again');
      }
      const json = await response.json();
      setFirstName(json.firstName);
      setLastName(json.lastName);
      setEmail(json.email);
      setError('');
      setAccountType(json.accountType)
      console.log(json.accountType)
    } catch (error) {
      setError(error.message);
    }
  }
  fetchData();
}, [User_Id, token]);


  const data = {
    firstName: firstName,
    lastName: lastName,
    email: email,
    smnt: smnt,
    accountType :accountType
  };

   if (accountType === 'ADMINISTRATOR') {
    return (
      <div>
        <AdminPanel props={data} />
      </div>
    );
  } else {
    return (
      <div>
        <AllShipment smnt={data} />
      </div>
    );
  }
}

export default Main;
