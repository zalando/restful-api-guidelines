import React from 'react';

export default function UserInfo(props){
    return (
      <div className="user-info">
        <div className="dc-row">
          <div className="dc-column--shrink">
            <i className="dc-icon dc-icon--user user-info__icon"><span>{props.username}</span></i>
          </div>
          <div className="dc-column">
            <a href onClick={props.handleLogout.bind(this)} className="dc-link dc-link--destroy user-info__logout">Logout</a>
          </div>
        </div>
      </div>
    )
}
