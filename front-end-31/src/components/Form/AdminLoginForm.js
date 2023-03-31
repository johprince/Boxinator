import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import './LoginForm.css'


function Form(){

    const [username, setUser] = useState("");
    const [password, setPassword] = useState("");
    const [qrCode, setQrCode] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();
    
    async function postUserCredential(setError,code) {
    let data = null;

    try{
    const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'X-Forwarded-For':'0.0.0.0'
        },
        body: JSON.stringify(code)
    })
    .then(response => 
    response.json().then(data => ({
        data: data.qr_code_data_url,
        status: response.status
    })
     ).then(res => {
    setError("")
    console.log(res.data)
    return [res.status, res.data]
     }));

    return response;
    }catch(err){
        console.log(err)
        setError("Error: try again")
        }
}
      const handleEmailChange = (e) => {
      setUser(e.target.value);
      };

      const handlePasswordChange = (e) => {
      setPassword(e.target.value);
      };
      
        const handleFormSubmit = async e => {
            e.preventDefault();
            const qr_code = await postUserCredential(setError,{
                username,
                password
            });
          
            if(qr_code[0] == 200){
                const data = {
                    user:username,
                    qr:qr_code[1],
                };
                setQrCode(qr_code[1])
                setPassword("")
                console.log(qr_code)
                setError("")
                localStorage.setItem("authenticated_no_2fa", true);
                navigate('/mfa', { state:data })
            }else if(qr_code[0] == 400 || qr_code[0] == 401){
                setQrCode("")
                setError("UNAUTHORIZED")
            }else{
                setError("Error code: " + qr_code)
                setQrCode("")
            }
            
        }

    return(
        <div className="loginContianer">
         <form className="adminLoginForm" onSubmit={handleFormSubmit}>
          <div className="formContent">
           <h3 className="title">Admin Login</h3>
           <div className="form-group mt-3">
             <label className="email-label">Email address</label>
             <input
                type="email"
                className="form-control"
                placeholder="Enter email"
                onChange={handleEmailChange}
             />
            </div>
           <div className="form-group mt-4">
            <label className="pwd-label">Password (min 8 characters)</label>
            <input
              type="password"
              name="password" required
              minLength="8"
              className="form-control"
              placeholder="Enter password"
              onChange={handlePasswordChange}
            />
            </div>
          <div className="d-grid gap-2 mt-5">
            <button type="submit" className="btn btn-success">
              Submit
            </button>
          </div>
          </div>
         </form>
        </div>
    );
}
export default Form