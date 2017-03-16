import React from 'react';

export default class Form extends React.Component {
  handleFormSubmit(event){
    event.preventDefault(); // Let's stop this event.
    console.debug('call api');
  }

  render () {
    return (
      <form onSubmit={this.handleFormSubmit}>
        <label className="dc-label">Enter full path to your swagger file from repo </label>
        <input className="dc-input dc-input--block" type="url" name="path" placeholder="e.g https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/json/petstore.json" required pattern="https?://.+" />
        <button type="submit" className="dc-btn dc-btn--primary">Submit</button>
      </form>
    )
  }
}

