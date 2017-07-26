import React from 'react';
import {If} from './util.jsx';
import {Msg} from './dress-code.jsx';
import {RuleType} from './rules.jsx';

export function Violations (props) {
  return (
    <div>
      {props.violations.length ? <h3>VIOLATIONS</h3> : ''}
      <ul style={{padding: 0, listStyle: 'none'}}>{
        props.violations.map((violation, index) => {
          return (<Violation key={index} violation={violation} />);
        })
      }</ul>
    </div>
  );
}

export function Violation (props) {
  return (
    <li style={{marginBottom: '32px', paddingBottom: '32px', borderBottom: '1px solid #ccc'}}>
      <h4 className="dc-h4">
        <RuleType type={props.violation.violation_type} />
        {props.violation.violation_type} {'\u2013'} {props.violation.title}
      </h4>

      <p>{props.violation.description}</p>

      <If test={() => !!props.violation.rule_link } dataTestId="if-violation-rule-link" >
        <ViolationRuleLink ruleLink={props.violation.rule_link} />
      </If>

      <If test={() => !!props.violation.paths.length } dataTestId="if-violation-paths">
        <ViolationPaths paths={props.violation.paths} />
      </If>
    </li>
  );
}

export function ViolationRuleLink (props) {
  return (
    <p>
      Rule: <a href={props.ruleLink} className="dc-link" target="_blank">{props.ruleLink}</a>
    </p>
  );
}

export function ViolationPaths (props) {
  return (
    <span>
      <p>Paths:</p>
      <ul>{ props.paths.map((path, i) => { return <li key={i}>{path}</li>; }) }</ul>
    </span>
  );
}

export function ViolationsResult (props){
  return (
    <div className="violations-result">
      <If test={() => props.pending} dataTestId="if-loading">
        <div className="violations-result__spinner"><div className="dc-spinner dc-spinner--small"></div></div>
      </If>
      <If test={() => !props.pending && props.complete && !props.errorMsgText && props.violations.length === 0} dataTestId="if-success">
        <Msg type="success" title={props.successMsgTitle} text={props.successMsgText} closeButton={false}  />
      </If>
      <If test={() => !props.pending && props.complete && props.errorMsgText} dataTestId="if-error">
        <Msg type="error" title="ERROR" text={props.errorMsgText} closeButton={false} />
      </If>
      <If test={() => !props.pending && props.complete && props.violations.length} dataTestId="if-violations">
        <Violations violations={props.violations} violationsCount={props.violationsCount}/>
      </If>
    </div>
  );
}

ViolationsResult.propTypes = {
  pending: React.PropTypes.bool,
  complete: React.PropTypes.bool,
  errorMsgText: React.PropTypes.string,
  successMsgTitle:  React.PropTypes.string.isRequired,
  successMsgText:  React.PropTypes.string.isRequired,
  violations: React.PropTypes.array
};


