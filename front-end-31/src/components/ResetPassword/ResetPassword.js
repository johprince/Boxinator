import React, { useState } from "react"
import './ResetPassword.css'

async function postResetPassword(credintial) {
    return fetch('http://localhost:3000/resetpassword', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credintial)
    })
    .then(data => data.json())
}


function ResetPassword(){
      const [email, setEmail] = useState("");
      const [userName, setUserName] = useState("");

      const handleEmailChange = (e) => {
      setEmail(e.target.value);
      };
      const handleUserNameChange = (e) => {
      setUserName(e.target.value);
      };
      
        const handleFormSubmit = async e => {
            e.preventDefault();
            const data = await postResetPassword({
                email,
                userName
            });
        }
    return(
        <div className="resetPasswordContianer">
         <form className="resetForm" submit={handleFormSubmit}>
          <div className="resetFormContent">
           <h3 className="title">Reset Password</h3>
           <div className="form-group mt-3">
             <label>Username</label>
             <input
                type="Text"
                name="user" required
                className="form-control"
                placeholder="Username"
                onChange={handleUserNameChange}
             />
            </div>
           <div className="form-group mt-4">
            <label>Email address</label>
            <input
              type="email"
              name="email" required
              className="form-control"
              placeholder="Enter Email address"
              onChange={handleEmailChange}
            />
            </div>
          <div className="d-grid gap-2 mt-5">
            <button type="submit" className="btn btn-outline-primary">
              Submit
            </button>
          </div>
          </div>
         </form>
        </div>
    );
}
export default ResetPassword