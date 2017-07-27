function handleResponse (response) {
  if (response.status >= 400) {
    const error = new Error(response.statusText || response.status);
    error.status = response.status;
    return Promise.reject(error);
  }

  return Promise.resolve(response);
}

export default function () {
  return fetch
    .apply(null, arguments)
    .then(handleResponse);
}
