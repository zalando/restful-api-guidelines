import React from 'react';
import {shallow} from 'enzyme';
import {Editor} from '../editor.jsx';

jest.mock('../../components/editor.jsx', () => ({
  EditorInputForm: () => {}
}));

describe('Editor container component', () => {
  let MockStorage, route;

  beforeEach(() => {
    MockStorage = {
      setItem: jest.fn(),
      getItem: jest.fn()
    };
    route = { 'Storage': MockStorage };
  });

  test('should set expected state values when instantiated', () => {
    const editorValue = 'prop: foo';
    MockStorage.getItem.mockReturnValueOnce(editorValue);
    const component = shallow(<Editor route={route} />);
    expect(component.state().editorValue).toBe(editorValue);
  });

  test('should set expected state values when componentDidMount', () => {
    const editorValue = 'prop: foo';
    MockStorage.getItem.mockReturnValueOnce(editorValue);
    const component = shallow(<Editor route={route} />);
    component.instance().componentDidMount();
    expect(component.state().inputValue).toEqual({prop: 'foo'});
  });


  test('on input value change save new value in the Storage and update the state accordingly', () => {
    const editorValue = 'prop: foo';
    const newEditorValue = 'foo: prop';
    MockStorage.getItem.mockReturnValueOnce(editorValue);
    const component = shallow(<Editor route={route} />);

    component.instance().handleOnInputValueChange(newEditorValue);

    expect(MockStorage.setItem).toHaveBeenCalledWith('editor-value', newEditorValue);

    expect(component.state().editorValue).toBe(newEditorValue);
    expect(component.state().inputValue).toEqual({ foo: 'prop'});
  });

  test('should use an empty string as editor value and input value if Storage doesn\'t contain an item', () => {
    const component = shallow(<Editor route={route} />);

    expect(component.state().editorValue).toBe('');
    expect(component.state().inputValue).toBe('');
  });

  test('should show an en error if schema parsing fails', () => {

    MockStorage.getItem.mockReturnValueOnce('invalidyaml: \'2');
    const component = shallow(<Editor route={route} />);

    component.instance().componentDidMount();

    expect(component.state().editorError).toBeDefined();
    expect(component.state().editorError.name).toEqual('YAMLException');
  });
});
