import React from 'react';

export default function UserInfo(props){
    const onClickLogout = (event) => {
      console.log(props);
      event.preventDefault();
      if(typeof props.onLogout === 'function') {
        props.onLogout();
      }
    };
    return (
      <div className="user-info">
        <div className="dc-row">
          <div className="dc-column--shrink">
            <i className="dc-icon dc-icon--user user-info__icon"><span>{props.username}</span></i>
          </div>
          <div className="dc-column">
            <span onClick={onClickLogout}
               className="dc-link dc-link--destroy user-info__logout">logout</span>
          </div>
        </div>
      </div>
    )
}
