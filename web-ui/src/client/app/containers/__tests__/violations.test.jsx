import {Violations} from '../violations.jsx';

describe('Violations container component', () => {
  let container, event, getApiViolations;

  beforeEach(() => {
    getApiViolations = jest.fn();
    container = new Violations({
      route: { getApiViolations: getApiViolations }
    });
    container.setState = jest.fn();
    event = {
      preventDefault: jest.fn(),
      target: {}
    };
  });

  describe('when call handleOnInputValueChange', () => {
    test('should set the correct state', () => {
      event.target.value = 'foo';
      container.handleOnInputValueChange(event);
      expect(container.setState).toHaveBeenCalledWith({inputValue: 'foo'});
    });
  });

  describe('when call handleFormSubmit', () => {
    test('should handle success', () => {
      const violations = [{}];
      const violationsCount = 1;
      getApiViolations.mockReturnValueOnce(Promise.resolve(violations, violationsCount));
      container.state.inputValue = 'URL_WITH_GOOD_SCHEMA';

      const promise = container.handleFormSubmit(event);
      expect(container.setState).toHaveBeenCalled();

      return promise.then(() => {
        expect(event.preventDefault).toHaveBeenCalled();
        expect(getApiViolations.mock.calls[0][0]).toBe('URL_WITH_GOOD_SCHEMA');
        expect(container.setState).toHaveBeenCalled();
      });
    });

    test('should handle failure', (done) => {
      container.state.inputValue = 'URL_WITH_AN_ERROR';
      getApiViolations.mockReturnValueOnce(Promise.reject());
      container.handleFormSubmit(event).catch(() => {
        try {
          expect(getApiViolations.mock.calls[0][0]).toBe('URL_WITH_AN_ERROR');
          done();
        } catch (e) {
          done.fail(e);
        }
      });
    });
  });
});
