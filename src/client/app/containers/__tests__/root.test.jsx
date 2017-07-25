import React from 'react';
import {shallow} from 'enzyme';
import {Root} from '../root.jsx';

jest.mock('../editor.jsx', () => ({
  Editor: () => {}
}));

describe('Root container component', () => {
  let MockRestService;

  beforeEach(() => {
    MockRestService = {
      getApiViolationsByURL: jest.fn(),
      getApiViolationsBySchema: jest.fn(),
      getSupportedRules: jest.fn()
    };
  });

  test('should "register" /login route if auth is enabled', () => {
    const component = shallow(<Root env={{ OAUTH_ENABLED: true }} RestService={MockRestService} />);
    expect(component.find('[path="/login"]').length).toEqual(1);
  });

  test('should not "register" /login route if auth is not enabled', () => {
    const component = shallow(<Root env={{ OAUTH_ENABLED: false }} RestService={MockRestService} />);
    expect(component.find('[path="/login"]').length).toEqual(0);
  });
});
