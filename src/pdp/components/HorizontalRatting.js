import React from "react";
import styles from "./HorizontalRatting.css";
import PropTypes from "prop-types";
export default class HorizontalRatting extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.pointNumber}>{this.props.label}</div>
        <div className={styles.indexNumberWithIcon}>{this.props.header}</div>
        <div className={styles.defaultBar}>
          <div
            className={styles.filledBar}
            style={{ width: this.props.width }}
          />
        </div>
      </div>
    );
  }
}
HorizontalRatting.propTypes = {
  label: PropTypes.obj,
  header: PropTypes.obj,
  width: PropTypes.number
};
