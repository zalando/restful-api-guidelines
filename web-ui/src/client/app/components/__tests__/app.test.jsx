import React from 'react';
import {shallow} from 'enzyme';

describe('App component', () => {

  global.window = {
    env: {
      'OAUTH_ENABLED': ''
    }
  };

  const App = require('../app')['default'];

  const RestService = {
    getApiViolations (){}
  };

  test('should render the app', () => {
    const component = shallow(<App RestService={RestService} />);
    const page = component.find('.dc-page');
    const title = component.find('.dc-h2');
    const form = component.find('Form');
    const userInfoContainer = component.find('UserInfoContainer');

    expect(page.length).toEqual(1);
    expect(form.length).toEqual(1);
    expect(userInfoContainer.length).toEqual(1);
    expect(title.text()).toEqual('Zally Report');
  });
});
