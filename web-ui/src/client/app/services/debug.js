export function debug () {
  if (window && window.env && window.env.DEBUG) {
    /* eslint-disable no-console */
    console.debug ? console.debug.apply(null, arguments) : console.log(null, arguments);
  }
}
