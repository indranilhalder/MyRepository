import React from "react";
import { HOME_ROUTER, SUCCESS } from "../../lib/constants";
import PropTypes from "prop-types";
import styles from "./LogoutButton.css";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class LogoutButton extends React.Component {
  async logoutUser() {
    if (this.props.logout) {
      const logoutResponse = await this.props.logout();
      if (logoutResponse.status === SUCCESS) {
        const generateCartIdForAnonymous = await this.props.generateCartIdForAnonymous();
        if (generateCartIdForAnonymous.status === SUCCESS) {
          this.props.history.push(`${HOME_ROUTER}`);
        }
      }
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
