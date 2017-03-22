import React from 'react';
import Form from './form.jsx';

export default function App(props) {
  return (
    <div className="dc-page">
      <div className="dc-container">
        <div className="dc-card">
          <h2 className="dc-h2" style={{marginTop: '15px'}}>Zally Report</h2>
          <Form RestService={props.RestService} />
        </div>
      </div>
    </div>
  )
}
