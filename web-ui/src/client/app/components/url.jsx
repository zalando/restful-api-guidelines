import React from 'react';

export function URLInputForm (props) {
  const validateButtonIsDisabled = props.pending || !props.inputValue.trim();

  return (<form onSubmit={props.onSubmit} className="url-input-form">
    <label className="dc-label">Enter full path to your swagger file</label>
    <input className="dc-input dc-input--block"
           value={props.inputValue}
           onChange={props.onInputValueChange}
           type="url"
           name="path"
           placeholder="e.g https://raw.githubusercontent.com/OAI/OpenAPI-Specification/master/examples/v2.0/json/petstore.json"
           required pattern="https?://.+" />
    <button
      type="submit"
      disabled={props.pending}
      className={'dc-btn dc-btn--primary  ' + (validateButtonIsDisabled ? 'dc-btn--disabled' : '')}>
      VALIDATE
    </button>
  </form>);
}



