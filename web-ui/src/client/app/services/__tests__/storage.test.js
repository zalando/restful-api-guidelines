/* global global, window */

import {Storage, PREFIX} from '../storage.js';

describe('Storage', () => {
  let mockLocalStorage;

  beforeEach(() => {
    mockLocalStorage = {
      getItem: jest.fn(),
      setItem: jest.fn()
    };
    global.window = {
      localStorage: mockLocalStorage
    };
  });

  afterEach(() => {
    delete global.window;
  });

  test('getItem retrieve an item from local storage with prefixed key', () => {
    mockLocalStorage.getItem.mockReturnValueOnce('world');
    const item = Storage.getItem('hello');
    expect(item).toEqual('world');
    expect(mockLocalStorage.getItem).toHaveBeenCalledWith(`${PREFIX}hello`);
  });

  test('setItem set item into local storage with prefixed key', () => {
    const item = 'world';
    Storage.setItem('hello', item);
    expect(mockLocalStorage.setItem).toHaveBeenCalledWith(`${PREFIX}hello`, item);
  });
});
