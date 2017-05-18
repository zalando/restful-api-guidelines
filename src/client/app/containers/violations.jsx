import React, {Component} from 'react';
import {Link} from 'react-router';
import {ViolationsResult} from '../components/violations.jsx';


export function ViolationsTab (props) {

  return (<div className="dc-container">
    <h4 className="dc-h4">
      Check if your&nbsp;
      <a href="http://swagger.io/specification/" target="_blank" className="dc-link">SWAGGER Schema</a> conforms to&nbsp;
      <a href="http://zalando.github.io/restful-api-guidelines/" target="_balnk" className="dc-link">Zalando's REST API Guidelines</a>
    </h4>

    <div className="tab-navigation">
      <div className="tab-navigation-group">
        <Link to="/" className="dc-link tab-navigation__link" activeClassName="tab-navigation__link--active">BY URL</Link>
        <Link to="/editor" className="dc-link tab-navigation__link" activeClassName="tab-navigation__link--active">EDITOR</Link>
        <Link to="/rules" className="dc-link tab-navigation__link" activeClassName="tab-navigation__link--active">RULES</Link>
      </div>
    </div>
    <div className="tab-contents">
      {/* Mount child routes*/}
      {props.children}
    </div>
  </div>);
}

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
          error: error.detail || Violations.DEFAULT_ERROR_MESSAGE,
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

Violations.DEFAULT_ERROR_MESSAGE = 'Ooops something went wrong!';
