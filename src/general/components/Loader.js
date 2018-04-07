import React from "react";
import styles from "./Loader.css";
import PropTypes from "prop-types";
import MDSpinner from "react-md-spinner";

export default class OrderCard extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.spinner}>
          <MDSpinner />
        </div>
      </div>
    );
  }
}
