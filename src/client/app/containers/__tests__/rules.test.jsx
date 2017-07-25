import React from 'react';
import {Rules} from '../rules.jsx';
import {shallow} from 'enzyme';

describe('Rules container component', () => {
  let component, props, container, getSupportedRules;

  beforeEach(() => {
    getSupportedRules = jest.fn();
    props = {
      route: { getSupportedRules: getSupportedRules },
      location: { query: {} }
    };
    component = shallow(<Rules {...props}></Rules>);
    container = component.instance();
  });

  describe('when call fetchRules', () => {
    test('should handle success', () => {
      const rules = [{}];
      getSupportedRules.mockReturnValueOnce(Promise.resolve({
        rules: rules
      }));
      const promise = container.fetchRules();

      return promise.then(() => {
        expect(getSupportedRules).toHaveBeenCalled();
        expect(container.state.error).toBe(null);
        expect(container.state.pending).toBe(false);
        expect(container.state.ajaxComplete).toBe(true);
        expect(container.state.rules).toBe(rules);
      });
    });

    test('should handle failure', () => {
      const mockError = { detail: 'error' };
      getSupportedRules.mockReturnValueOnce(Promise.reject(mockError));

      container.fetchRules()
        .catch(() => {
          expect(getSupportedRules).toHaveBeenCalled();
          expect(container.state.error).toBe(mockError.detail);
          expect(container.state.pending).toBe(false);
          expect(container.state.ajaxComplete).toBe(true);
          expect(container.state.rules).toEqual([]);
        });
    });

    test('should handle failure without error detail', () => {
      getSupportedRules.mockReturnValueOnce(Promise.reject({}));

      container.fetchRules()
        .catch(() => {
          expect(getSupportedRules).toHaveBeenCalled();
          expect(container.state.error).toBe(Rules.DEFAULT_ERROR_MESSAGE);
          expect(container.state.pending).toBe(false);
          expect(container.state.ajaxComplete).toBe(true);
          expect(container.state.rules).toEqual([]);
        });
    });
  });

  describe('when call parseFilterValue', () => {
    test('should return null when null passed', () => {
      expect(container.parseFilterValue(null)).toBe(null);
    });
    test('should return null when empty object passed', () => {
      expect(container.parseFilterValue({})).toBe(null);
    });
    test('should return correct object when is_active false', () => {
      expect(container.parseFilterValue({'is_active': 'false'})).toEqual({is_active: false});
    });
    test('should return correct object when is_active true', () => {
      expect(container.parseFilterValue({'is_active': 'true'})).toEqual({is_active: true});
    });
  });
});
