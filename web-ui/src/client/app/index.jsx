import React from 'react';
import {render} from 'react-dom';

class App extends React.Component {
  render () {
    return <Container />;
  }
}


class Container extends React.Component {
  render () {
    return (
      <div className="dc-page">
        <div className="dc-container">
          <div className="dc-card">
            <h2 className="dc-h2">Zally report</h2>
            <Form />
            <Violations />
          </div>
        </div>
      </div>
    );
  }
}

class Form extends React.Component {
  handleFormSubmit(){

  }

  render () {
    return (
      <form onSubmit={this.handleFormSubmit}>
          <label className="dc-label">Enter full path to your swagger file from repo </label>
          <input className="dc-input dc-input--block" type="url" name="path" placeholder="e.g https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/json/petstore.json" required pattern="https?://.+" />
          <button type="submit" className="dc-btn dc-btn--primary">Submit</button>
      </form>
    )
  }
}

class Violations extends React.Component {

  renderViolationType(type) {
    if("MUST" === type)
      return <span className="dc-status dc-status--error"></span>;
    else if("SHOULD" === type)
      return <span className="dc-status dc-status--new"></span>;
    else
      return <span className="dc-status dc-status--inactive"></span>;
  }

  render () {
    const data = [{violation_type: 'MUST', description: "Description", rule_link: "rulelink-", title: 'its a title', paths: ["patha", "pathsb"]},{violation_type: 'SHOULD', description: "Description 2", rule_link: "rulelink-2", title: 'its a title', paths: ["patha2", "pathsb2"]}];
    return (
      <div>
        <ul>{
          data.map((violation, index) => {
                    return (<li key={ index }>
                              <h4 className="dc-h4">
                                {this.renderViolationType(violation.violation_type)}
                                {violation.violation_type} {'\u2013'} {violation.title}
                              </h4>
                              <p>{violation.description}</p>

                              { violation.rule_link &&
                                <p>Rule: <a href="{violation.rule_link}" className="dc-link">{violation.rule_link}</a></p>
                              }

                              {violation.paths.length &&
                                <span>
                                  <p>Paths:</p>
                                  <ul>
                                    {
                                      violation.paths.map(function(path, i){
                                        return <li key={i}>{path}</li>
                                      })
                                    }
                                  </ul>
                                </span>
                              }
                            </li>);
                  })
        }</ul>
      </div>
    )
  }
}

render(<App/>, document.getElementById('app'));
