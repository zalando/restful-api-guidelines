import React, {Component} from 'react';
import {ViolationsResult} from './violations.jsx';

export class Violations extends Component {
  constructor (props) {
    super(props);

    this.Storage = this.props.route.Storage;
    this.getApiViolations = this.props.route.getApiViolations;

    this.state = {
      error: null,
      pending: false,
      ajaxComplete: false,
      inputValue: '',
      mode: 'URL',
      violations: [],
      violationsCount: {
        could: 0,
        hint: 0,
        must: 0,
        should: 0
      },
      successMsgTitle: 'Good Job!',
      successMsgText: 'No violations found in the analyzed schema.'
    };
  }

  handleFormSubmit (event) {
    event.preventDefault();

    this.setState({ error: null, pending: true, ajaxComplete: false });

    return this.getApiViolations(this.state.inputValue)
      .then((response) => {
        this.setState({
          pending: false,
          ajaxComplete: true,
          violations: response.violations,
          violationsCount: response.violations_count
        });
      })
      .catch((error) => {
        console.error(error); // eslint-disable-line no-console
        this.setState({
          pending: false,
          ajaxComplete: true,
          error: 'Ooops something went wrong!',
          violations: [],
          violationsCount: {
            could: 0,
            hint: 0,
            must: 0,
            should: 0
          }
        });
        return Promise.reject(error);
      });
  }

  handleOnInputValueChange (event) {
    this.setState({inputValue: event.target.value});
  }

  render () {
    return (<ViolationsResult
      pending={this.state.pending}
      complete={this.state.ajaxComplete}
      errorMsgText={this.state.error}
      violations={this.state.violations}
      successMsgTitle={this.state.successMsgTitle}
      successMsgText={this.state.successMsgText} />
    );
  }
}
