export function debug () {
  if (typeof window !== 'undefined' && window.env && window.env.DEBUG) {
    /* eslint-disable no-console */
    typeof console.debug === 'function' ? console.debug.apply(null, arguments) : console.log.apply(null, arguments);
  }
}
