import React from "react";
import { LOGIN_PATH } from "../../lib/constants";
import PropTypes from "prop-types";
import styles from "./LogoutButton.css";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class LogoutButton extends React.Component {
  logoutUser() {
    if (this.props.logout) {
      this.props.logout();
      this.props.history.push(`${LOGIN_PATH}`);
    }
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.button}>
          <UnderLinedButton
            size={this.props.size}
            fontFamily={this.props.fontFamily}
            color={this.props.color}
            label={this.props.label}
            onClick={() => this.logoutUser()}
          />
        </div>
      </div>
    );
  }
}
LogoutButton.propTypes = {
  label: PropTypes.string,
  logout: PropTypes.func
};
LogoutButton.defaultProps = {
  size: "14px",
  label: "Logout",
  color: "#ff1744",
  fontFamily: "regular"
};
