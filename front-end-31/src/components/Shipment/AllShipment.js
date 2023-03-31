import Menu from '../Menu/MenuBar';
import React, { useState,useEffect } from 'react';
import { useNavigate } from 'react-router-dom';


function AllShipment(props) {

 const [firstName, setFirstName] = useState("");
 const [lastName, setLastName] = useState("");
 const [error, setError] = useState("");
 const[email, setEmail] = useState("");
 const[smnt, setShipment] = useState(false);
 const [shipmentData, setShipmentData] = useState(null);
 const navigate = useNavigate();

useEffect(() => {
  async function fetchData() {
    try {
      const response = await fetch('http://localhost:8080/shipments', {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('auth_token')
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setShipmentData(data.shipment1);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }
  fetchData();
}, []);


const add = () => {
    navigate('/addnew');
}

const shipments = (shipmentData) => {
  console.log(shipmentData)
  if (!shipmentData != null){
        console.log(shipmentData)
    const { firstName, lastName, email, accountType } = props.smnt;
    return (
      <div>
        <React.Fragment>
          <Menu props={{ firstName, lastName, email, accountType }} />
          <h3 className="title">Shipments</h3>
          <div>
            <table>
<thead>
  <tr>
    <th>ID</th>
    <th>Delivery Address</th>
    <th>Delivery City</th>
    <th>Delivery Contact Number</th>
    <th>Delivery Date</th>
    <th>Delivery Instructions</th>
    <th>Delivery State</th>
    <th>Delivery Status</th>
    <th>Delivery Time</th>
    <th>Delivery Zip Code</th>
    <th>Height</th>
    <th>Length</th>
    <th>Receiver Address</th>
    <th>Receiver City</th>
    <th>Receiver Contact Number</th>
    <th>Receiver Name</th>
    <th>Receiver State</th>
    <th>Receiver Zip Code</th>
    <th>Sender Address</th>
    <th>Sender City</th>
    <th>Sender Contact Number</th>
    <th>Sender ID</th>
    <th>Sender Name</th>
    <th>Sender State</th>
    <th>Sender Zip Code</th>
    <th>Shipment Date</th>
    <th>Shipment Status</th>
    <th>Shipment Type</th>
    <th>Weight</th>
    <th>Width</th>
  </tr>
</thead>
              <tbody>
                {
                  <tr key={shipmentData}>
<td>{shipmentData && shipmentData.deliveryAddress !== 'NULL' ? shipmentData.deliveryAddress : 'N/A'}</td>
<td>{shipmentData && shipmentData.deliveryCity !== 'NULL' ? shipmentData.deliveryCity : 'N/A'}</td>
<td>{shipmentData && shipmentData.deliveryContactNumber !== 'NULL' ? shipmentData.deliveryContactNumber : 'N/A'}</td>
<td>{shipmentData && shipmentData.deliveryDate !== 'NULL' ? shipmentData.deliveryDate : 'N/A'}</td>
<td>{shipmentData && shipmentData.deliveryInstructions !== 'NULL' ? shipmentData.deliveryInstructions : 'N/A'}</td>
<td>{shipmentData && shipmentData.deliveryState !== 'NULL' ? shipmentData.deliveryState : 'N/A'}</td>
<td>{shipmentData && shipmentData.deliveryStatus !== 'NULL' ? shipmentData.deliveryStatus : 'N/A'}</td>
<td>{shipmentData && shipmentData.deliveryTime !== 'NULL' ? shipmentData.deliveryTime : 'N/A'}</td>
<td>{shipmentData && shipmentData.deliveryZipCode !== 'NULL' ? shipmentData.deliveryZipCode : 'N/A'}</td>
<td>{shipmentData && shipmentData.height !== 'NULL' ? shipmentData.height : 'N/A'}</td>
<td>{shipmentData && shipmentData.length !== 'NULL' ? shipmentData.length : 'N/A'}</td>
<td>{shipmentData && shipmentData.receiverAddress !== 'NULL' ? shipmentData.receiverAddress : 'N/A'}</td>
<td>{shipmentData && shipmentData.receiverCity !== 'NULL' ? shipmentData.receiverCity : 'N/A'}</td>
<td>{shipmentData && shipmentData.receiverContactNumber !== 'NULL' ? shipmentData.receiverContactNumber : 'N/A'}</td>
<td>{shipmentData && shipmentData.receiverName !== 'NULL' ? shipmentData.receiverName : 'N/A'}</td>
<td>{shipmentData && shipmentData.receiverState !== 'NULL' ? shipmentData.receiverState : 'N/A'}</td>
<td>{shipmentData && shipmentData.receiverZipCode !== 'NULL' ? shipmentData.receiverZipCode : 'N/A'}</td>
<td>{shipmentData && shipmentData.senderAddress !== 'NULL' ? shipmentData.senderAddress : 'N/A'}</td>
<td>{shipmentData && shipmentData.senderCity !== 'NULL' ? shipmentData.senderCity : 'N/A'}</td>
<td>{shipmentData && shipmentData.senderContactNumber !== 'NULL' ? shipmentData.senderContactNumber : 'N/A'}</td>
<td>{shipmentData && shipmentData.senderID !== 'NULL' ? shipmentData.senderID : 'N/A'}</td>
<td>{shipmentData && shipmentData.senderName !== 'NULL' ? shipmentData.senderName : 'N/A'}</td>
<td>{shipmentData && shipmentData.senderState !== 'NULL' ? shipmentData.senderState : 'N/A'}</td>
<td>{shipmentData && shipmentData.senderZipCode !== 'NULL' ? shipmentData.senderZipCode : 'N/A'}</td>
<td>{shipmentData && shipmentData.shipmentDate !== 'NULL' ? shipmentData.shipmentDate : 'N/A'}</td>
<td>{shipmentData && shipmentData.shipmentStatus !== 'NULL' ? shipmentData.shipmentStatus : 'N/A'}</td>
<td>{shipmentData && shipmentData.shipmentType !== 'NULL' ? shipmentData.shipmentType : 'N/A'}</td>
<td>{shipmentData && shipmentData.weight !== 'NULL' ? shipmentData.weight : 'N/A'}</td>
<td>{shipmentData && shipmentData.width !== 'NULL' ? shipmentData.width : 'N/A'}</td>

                  </tr>
                }
              </tbody>
            </table>
          </div>
          <button onClick={add} className="btn btn-outline-primary">
            new shipment
          </button>
        </React.Fragment>
      </div>
    );
  } else {
    const { firstName, lastName, email , accountType} = props.smnt;
    return (
      <div>
        <React.Fragment>
          <Menu props={{ firstName, lastName, email, accountType }} />
          <h3 className="title">Shipments</h3>
          <p>No shipments found.</p>
          <button onClick={add} className="btn btn-outline-primary">
            new shipment
          </button>
        </React.Fragment>
      </div>
    );
  }
};

return (
  <div>
    {shipments(shipmentData)}
  </div>
);
}

export default AllShipment;
