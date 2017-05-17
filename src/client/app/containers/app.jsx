import React from 'react';
import {Link} from 'react-router';
import UserInfo from '../components/user-info.jsx';

export function App (props) {
  const {user, showUserInfo, logout, login, env} = props.route;
  const MOUNTPATH = env.MOUNTPATH || '/';

  return (
  <div>
    <div className="main-navigation-bar">
      <h1 className="dc-h1 main-navigation-bar__title">
        <Link to="/" className="main-navigation-bar__link">
          <img className="main-navigation-bar__logo" src={MOUNTPATH + 'assets/logo.png'} />
          Zally API Linter
        </Link>
      </h1>
      { showUserInfo ?
        <UserInfo username={user.username}
          authenticated={user.authenticated}
          onLogin={login}
          onLogout={logout}
        />
      : null }
    </div>

    <div className="dc-page page-container">
      {/* Mount child routes*/}
      { props.children }
    </div>
    <footer>
      <a className="dc-link" href="https://github.com/zalando-incubator/zally" target="_blank">Github Project</a> - Copyright ZALANDO SE 2016
    </footer>
  </div>);
}
