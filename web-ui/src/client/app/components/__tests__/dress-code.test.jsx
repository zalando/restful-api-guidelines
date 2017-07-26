import React from 'react';
import {shallow} from 'enzyme';
import {Msg} from '../dress-code.jsx';

describe('Msg component', () => {

  test('with default props should show the close button', () => {
    const component = shallow(<Msg />);
    const closeButton = component.find('.dc-msg__close');

    expect(closeButton.length).toEqual(1);
  });

  test('with default props should use "info" type', () => {
    const component = shallow(<Msg />);
    const container = component.find('.dc-msg--info');

    expect(container.length).toEqual(1);
  });

  test('should show title and text', () => {
    const component = shallow(
      <Msg title="Hello" text="World"/>
    );
    const text = component.find('.dc-msg__text');
    const title = component.find('.dc-msg__title');

    expect(title.text()).toEqual('Hello');
    expect(text.text()).toEqual('World');
  });

  test('should use the type defined via related prop', () => {
    const component = shallow(<Msg type="alert"/>);
    const defaultContainer = component.find('.dc-msg--info');
    const container = component.find('.dc-msg--alert');

    expect(defaultContainer.length).toEqual(0);
    expect(container.length).toEqual(1);
  });

  test('should invoke callback when close button is clicked', () => {
    const onCloseButtonClick = jest.fn();
    const component = shallow(<Msg onCloseButtonClick={onCloseButtonClick}/>);
    component.find('.dc-msg__close').simulate('click');

    expect(onCloseButtonClick).toHaveBeenCalled();
  });

  test('should hide close button', () => {
    const component = shallow(<Msg closeButton={false}/>);

    expect(component.find('.dc-msg__close').length).toEqual(0);
  });

});
