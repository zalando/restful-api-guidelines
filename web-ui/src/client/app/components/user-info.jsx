import React from 'react';

export default function UserInfo (props){

  return (
      <div className="user-info">
        <div className="dc-row">
          <div className="dc-column--shrink">
            <i className="dc-icon dc-icon--user user-info__icon"><span>{props.username}</span></i>
          </div>
          <div className="dc-column">
            { props.authenticated ?
              <span onClick={props.onLogout}
                 className="dc-link dc-link--destroy user-info__logout">
                logout
              </span> :
              <span onClick={props.onLogin} className="dc-link dc-link--success user-info__login">
                login
              </span>
            }
          </div>
        </div>
      </div>
  );
}
