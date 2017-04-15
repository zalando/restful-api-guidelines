import React from 'react';
import {Link} from 'react-router';
import {If} from '../components/util.jsx';
import UserInfo from '../components/user-info.jsx';

export function App (props) {
  const user = props.route.user;
  const showUser = props.route.showUser;
  const logout = props.route.logout;
  return (
  <div>
    <div className="main-navigation-bar">
      <h1 className="dc-h1 main-navigation-bar__title">
        <Link to="/" className="main-navigation-bar__link">
          <img className="main-navigation-bar__logo" src="/assets/logo.png" />
          Zally API Linter
        </Link>
      </h1>
      <If test={() => showUser === true}>
        <UserInfo username={user.username} onLogout={logout}/>
      </If>
    </div>

    <div className="dc-page page-container">
      <div className="dc-container">
        <h4 className="dc-h4">
          Check if your&nbsp;
          <a href="http://swagger.io/specification/" target="_blank" className="dc-link">SWAGGER Schema</a> conforms to&nbsp;
          <a href="http://zalando.github.io/restful-api-guidelines/" target="_balnk" className="dc-link">Zalando's REST API Guidelines</a>
        </h4>

        <div className="tab-navigation">
          <Link to="/" className="dc-link tab-navigation__link" activeClassName="tab-navigation__link--active">BY URL</Link>
          <Link to="/editor" className="dc-link tab-navigation__link" activeClassName="tab-navigation__link--active">EDITOR</Link>
        </div>
        <div className="tab-contents">
          {/* Mount child routes*/}
          {props.children}
        </div>
      </div>
    </div>
    <footer>
      <a className="dc-link" href="https://github.com/zalando-incubator/zally" target="_blank">Github Project</a> - Copyright ZALANDO SE 2016
    </footer>
  </div>);
}
