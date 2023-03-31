import React, { useState,useEffect } from 'react';
import Menu from '../Menu/MenuBar';
import { useLocation } from 'react-router-dom';
import './style.css';

function AddNew(){
    const [showPopup, setShowPopup] = useState(false);
    const [boxNamee, setBoxName] = useState('');
    const [name, setName] = useState('');
const [address, setAddress] = useState('');

    const handleSubmit = (event) => {
  event.preventDefault();

};
 const handleNameChange = (event) => {
  setName(event.target.value);
};

const handleAddressChange = (event) => {
  setAddress(event.target.value);
};

  const handleClick = (boxNumber) => {
    console.log(`Box ${boxNumber} was clicked`);
    setShowPopup(true);
    
   
         if('Basic' === boxNumber) setBoxName("Basic")
         else if("Humble" === boxNumber) setBoxName("Humble")
         else if("Deluxe" === boxNumber)setBoxName("Deluxe")
         else if("Deluxe"===boxNumber)  setBoxName( "Deluxe")
         else if("Premium"===boxNumber)setBoxName("Premium")
 

  }

  return (
    <React.Fragment>    
      <h1> Tier of mystery box!</h1>
  <div className="box-container">
  <div className="box basic" onClick={() =>handleClick('Basic')}>
    <h2>Basic</h2>
    <p>1Kg Box</p>
  </div>
  <div className="box humble" onClick={() =>handleClick('Humble')}>
    <h2>Humble</h2>
    <p>2Kg Box</p>
  </div>
  <div className="box deluxe" onClick={() =>handleClick('Deluxe')}>
    <h2>Deluxe</h2>
    <p>5Kg Box</p>
  </div>
  <div className="box premium" onClick={() =>handleClick('Premium')}>
    <h2>Premium</h2>
    <p>8Kg Box</p>
  </div>
  {showPopup && (
  <div className="popup">
    <div className="popup">
  <h2>Enter box information : {boxNamee}</h2>
  <form onSubmit={handleSubmit}>
    <label htmlFor="name">Name:</label>
    <input type="text" id="name" value={name} onChange={handleNameChange} />

    <label htmlFor="address">Address:</label>
    <input type="text" id="address" value={address} onChange={handleAddressChange} />

    <button type="submit">Submit</button>
  </form>
</div>
  </div>
)}
</div>

    </React.Fragment>
  );
}

export default AddNew;