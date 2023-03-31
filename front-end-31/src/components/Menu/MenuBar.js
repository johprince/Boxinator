import React, { useState } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { NavLink,Route,useNavigate, useLocation } from 'react-router-dom';

 function handleAnchorClick(){
   localStorage.clear();       
  }
 
  
// we are using bootstrap elements here
function Menu({props}){
         console.log(props.accountType)
    const [error, setError] = useState("");
    const [shipment, setShipment] = useState("");
    const [data, setData] = useState([]);
    const navigate = useNavigate();
    
    const changeState = () => {
     
    } 

    const CompletedShipment = () => {

    fetch("http://localhost:8080/shipments/complete", {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("auth_token"),
      },
    })
      .then((response) => response.json())
      .then((x) => {
        setData(x)
      });
  };


    const handleClick = () => {
    fetch('http://localhost:8080/shipments', {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.getItem('auth_token')
    },
  }).then(response => response.json())
    .then(data => setData(data));
  ;}

 
 
   if (props.accountType === 'ADMINISTRATOR') {
  return (
    <React.Fragment>
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home">Welcome {props.firstName}</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="/main" onClick={handleClick}>
                All Shipments
              </Nav.Link>
              <Nav.Link href="/" onClick={handleAnchorClick}>
                Logout
              </Nav.Link>
              <Nav.Link href="/main" onClick={CompletedShipment}>
                Completed shipments
              </Nav.Link>
               <Nav.Link href="/main" onClick={changeState}>
                Change a shipments state
              </Nav.Link>
              <Nav.Link href="/main" onClick={changeState}>
              update the country multipliers
              </Nav.Link>
               <Nav.Link href="/main" onClick={changeState}>
              Show or update information
              </Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <div>
        <table key={data.id}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Address</th>
              <th>Delivery Status</th>
              <th>Sender ID</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{data.id}</td>
              <td>{data.senderName}</td>
              <td>{data.senderAddress}</td>
              <td>{data.deliveryStatus}</td>
              <td>{data.senderID}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </React.Fragment>
  );
}else if(props.accountType === 'REGISTERED_USER'){
  return (
    <Navbar bg="light" expand="lg">
      <Container>
        <Navbar.Brand href="#home">Welcome {props.firstName} {props.lastName}</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/main">All Shipments</Nav.Link>
            <Nav.Link href="/" onClick={handleAnchorClick}>Logout</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
     </Navbar>
  );
 }else{
        return (
    <Navbar bg="light" expand="lg">
      <Container>
        <Navbar.Brand href="#home">Welcome </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/">Home</Nav.Link>
            <Nav.Link href="/Registration" >Create User account</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
     </Navbar>
  );
 }
}
export default Menu