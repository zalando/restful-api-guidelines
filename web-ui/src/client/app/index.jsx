import React from 'react';
import {render} from 'react-dom';
import {firewall} from './oauth';
import Form from './components/form.jsx';

class App extends React.Component {
  render () {
    return (
      <div className="dc-page">
        <div className="dc-container">
          <div className="dc-card">
            <h2 className="dc-h2" style={{marginTop: '15px'}}>Zally Report</h2>
            <Form />
          </div>
        </div>
      </div>
    );
  }
}

firewall().then(() => {
  render(<App />, document.getElementById('app'));
});

