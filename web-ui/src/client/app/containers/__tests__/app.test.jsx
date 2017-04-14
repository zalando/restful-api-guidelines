import React from 'react';
import {shallow} from 'enzyme';
import {App} from '../app.jsx';

describe('App component', () => {

  test('should render the app', () => {
    const route = {
      user: {},
      logout: jest.fn()
    };
    const component = shallow(<App route={route}/>);
    const page = component.find('.dc-page');
    const title = component.find('.dc-h1');
    const userInfo = component.find('UserInfo');

    expect(page.length).toEqual(1);
    expect(userInfo.length).toEqual(1);
    expect(title.length).toEqual(1);
  });
});
