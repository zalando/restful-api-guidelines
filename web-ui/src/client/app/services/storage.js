export const PREFIX = 'zally_';

export function createKey (key) {
  return `${PREFIX}${key}`;
}

export const Storage = {
  setItem (key, value) {
    window.localStorage.setItem(createKey(key), value);
  },
  getItem (key) {
    return window.localStorage.getItem(createKey(key));
  }
};
