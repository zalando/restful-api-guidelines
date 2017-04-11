import React from 'react';
import {Link} from 'react-router';

export function App(props) {
  return (
    <div className="dc-page">
      <div className="dc-container">
        <div>
          <h1 className="dc-h1">Zally API Linter</h1>
          <h3 className="dc-h2">
            Check if your&nbsp;
            <a href="http://swagger.io/specification/" target="_blank" className="dc-link">SWAGGER Schema</a> conforms to&nbsp;
            <a href="http://zalando.github.io/restful-api-guidelines/" target="_balnk" className="dc-link">Zalando's REST API Guidelines</a>
          </h3>
        </div>

        <div className="navigation">
          <Link to="/" className="dc-link navigation__link" activeClassName="navigation__link--active">BY URL</Link>
          <Link to="/editor" className="dc-link navigation__link" activeClassName="navigation__link--active">EDITOR</Link>
        </div>

        <div className="child-routes-container">
          {/*Mount child routes*/}
          {props.children}
        </div>
      </div>
    </div>
  )
}
