import React from 'react';

function Violations(props) {
  return (
    <div>
      {props.violations.length ? <h3>Violations</h3> : ''}
      <ul style={{padding: 0, listStyle: 'none'}}>{
        props.violations.map((violation, index) => {
          return (<Violation key={index} violation={violation} />)
        })
      }</ul>
    </div>
  )
}

function Violation(props) {
  return (
    <li style={{marginBottom: '32px', paddingBottom: '32px', borderBottom: '1px solid #ccc'}}>
      <h4 className="dc-h4">
        <ViolationType type={props.violation.violation_type} />
        {props.violation.violation_type} {'\u2013'} {props.violation.title}
      </h4>

      <p>{props.violation.description}</p>

      { props.violation.rule_link && <ViolationRuleLink ruleLink={props.violation.rule_link} /> }

      { props.violation.paths.length && <ViolationPaths paths={props.violation.paths} /> }
    </li>
  )
}

function ViolationType(props) {
  if("MUST" === props.type)
    return <span className="dc-status dc-status--error"></span>;
  else if("SHOULD" === props.type)
    return <span className="dc-status dc-status--new"></span>;
  else
    return <span className="dc-status dc-status--inactive"></span>;
}


function ViolationRuleLink(props) {
  return (
    <p>
      Rule: <a href="{props.ruleLink}" className="dc-link">{props.ruleLink}</a>
    </p>
  )
}

function ViolationPaths(props) {
  return (
    <span>
      <p>Paths:</p>
      <ul>{ props.paths.map((path, i) => { return <li key={i}>{path}</li> }) }</ul>
    </span>
  )
}

export {Violations, Violation, ViolationPaths, ViolationRuleLink, ViolationType}
