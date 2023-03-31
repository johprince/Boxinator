import React, { useState, useEffect } from 'react';
import './LoginForm.css'
import DatePicker from 'react-date-picker';
import Modal from 'react-modal';
import { useNavigate } from "react-router-dom";

async function postUserRegistrationData(userData) {
    let data = null;

    try{
    console.log(userData)
    const response = await fetch('http://localhost:8080/account', 
    {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    });

    
    data = response;
    return data;
    }catch(err)  {
      console.log(err.error);
    }
}


function Form(){

    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [password, setPwd1] = useState("");
    const [pwd2, setPwd2] = useState("");
    const [dateOfBirth, onChange] = useState(new Date());
    const [countryResidence, setCountryOfResidence] = useState("");
    const [zipcode, setZipCode] = useState(0);
    const [phoneNumber, setContactNumber] = useState(0);
    const [errorMessage, setErrorMessage] = useState('');
    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate();


     const cor= (e) => {
         setCountryOfResidence(e.target.value)

     };
      const handleChange = (e) => {
    
          switch(e.target.name){
              case "firstName":
               setFirstName(e.target.value)
               break;
              case "lastName":
               setLastName(e.target.value)
               break;
             case "email":
               setEmail(e.target.value)
               break;
             case "password1":
               setPwd1(e.target.value)
               break;
             case "password2":
               setPwd2(e.target.value)
               break;
             case "zipCode":
               setZipCode(e.target.value)
               break;
            case "contactNumber":
              setContactNumber(e.target.value)
              break;
            default:
               break;

          }
      };

      
        const handleFormSubmit = async e => {
            e.preventDefault();
            const result = await postUserRegistrationData({
               firstName,
               lastName,
               email,
               password,
               dateOfBirth,
               countryResidence,
               zipcode,
               phoneNumber
            });
            
            if(result.status == 201){
                setIsOpen(true);
            }
            
        }

        function handleModalClose(){
           setIsOpen(false);
           navigate('/')
        }

        function handleModalOk(){
            setIsOpen(false)
            navigate('/')
        }
    return(
        <div className="registrationLoginContianer">
         <form className="registrationLoginForm" onSubmit={handleFormSubmit}>
          <div className="formContent">
           <h3 className="title">Registration</h3>
           {errorMessage && (<p className="error"> {errorMessage} </p>)}
           <div className="form-group mt-3">
             <label>First name</label>
             <input
                type="Text"
                className="form-control"
                placeholder="First name"
                name="firstName" required
                pattern="[a-zA-ZæåøÆØÅ]*"
                onChange={handleChange}
                onInvalid={e => console.log(e)}
             />
            </div>
           <div className="form-group mt-4">
            <label>Last Name</label>
            <input
              type="Text"
              name="lastName" required
              className="form-control"
              placeholder="Last name"
              onChange={handleChange}
            />
            </div>
             <div className="form-group mt-3">
             <label>Email address</label>
             <input
                type="email"
                name='email' required
                className="form-control"
                placeholder="Enter email"
                onChange={handleChange}
             />
            </div>
             <div className="form-group mt-4">
            <label>Password (min 8 characters)</label>
            <input
              type="password"
              name="password1" required
              minLength="8"
              className="form-control"
              placeholder="Enter password"
              onChange={handleChange}
            />
            </div>
            <label>Password (min 8 characters)</label>
            <input
              type="password"
              name="password2" required
              minLength="8"
              className="form-control"
              placeholder="Enter password"
              onChange={handleChange}
            />
              <div>
             <div>
             <label>Date of birth</label>
              <DatePicker className="form-control" onChange={onChange} value={dateOfBirth} required format="MM/dd/yyyy"/>
             </div>
           
             </div>
            <label>Country of residence</label>
            <input
              type="Text"
              name="countryOfResidence" required
              className="form-control"
              placeholder="Enter Country of residence"
              onChange={cor}
            />

             <div className="form-group mt-4">
            <label>Zip code / postal code</label>
            <input
              type="Text"
              name="zipCode" required
              className="form-control"
              placeholder="zip code"
              onChange={handleChange}
            />
            </div>


            <div className="form-group mt-4">
            <label>Contact number</label>
            <input
              type="Text"
              name="contactNumber" required
              className="form-control"
              placeholder="Contact number"
              onChange={handleChange}
            />
            </div>
          <div className="d-grid gap-2 mt-5">
            <button type="submit" className="btn btn-outline-primary">
              Submit
            </button>
          </div>
           <p className="forgot-password text-right mt-2">
            <a href="/">Login</a>
           </p>
          </div>
                <Modal
                isOpen={isOpen}
                onRequestClose={handleModalClose}
                contentLabel="User created"
                >
                <h1>Login with email and password.</h1>
                <button onClick={handleModalOk}>Ok</button>
                </Modal>
         </form>
        </div>
    );
}
export default Form
 