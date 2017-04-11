import React from 'react';
import {ViolationsResult, AbstractViolationsContainer} from './violations.jsx';
import RestService from '../services/rest.js';
import {storage} from '../services/storage.js';

export function URLInputForm (props) {
  return (<form onSubmit={props.onSubmit} className="url-input-form">
    <label className="dc-label">Enter full path to your swagger file</label>
    <input className="dc-input dc-input--block"
           value={props.inputValue}
           onChange={props.onInputValueChange}
           type="url"
           name="path"
           placeholder="e.g https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/json/petstore.json"
           required pattern="https?://.+" />
    <button
      type="submit"
      disabled={props.pending}
      className={"dc-btn dc-btn--primary  " + (props.pending ? "dc-btn--disabled" : "")}>
      VALIDATE
    </button>
  </form>)
}



