import React from 'react';
import Menu from '../Menu/MenuBar';
import { useLocation } from 'react-router-dom';

function AdminPanel(props) {
 
  const { firstName, lastName, email, accountType } = props.props

  return (
    <React.Fragment>    
      <h1>Welcome to the Admin Panel!</h1>
      <p>Here's where you can manage all the content on your site.</p>
      <Menu props={{ firstName, lastName, email, accountType }} />
    </React.Fragment>
  );
}

export default AdminPanel;
