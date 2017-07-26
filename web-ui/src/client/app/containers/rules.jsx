import React, {Component} from 'react';
import {Link} from 'react-router';
import {RulesTab} from '../components/rules.jsx';


export class Rules extends Component {

  constructor (props) {
    super(props);
    this.state = {
      error: null,
      pending: false,
      ajaxComplete: false,
      filter: null,
      rules: []
    };
  }

  componentDidMount () {
    this.state.filter = this.parseFilterValue(this.props.location.query);
    this.fetchRules();
  }

  componentWillReceiveProps (nextProps) {
    if (nextProps.location.query['is_active'] !== this.props.location.query['is_active']) {
      this.setState({filter: this.parseFilterValue(nextProps.location.query)});
    }
  }

  componentDidUpdate (prevProps, prevState) {
    if (prevState.filter !== this.state.filter) {
      this.fetchRules();
    }
  }

  fetchRules () {
    this.setState({ error: null, pending: true, ajaxComplete: false });
    const {getSupportedRules} = this.props.route;
    return getSupportedRules(this.state.filter)
      .then((response) => {
        this.setState({
          error: null,
          pending: false,
          ajaxComplete: true,
          rules: response.rules
        });
      })
      .catch((error) => {
        console.error(error); // eslint-disable-line no-console

        this.setState({
          pending: false,
          ajaxComplete: true,
          error: error.detail || Rules.DEFAULT_ERROR_MESSAGE,
          rules: [],
        });
        return Promise.reject(error);
      });
  }

  parseFilterValue (query) {
    if (query && query['is_active'] !== undefined) {
      return { is_active: query['is_active'] === 'true' };
    }
    return null;
  }

  render () {
    return (
      <div>
        <div className="dc-row">
          <div className="dc-column dc-column--small-12 dc-column--large-7">
            <label className="dc-label filter-navigation__label">Show only</label>
            <div className="dc-btn-group dc-btn-group--in-row">
              <Link to={{ pathname: '/rules', query: {is_active: true}}} className="dc-btn dc-btn--small dc-btn--in-btn-group" activeClassName="dc-btn--primary">Active</Link>
              <Link to={{ pathname: '/rules', query: {is_active: false}}} className="dc-btn dc-btn--small dc-btn--in-btn-group" activeClassName="dc-btn--primary">Inactive</Link>
            </div>
          </div>
        </div>
        <div className="dc-row">
          <div className="dc-column dc-column--small-12 dc-column--large-7">
            <div className="dc-column__contents">
              <RulesTab error={this.state.error} rules={this.state.rules}></RulesTab>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

Rules.DEFAULT_ERROR_MESSAGE = 'Ooops not able to load supported rules!';
