import React from 'react';
import yaml from 'js-yaml';
import {Violations} from './violations.jsx';
import {ViolationsResult} from '../components/violations.jsx';
import {EditorInputForm} from '../components/editor.jsx';

export class Editor extends Violations {

  constructor (props) {
    super(props);
    this.state.editorValue = this.Storage.getItem('editor-value') || '';
    this.state.inputValue = yaml.load(this.state.editorValue);
  }

  handleOnInputValueChange (value) {
    this.Storage.setItem('editor-value', value);

    this.setState({
      inputValue: yaml.load(value),
      editorValue: value
    });
  }

  render () {
    return (
      <div className="dc-row">
        <div className="dc-column dc-column--small-12 dc-column--large-7">
          <div className="dc-column__contents">
            <EditorInputForm
              editorValue={this.state.editorValue}
              onSubmit={this.handleFormSubmit.bind(this)}
              onInputValueChange={this.handleOnInputValueChange.bind(this)}
              pending={this.state.pending} />
          </div>
        </div>
        <div className="dc-column dc-column--small-12 dc-column--large-5">
          <div className="dc-column__contents">
            <ViolationsResult
              pending={this.state.pending}
              complete={this.state.ajaxComplete}
              errorMsgText={this.state.error}
              violations={this.state.violations}
              successMsgTitle={this.state.successMsgTitle}
              successMsgText={this.state.successMsgText} />
          </div>
        </div>
      </div>
    );
  }
}
