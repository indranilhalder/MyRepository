import React from "react";
import styles from "./Loader.css";
import PropTypes from "prop-types";
import SecondaryLoader from "./SecondaryLoader";

export default class Loader extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.spinner}>
          <SecondaryLoader />
        </div>
      </div>
    );
  }
}
