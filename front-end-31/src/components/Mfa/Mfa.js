import LoginForm from '../Form/LoginForm';
import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from "react-router-dom";
import '../Form/LoginForm.css';

function MFA() {
  const [error, setError] = useState("");
  const [otp, setOtp] = useState("");
  const [username, setUser] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [qrCode, setQrCode] = useState("");
  const [isTokenOk, setTokenOk] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const user_and_qr = location.state;
  
  const handleOtpChange = (e) => {
    setOtp(e.target.value);
  };

  useEffect(() => {
    const user_with_no_mfa = localStorage.getItem("authenticated_no_2fa");
    setUser(user_and_qr.user);
    setQrCode(user_and_qr.qr);
    if (!user_with_no_mfa) {
      navigate("/");
    }
  }, []);

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const res = await postUserCredential(setError, {
        username,
        otp
      });
      console.log(res.auth_token)
 
      if (res && res.auth_token) {
        localStorage.setItem("auth_token", res.auth_token);
        localStorage.setItem("User_Id", res.User_id);
        setTokenOk(true);
        navigate("/main");
      }
    } catch (err) {
      console.log(err);
      setError("Error: try again");
    } finally {
      setIsLoading(false);
    }
  };

async function postUserCredential(setError, credentials) {
  try {
    const response = await fetch("http://localhost:8080/verify_otp", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        "X-Forwarded-For": "0.0.0.0",
      },
      body: JSON.stringify(credentials),
    });
    if (response.status === 401 || response.status === 400) {
      setError("UNAUTHORIZED");
      return null;
    } else if (response.status === 200) {
      const res = await response.json();
      setTokenOk(true);
      return res;
    } else {
      setError("Error: try again");
      return null;
    }
  } catch (err) {
    console.log(err);
    setError("Error: try again");
    return null;
  }
}


  return (
    <div className="loginContianer">
      <form className="loginForm" onSubmit={handleFormSubmit}>
        <div className="formContent">
          <h3 className="title">Scan Qr qode</h3>
          <div className="form-group mt-3">
            <div className="imageContainer">
              <img src={qrCode} />
            </div>
            <label>code</label>
            <input
              type="Text"
              className="form-control"
              placeholder="Enter code"
              onChange={handleOtpChange}
            />
          </div>
          <div>{error && <h1 className="error"> {error} </h1>}</div>
          <div className="d-grid gap-2 mt-5">
            <button type="submit" className="btn btn-outline-primary">
              Submit
            </button>
          </div>
          <p className="forgot-password text-right mt-2">
            <a href="/">Back</a>
          </p>
        </div>
      </form>
    </div>
  );
}

export default MFA;