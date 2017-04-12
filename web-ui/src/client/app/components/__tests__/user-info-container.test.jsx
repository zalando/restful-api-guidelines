import React from 'react';
import {shallow} from 'enzyme';

describe('UserInfoContainer component', () => {
  let UserInfoContainer, component;
  const firewallResponse = {
    cn: 'user-foo'
  };
  const mockGetUserName = jest.fn();
  const mockLogout = jest.fn();

  jest.mock('../../services/oauth-util.js', () => ({
    getUserName: mockGetUserName,
    logout: mockLogout
  }));

  describe('If OAUTH_ENABLED is false', () => {
    beforeEach(() => {
      global.window = {
        env: {
          'OAUTH_ENABLED': false
        }
      }
      UserInfoContainer = require('../user-info-container.jsx')['default'];
      component = shallow(<UserInfoContainer firewallResponse={firewallResponse}/>);
    });

    test('it should not render the component', () => {
      expect(component.type()).toBeNull();
    });
  });

  describe('If OAUTH_ENABLED is true', () => {
    beforeEach(() => {
      global.window = {
        env: {
          'OAUTH_ENABLED': true
        }
      };

      UserInfoContainer = require('../user-info-container.jsx')['default'];
      component = shallow(<UserInfoContainer firewallResponse={firewallResponse}/>);
    });

    test('should render the component', () => {
      const userInfo = component.find('UserInfo');
      expect(userInfo.length).toEqual(1);
    });

    test('should get username from response', () => {
      expect(mockGetUserName).toHaveBeenCalled();
      expect(mockGetUserName).toHaveBeenCalledWith(firewallResponse);
    });

    test('should call logout function for handling logout action', () => {
      const userInfo = component.find('UserInfo');
      userInfo.prop('handleLogout')({preventDefault() {}});

      expect(mockLogout).toHaveBeenCalled();
    })
  });
})
