import React from 'react';
import 'brace';
import AceEditor from 'react-ace';

import 'brace/mode/yaml';
import 'brace/mode/json';
import 'brace/theme/github';

export function ValidateButton (props) {
  return (<button
    type="submit"
    disabled={props.disabled}
    className={'dc-btn dc-btn--primary editor-input-form__button' + (props.disabled ? ' dc-btn--disabled' : '')}>
    VALIDATE
  </button>);
}

export function EditorInputForm (props) {
  const validateButtonIsDisabled = props.pending || props.editorError || !props.editorValue.trim();

  return (<form onSubmit={props.onSubmit} className="editor-input-form">
    <label className="dc-label editor-input-form__label">Paste in a Swagger schema and click</label>
    <ValidateButton disabled={validateButtonIsDisabled} />
    <Editor
      annotations={props.editorAnnotations}
      onChange={props.onInputValueChange}
      value={props.editorValue} />
    <div className="editor-input-form__bottom-button">
      <ValidateButton disabled={validateButtonIsDisabled} />
    </div>
  </form>);
}


export function Editor (props) {
  return (
    <div className="editor">
      <AceEditor
        className="editor__ace-editor"
        mode="yaml"
        theme="github"
        width="100%"
        annotations={props.annotations}
        showPrintMargin={false}
        value={props.value}
        onChange={props.onChange || function () {}}
        editorProps={{$blockScrolling: true}}
      />
    </div>
  );
}
