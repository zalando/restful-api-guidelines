import React from 'react';
import {shallow} from 'enzyme';
import {URL} from '../url.jsx';

describe('URL container component', () => {
  let MockStorage, route;

  beforeEach(() => {
    MockStorage = {
      setItem: jest.fn(),
      getItem: jest.fn()
    };
    route = { 'Storage': MockStorage };
  });

  test('should set expected state values when instantiated', () => {
    const value = 'http://github.com/petstore.json';
    MockStorage.getItem.mockReturnValueOnce(value);
    const component = shallow(<URL route={route} />);
    expect(component.state().inputValue).toEqual(value);
  });


  test('on input value change save new value in the Storage and update the state accordingly', () => {
    const value = 'http://github.com/petstore.json';
    const newValue = 'http://github.com/swagger.json';
    MockStorage.getItem.mockReturnValueOnce(value);
    const component = shallow(<URL route={route} />);

    component.instance().handleOnInputValueChange({
      target: { value: newValue }
    });

    expect(MockStorage.setItem).toHaveBeenCalledWith('url-value', newValue);

    expect(component.state().inputValue).toEqual(newValue);
  });

  test('should use an empty string as input value if Storage doesn\'t contain an item', () => {
    const component = shallow(<URL route={route} />);
    expect(component.state().inputValue).toEqual('');
  });
});
