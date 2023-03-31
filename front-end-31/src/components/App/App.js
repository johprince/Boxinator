import React, { useState }from 'react';
import './App.css';
import Login from '../Login/Login';
import ResetPassword from '../ResetPassword/ResetPassword';
import AdminLoginForm from '../Form/AdminLoginForm';
import RegistrationForm from '../Form/RegistrationForm';
import MFA from '../Mfa/Mfa';
import Main from '../MainPage/Main';
import AdminPanel from '../AdminPanel/Adminpanel';
import AddNew from '../Shipment/Addnew';
import { BrowserRouter, Routes, Route } from "react-router-dom"

function App() {


  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/Admin-login" element={<AdminLoginForm />} />
        <Route path="/Registration" element={<RegistrationForm/>} />
        <Route path="/main" element={<Main />} />
        <Route path="/mfa" element={<MFA />} />
         <Route path="/Adminpanel" element={<AdminPanel />} />
         <Route path="/addnew" element={<AddNew />} />
        
      </Routes>
    </BrowserRouter>
  )
}

export default App