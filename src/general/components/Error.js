import React from "react";
import PropTypes from "prop-types";
import styles from "./Error.css";
export default class Error extends React.Component {
  render() {
    return this.props.show ? (
      <div className={styles.base}>{this.props.message} </div>
    ) : null;
  }
}

Error.PropTypes = {
  show: PropTypes.bool,
  message: PropTypes.string
};
