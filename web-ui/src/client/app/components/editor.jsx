import React from 'react';
import brace from 'brace';
import AceEditor from 'react-ace';

import 'brace/mode/yaml';
import 'brace/mode/json';
import 'brace/theme/github';


export function EditorInputForm (props) {
  return (<form onSubmit={props.onSubmit} className="editor-input-form">
    <label className="dc-label editor-input-form__label">Paste in a Swagger schema and click</label>
    <button
      type="submit"
      disabled={props.pending}
      className={"dc-btn dc-btn--primary editor-input-form__button" + (props.pending ? "dc-btn--disabled" : "")}>
      VALIDATE
    </button>
    <Editor
      onChange={props.onInputValueChange}
      value={props.editorValue} />
    <div className="editor-input-form__bottom-button">
      <button
        type="submit"
        disabled={props.pending}
        className={"dc-btn dc-btn--primary editor-input-form__button" + (props.pending ? "dc-btn--disabled" : "")}>
        VALIDATE
      </button>
    </div>
  </form>)
}


export function Editor(props) {
  return (
    <div className="editor">
      <AceEditor
        className="editor__ace-editor"
        mode="yaml"
        theme="github"
        width="100%"
        value={props.value}
        onChange={props.onChange || function() {}}
        editorProps={{$blockScrolling: true}}
      />
    </div>
   )
}
