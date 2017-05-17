import React from 'react';
import {Violations} from '../violations.jsx';
import {shallow} from 'enzyme';

describe('Violations container component', () => {
  let component, props, container, event, getApiViolations;

  beforeEach(() => {
    getApiViolations = jest.fn();
    props = {
      route: { getApiViolations: getApiViolations }
    };
    component = shallow(<Violations {...props}></Violations>);
    container = component.instance();
    event = {
      preventDefault: jest.fn(),
      target: {}
    };
  });

  describe('when call handleOnInputValueChange', () => {
    test('should set the correct state', () => {
      event.target.value = 'foo';
      container.handleOnInputValueChange(event);
      expect(container.state.inputValue).toBe(event.target.value);
    });
  });

  describe('when call handleFormSubmit', () => {
    test('should handle success', () => {
      const violations = [{}];
      const violationsCount = 1;
      getApiViolations.mockReturnValueOnce(Promise.resolve({
        violations: violations,
        violations_count: violationsCount
      }));
      container.state.inputValue = 'URL_WITH_GOOD_SCHEMA';

      const promise = container.handleFormSubmit(event);

      return promise.then(() => {
        expect(event.preventDefault).toHaveBeenCalled();
        expect(getApiViolations.mock.calls[0][0]).toBe('URL_WITH_GOOD_SCHEMA');
        expect(container.state.violations).toEqual(violations);
        expect(container.state.violationsCount).toEqual(violationsCount);
      });
    });

    test('should handle failure', (done) => {
      const mockError = { detail: 'error' };
      container.state.inputValue = 'URL_WITH_AN_ERROR';
      getApiViolations.mockReturnValueOnce(Promise.reject(mockError));
      container.handleFormSubmit(event).catch(() => {
        try {
          expect(getApiViolations.mock.calls[0][0]).toBe('URL_WITH_AN_ERROR');
          expect(container.state.error).toEqual(mockError.detail);
          done();
        } catch (e) {
          done.fail(e);
        }
      });
    });

    test('should handle failure and use DEFAULT_ERROR_MESSAGE if expected error field is undefined', (done) => {
      container.state.inputValue = 'URL_WITH_AN_ERROR';
      getApiViolations.mockReturnValueOnce(Promise.reject({}));
      container.handleFormSubmit(event).catch(() => {
        try {
          expect(getApiViolations.mock.calls[0][0]).toBe('URL_WITH_AN_ERROR');
          expect(container.state.error).toEqual(Violations.DEFAULT_ERROR_MESSAGE);
          done();
        } catch (e) {
          done.fail(e);
        }
      });
    });
  });
});
