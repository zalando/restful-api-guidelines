import React from 'react';
import {shallow} from 'enzyme';
import {Rule, RuleType, RuleLink} from '../rules.jsx';

describe('RuleType component', () => {
  describe('when ruleType is MUST', () => {
    test('should return dc-status--error', () => {
      const component = shallow(<RuleType type='MUST' />);
      const status = component.find('.dc-status--error');

      expect(status.length).toEqual(1);
    });
  });

  describe('when ruleType is SHOULD', () => {
    test('should return dc-status--new', () => {
      const component = shallow(<RuleType type='SHOULD' />);
      const status = component.find('.dc-status--new');

      expect(status.length).toEqual(1);
    });
  });

  describe('when ruleType niether MUST nor SHOULD', () => {
    test('should return dc-status--inactive', () => {
      const component = shallow(<RuleType type='' />);
      const status = component.find('.dc-status--inactive');

      expect(status.length).toEqual(1);
    });
  });
});

describe('RuleLink component', () => {
  test('should return a rule link', () => {
    const component = shallow(<RuleLink url='foo'/>);
    const link = component.find('a');
    expect(link.length).toEqual(1);
    expect(link.text()).toEqual('foo');
    expect(link.node.props.href).toEqual('foo');
  });
});

describe('Rule component', () => {
  test('should render a rule with url', () => {
    const rule = {
      'name':'NoUnusedDefinitionsRule',
      'title':'Do not leave unused definitions',
      'type':'SHOULD',
      'url':'someurl',
      'code':'S005'
    };

    const component = shallow(<Rule rule={rule} />);
    const RuleLink = component.find('RuleLink');

    expect(component.find('RuleType').length).toEqual(1);
    expect(RuleLink.length).toEqual(1);
    expect(RuleLink.prop('url')).toEqual('someurl');
    expect(component.find('RuleType').length).toEqual(1);
  });

  test('should render a rule without url', () => {
    const rule = {
      'name':'NoUnusedDefinitionsRule',
      'title':'Do not leave unused definitions',
      'type':'SHOULD',
      'url': null,
      'code':'S005'
    };

    const component = shallow(<Rule rule={rule} />);
    const RuleLink = component.find('RuleLink');

    expect(component.find('RuleType').length).toEqual(1);
    expect(RuleLink.length).toEqual(0);
    expect(component.find('RuleType').length).toEqual(1);
  });
});
