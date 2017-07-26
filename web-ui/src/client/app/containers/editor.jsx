import React from 'react';
import yaml from 'js-yaml';
import {Msg} from '../components/dress-code.jsx';
import {Violations} from './violations.jsx';
import {ViolationsResult} from '../components/violations.jsx';
import {EditorInputForm} from '../components/editor.jsx';


export const editorErrorToAnnotations = (error) => {
  if (!error || !error.mark) { return []; }
  return [
    { row: error.mark.line, column: error.mark.column, type: 'error', text: error.reason }
  ];
};

export class Editor extends Violations {

  constructor (props) {
    super(props);
    this.state.editorValue = this.Storage.getItem('editor-value') || '';
  }

  componentDidMount () {
    this.updateInputValue(this.state.editorValue);
  }

  updateInputValue (value) {
    try {
      const inputValue = yaml.load(value);
      this.setState({
        inputValue: inputValue,
        editorError: null,
        editorAnnotations: []
      });
    } catch (e) {
      console.warn(e); // eslint-disable-line no-console
      this.setState({
        inputValue: null,
        editorError: e,
        editorAnnotations: editorErrorToAnnotations(e)
      });
    }
  }

  handleOnInputValueChange (value) {
    this.Storage.setItem('editor-value', value);

    this.setState({
      editorValue: value
    });
    this.updateInputValue(value);
  }

  render () {
    return (
      <div className="dc-row">
        <div className="dc-column dc-column--small-12 dc-column--large-7">
          <div className="dc-column__contents">
            <EditorInputForm
              editorError={this.state.editorError}
              editorAnnotations={this.state.editorAnnotations}
              editorValue={this.state.editorValue}
              onSubmit={this.handleFormSubmit.bind(this)}
              onInputValueChange={this.handleOnInputValueChange.bind(this)}
              pending={this.state.pending} />
          </div>
        </div>
        <div className="dc-column dc-column--small-12 dc-column--large-5">
          <div className="dc-column__contents">
            { this.state.editorError ? <Msg type="error" title="ERROR" text={this.state.editorError.message} closeButton={false} /> : null }
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
