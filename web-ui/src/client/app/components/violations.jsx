import React from 'react';

export default class Violations extends React.Component {

  constructor(props) {
    super(props);
  }

  renderViolationType(type) {
    if("MUST" === type)
      return <span className="dc-status dc-status--error"></span>;
    else if("SHOULD" === type)
      return <span className="dc-status dc-status--new"></span>;
    else
      return <span className="dc-status dc-status--inactive"></span>;
  }

  render () {
    return (
      <div>
        {this.props.violations.length ? <h3>Violations</h3> : ''}
        <ul style={{padding: 0, listStyle: 'none'}}>{
          this.props.violations.map((violation, index) => {
            return (<li key={ index } style={{marginBottom: '32px', paddingBottom: '32px', borderBottom: '1px solid #ccc'}}>
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
