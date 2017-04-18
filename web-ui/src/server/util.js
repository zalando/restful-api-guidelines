'use strict';

/**
 * Dummy function to transform object that contains
 * boolean values expressed as strings (eg. 'true' or 'false')
 * in real boolean values. Doesn't traverse the object tree.
 *
 * @param {Object} object
 * @return {Object}
 */
function stringToBool (object) {
  return Object.keys(object).reduce((acc, key) => {
    if (object[key] === 'true') {
      acc[key] = true;
    } else if (object[key] === 'false') {
      acc[key] = false;
    } else {
      acc[key] = object[key];
    }
    return acc;
  }, {});
}

module.exports = {
  stringToBool
};
