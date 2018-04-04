import React from "react";
import { HOME_ROUTER } from "../../lib/constants";
import PropTypes from "prop-types";
import styles from "./LogoutButton.css";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class LogoutButton extends React.Component {
  logoutUser() {
    if (this.props.logout) {
      this.props.logout();
      this.props.history.push(`${HOME_ROUTER}`);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <UnderLinedButton
          size={this.props.size}
          fontFamily={this.props.fontFamily}
          color={this.props.color}
          label={this.props.label}
          onClick={() => this.logoutUser()}
        />
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
