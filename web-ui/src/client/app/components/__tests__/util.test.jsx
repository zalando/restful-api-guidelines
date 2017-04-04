import React from 'react';
import {shallow} from 'enzyme';
import {If} from '../util.jsx';

test('If component should hide child component based on test prop result', () => {
  const iff = shallow(
    <If test={() => false}>
    	<div>Hello World!</div>
    </If>
  );
  expect(iff.find('div').length).toEqual(0);
});

test('If component should show child component based on test prop result', () => {
  const iff = shallow(
    <If test={() => true}>
    	<div>Hello World!</div>
    </If>
  );
  expect(iff.find('div').length).toEqual(1);
});
