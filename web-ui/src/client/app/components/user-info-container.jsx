import React from 'react';
import {getUserName, logout} from '../services/oauth-util.js';
import UserInfo from './user-info.jsx';

const css = `
    .main-container{
      position: relative;
    }
    .user-info{
      position: absolute;
      top: 0;
      right: 1rem;
      padding: 1rem 1.5rem;
    }
    .user-info__icon{
      font-size: 2rem;
      height: 5.5rem;
      line-height: 2;
    }
    .user-info__icon span{
      font-size: 1.4rem;
      padding-left: 0.7rem;
      line-height: normal;
      font-style: normal;
    }
    .user-info__logout{
      line-height: 5rem;
    }
    .main-container__title{
      marginTop: '15px'
    }
  `;

export default function UserInfoContainer(props){

  const showUserInfo = window.env.OAUTH_ENABLED;

  if(!showUserInfo){
    return null;
  }

  const username = getUserName(props.firewallResponse);
  const handleLogout = function (event){
    event.preventDefault();
    logout();
  };

  return(
    <section>
      <style>{css}</style>
      <UserInfo username={username} handleLogout={handleLogout}/>
    </section>
  )
}
