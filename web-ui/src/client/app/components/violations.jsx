import React from 'react';
import {If} from './util.jsx';
import {Msg} from './dress-code.jsx';

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

      <If test={() => !!props.violation.rule_link }>
        <ViolationRuleLink ruleLink={props.violation.rule_link} />
      </If>

      <If test={() => !!props.violation.paths.length }>
        <ViolationPaths paths={props.violation.paths} />
      </If>
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
      Rule: <a href={props.ruleLink} className="dc-link">{props.ruleLink}</a>
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

function ViolationsResult(props){
  return (
    <div className="violations-result">
      <If test={() => props.pending}>
        <div className="violations-result__spinner"><div className="dc-spinner dc-spinner--small"></div></div>
      </If>
      <If test={() => !props.pending && props.complete && !props.errorMsgText && props.violations.length == 0}>
        <Msg type="success" title={props.successMsgTitle} text={props.successMsgText} closeButton={false}  />
      </If>
      <If test={() => !props.pending && props.complete && props.errorMsgText}>
        <Msg type="error" title="ERROR" text={props.errorMsgText} closeButton={false} />
      </If>
      <If test={() => !props.pending && props.complete && props.violations.length}>
        <Violations violations={props.violations} violationsCount={props.violationsCount}/>
      </If>
    </div>
  )
}

ViolationsResult.propTypes = {
  pending: React.PropTypes.bool,
  complete: React.PropTypes.bool,
  errorMsgText: React.PropTypes.string,
  successMsgTitle:  React.PropTypes.string.isRequired,
  successMsgText:  React.PropTypes.string.isRequired,
  violations: React.PropTypes.array
};

export {Violations, Violation, ViolationPaths, ViolationRuleLink, ViolationType, ViolationsResult}
