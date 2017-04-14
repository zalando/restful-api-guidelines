import React from 'react';
import {shallow} from 'enzyme';
import {ViolationType, ViolationRuleLink, ViolationPaths, Violations, Violation, ViolationsResult} from '../violations.jsx';

describe('ViolationType component', () => {
  describe('when violationType is MUST', () => {
    test('should return dc-status--error', () => {
      const component = shallow(<ViolationType type='MUST' />);
      const status = component.find('.dc-status--error');

      expect(status.length).toEqual(1);
    });
  });

  describe('when violationType is SHOULD', () => {
    test('should return dc-status--new', () => {
      const component = shallow(<ViolationType type='SHOULD' />);
      const status = component.find('.dc-status--new');

      expect(status.length).toEqual(1);
    });
  });

  describe('when violationType niether MUST nor SHOULD', () => {
    test('should return dc-status--inactive', () => {
      const component = shallow(<ViolationType type='' />);
      const status = component.find('.dc-status--inactive');

      expect(status.length).toEqual(1);
    });
  });
});

describe('ViolationRuleLink component', () => {
  test('should return a rule link', () => {
    const component = shallow(<ViolationRuleLink ruleLink='foo'/>);
    const link = component.find('a');

    expect(link.length).toEqual(1);
    expect(link.text()).toEqual('foo');
  });
});

describe('ViolationPaths component', () => {
  test('should return list of paths', () => {
    const paths = ['#/definitions/Pet', '#/foo/bar'];
    const component = shallow(<ViolationPaths paths={paths} />);
    const li = component.find('li');

    expect(li.length).toEqual(2);
  });
});

describe('Violations component', () => {
  test('should return list of violation', () => {
    const violations = [{}, {}];
    const component = shallow(<Violations violations={violations} />);
    const violation = component.find('Violation');

    expect(violation.length).toEqual(2);
  });
});


describe('Violation component', () => {
  test('should render a violation', () => {
    const violation = {
      'rule': {
        'name':'NoUnusedDefinitionsRule',
        'title':'Do not leave unused definitions',
        'violationType':'SHOULD',
        'url':'',
        'code':'S005'
      },
      'title':'Do not leave unused definitions',
      'description':'Found 1 unused definitions',
      'violation_type':'SHOULD',
      'rule_link':'link',
      'paths':['#/definitions/Pet']
    };

    const component = shallow(<Violation violation={violation} />);
    const IfViolationRuleLinkSelector = 'If[dataTestId="if-violation-rule-link"]';
    const IfViolationRuleLink = component.find(IfViolationRuleLinkSelector);
    const ViolationRuleLink = IfViolationRuleLink.dive().find('ViolationRuleLink');
    const IfViolationPathsSelector = 'If[dataTestId="if-violation-paths"]';
    const IfViolationPaths = component.find(IfViolationPathsSelector);
    const ViolationPaths = IfViolationPaths.dive().find('ViolationPaths');

    expect(component.find('p').text()).toEqual('Found 1 unused definitions');
    expect(component.find('ViolationType').length).toEqual(1);
    expect(ViolationRuleLink.length).toEqual(1);
    expect(ViolationRuleLink.prop('ruleLink')).toEqual('link');
    expect(ViolationPaths.length).toEqual(1);
    expect(ViolationPaths.prop('paths')).toEqual(violation.paths);
  });
});

describe('ViolationsResult component', () => {
  describe('when state is pending', () => {
    test('should render loading spinner', () => {
      const component = shallow(<ViolationsResult
          pending={true}
          complete={false}
          errorMsgText={null}
          violations={[]}
          successMsgTitle='Good Job!'
          successMsgText='No violations found in the analyzed schema.'
        />);

      expect(component.find('If[dataTestId="if-loading"]').dive().find('.dc-spinner--small').length).toEqual(1);
    });
  });

  describe('when state is complete with no violations', () => {
    test('should render well done message', () => {
      const component = shallow(<ViolationsResult
          pending={false}
          complete={true}
          errorMsgText={null}
          violations={[]}
          successMsgTitle='Good Job!'
          successMsgText='No violations found in the analyzed schema.'
        />);

      const IfSuccessSelector = 'If[dataTestId="if-success"]';
      const IfSuccess = component.find(IfSuccessSelector);
      const Msg = IfSuccess.dive().find('Msg');

      expect(Msg.length).toEqual(1);
      expect(Msg.prop('type')).toEqual('success');
      expect(Msg.prop('title')).toEqual('Good Job!');
      expect(Msg.prop('text')).toEqual('No violations found in the analyzed schema.');
    });
  });

  describe('when state is complete with errors', () => {
    test('should render error  message', () => {
      const component = shallow(<ViolationsResult
          pending={false}
          complete={true}
          errorMsgText={'Server Error'}
          violations={[]}
          successMsgTitle=''
          successMsgText=''
        />);

      const IfErrorSelector = 'If[dataTestId="if-error"]';
      const IfError = component.find(IfErrorSelector);
      const Msg = IfError.dive().find('Msg');

      expect(Msg.length).toEqual(1);
      expect(Msg.prop('type')).toEqual('error');
      expect(Msg.prop('title')).toEqual('ERROR');
      expect(Msg.prop('text')).toEqual('Server Error');
    });
  });

  describe('when state is complete with violations', () => {
    test('should render violations', () => {
      const violations = [{}, {}];
      const component = shallow(<ViolationsResult
          pending={false}
          complete={true}
          errorMsgText={null}
          violations={violations}
          violationsCount ={2}
          successMsgTitle=''
          successMsgText=''
        />);

      const IfViolationsSelector = 'If[dataTestId="if-violations"]';
      const IfViolations = component.find(IfViolationsSelector);
      const Violations = IfViolations.dive().find('Violations');

      expect(Violations.length).toEqual(1);
      expect(Violations.prop('violations')).toEqual(violations);
      expect(Violations.prop('violationsCount')).toEqual(2);
    });
  });
});
