import React from 'react';

export class Msg extends React.Component {
  constructor (props) { super(props); }

  render () {
    const type = this.props.type || 'info';
    return (
      <div className={'dc-msg dc-msg--' + type }>
        <div className="dc-msg__inner">

          <div className="dc-msg__icon-frame">
            <i className={'dc-icon dc-msg__icon dc-icon--' + type}></i>
          </div>

          <div className="dc-msg__bd">
            <h1 className="dc-msg__title">{this.props.title}</h1>
            <p className="dc-msg__text">
              {this.props.text}
            </p>
          </div>
          { this.props.closeButton !== false ?
            <div className="dc-msg__close" onClick={this.props.onCloseButtonClick} >
              <i className="dc-icon dc-icon--close dc-msg__close__icon"></i>
            </div> : ''}
        </div>
      </div>
    );
  }
}
