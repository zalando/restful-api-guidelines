import React from 'react';
import {shallow} from 'enzyme';
import {default as App} from '../app.jsx';

describe('App component', () => {

  const RestService = {
    getApiViolations (){}
  };

  test('should render the app', () => {
    const component = shallow(<App RestService={RestService} />);
    const page = component.find('.dc-page');
    const title = component.find('.dc-h2');
    const form = component.find('Form');

    expect(page.length).toEqual(1);
    expect(form.length).toEqual(1);
    expect(title.text()).toEqual('Zally Report');
  });

});
