import React from 'react';

import {Msg} from './dress-code.jsx';

export function RulesTab (props) {
  const rules = props.rules;
  return (
    <div>
      <h3>SUPPORTED RULES</h3>
      { props.error ? <Msg type="error" title="ERROR" text={props.error} closeButton={false} /> : null }
      <ul style={{padding: 0, listStyle: 'none'}}>{
        rules.map((rule, index) => {
          return (<Rule key={index} rule={rule} />);
        })
      }</ul>
    </div>
  );
}

export function Rule (props) {
  const rule = props.rule;
  return (
    <li style={{marginBottom: '32px', paddingBottom: '32px', borderBottom: '1px solid #ccc'}}>
      <h4 className="dc-h4">
        <RuleType type={rule.type} />
        {rule.type} {'\u2013'} {rule.title}
      </h4>
      { rule.url ? <RuleLink url={rule.url} /> : null}
      <p className="dc--text-small">{rule.is_active ? 'Active' : 'Inactive'}</p>
    </li>
  );
}

export function RuleType (props) {
  if ('MUST' === props.type)
    return <span className="dc-status dc-status--error"></span>;
  else if ('SHOULD' === props.type)
    return <span className="dc-status dc-status--new"></span>;
  else
    return <span className="dc-status dc-status--inactive"></span>;
}


export function RuleLink (props) {
  return (
    <p>
      Reference: <a href={props.url} className="dc-link" target="_blank">{props.url}</a>
    </p>
  );
}
