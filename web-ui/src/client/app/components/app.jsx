import React from 'react';
import Form from './form.jsx';
import UserInfoContainer from './user-info-container.jsx';

const css = `
  .main-container{
    position: relative;
  }
`;

export default function App(props) {
  return (
    <div className="dc-page">
      <style>{css}</style>
      <div className="dc-container main-container">
        <div className="dc-card">
          <h2 className="dc-h2 main-container__title">Zally Report</h2>
          <UserInfoContainer firewallResponse={props.firewallResponse} />
          <Form RestService={props.RestService} />
        </div>
      </div>
    </div>
  )
}
