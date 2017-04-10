import React from 'react';
import {shallow} from 'enzyme';
import {default as Form} from '../form.jsx';

describe('Form component', () => {

  const getApiViolations = jest.fn()
    .mockReturnValueOnce(Promise.resolve({ violations: [{}], violationsCount: 1}))
    .mockReturnValueOnce(Promise.reject('bar'));

  const RestService = {
    getApiViolations
  };

  test('should render the form', () => {
    const component = shallow(<Form RestService={RestService} />);
    const label = component.find('.dc-label');
    const input = component.find('.dc-input');
    const submit = component.find('.dc-btn--primary');

    expect(label.length).toEqual(1);
    expect(input.length).toEqual(1);
    expect(submit.length).toEqual(1);
  });

  test('should handle input value change', () => {
    const component = shallow(<Form RestService={RestService} />);
    const input = component.find('.dc-input');
    const event = {
      target: {
        value: 'foo'
      }
    }
    input.simulate('change', event);
    expect(component.state('inputValue')).toEqual('foo');
  });

  describe('when subitting the form', ()=>{
    let form, component, input, event;
    beforeEach(() => {
      component = shallow(<Form RestService={RestService} />);
      input = component.find('.dc-input');
    });
    afterEach(() => {
      getApiViolations.mockClear();
    });
    test('should handle success', () => {
      event = {
        target: {
          value: 'URL_WITH_GOOD_SCHEMA'
        }
      }
      input.simulate('change', event);
      form = component.find('form');
      form.simulate('submit', {preventDefault() {}});
      expect(getApiViolations.mock.calls[0][0]).toBe('URL_WITH_GOOD_SCHEMA');
    });

    test('should handle failure', () => {
      event = {
        target: {
          value: 'URL_WITH_AN_ERROR'
        }
      }
      input.simulate('change', event);
      form = component.find('form');

      form.simulate('submit', {preventDefault() {}});
      expect(getApiViolations.mock.calls[0][0]).toBe('URL_WITH_AN_ERROR');
    });
  });
});
