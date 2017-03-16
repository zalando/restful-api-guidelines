import React from 'react';

export default class Violations extends React.Component {

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
