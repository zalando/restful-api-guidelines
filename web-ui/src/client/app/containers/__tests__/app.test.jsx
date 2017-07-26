import React from 'react';
import {shallow} from 'enzyme';
import {App} from '../app.jsx';

describe('App component', () => {

  test('should show UserInfo child component', () => {
    const route = {
      user: {},
      env: {},
      showUserInfo: true
    };
    const component = shallow(<App route={route}/>);
    const userInfo = component.find('UserInfo');
    expect(userInfo.length).toEqual(1);
  });

  test('should hide UserInfo child component', () => {
    const route = {
      user: {},
      env: {},
      showUserInfo: false
    };
    const component = shallow(<App route={route}/>);
    const userInfo = component.find('UserInfo');
    expect(userInfo.length).toEqual(0);
  });

  test('should render children', () => {
    const route = {
      env: {},
      user: {}
    };
    const component = shallow(
      <App route={route}>
        <div id="children">children</div>
      </App>
    );
    const children = component.find('#children');
    expect(children.length).toEqual(1);
  });
});
