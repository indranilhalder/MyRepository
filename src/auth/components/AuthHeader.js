import React from "react";
import { Icon } from "xelpmoc-core";
import tataLogo from "./img/tataLogo.png";
import { default as styles } from "./AuthHeader.css";
export default class AuthHeader extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.center}>
          <div className={styles.logo}>
            <Icon image={tataLogo} size={70} />
          </div>
        </div>
        {this.props.children}
      </div>
    );
  }
}
