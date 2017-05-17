import React from 'react';
import {Violations} from './violations.jsx';
import {ViolationsResult} from '../components/violations.jsx';
import {URLInputForm} from '../components/url.jsx';


export class URL extends Violations {
  constructor (props) {
    super(props);
    this.state.inputValue = this.Storage.getItem('url-value') || '';
  }

  handleOnInputValueChange (event) {
    this.Storage.setItem('url-value', event.target.value);
    super.handleOnInputValueChange(event);
  }

  render () {
    return (<div>
      <URLInputForm
        inputValue={this.state.inputValue}
        onSubmit={this.handleFormSubmit.bind(this)}
        onInputValueChange={this.handleOnInputValueChange.bind(this)}
        pending={this.state.pending} />

      <ViolationsResult
        pending={this.state.pending}
        complete={this.state.ajaxComplete}
        errorMsgText={this.state.error}
        violations={this.state.violations}
        successMsgTitle={this.state.successMsgTitle}
        successMsgText={this.state.successMsgText} />
    </div>);
  }
}

