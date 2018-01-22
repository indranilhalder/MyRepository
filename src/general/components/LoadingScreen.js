import React from "react";
import PropTypes from "prop-types";
import MDSpinner from "react-md-spinner";

import styles from "./LoadingScreen.css";

export default class LoadingScreen extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.spinner}>
          <MDSpinner />
        </div>
        <div className={styles.headerText}>{this.props.header}</div>
        <div className={styles.bodyText}>{this.props.body}</div>
      </div>
    );
  }
}

LoadingScreen.propTypes = {
  body: PropTypes.string,
  header: PropTypes.string
};
