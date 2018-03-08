import React from "react";
import styles from "./CheckOutHeader.css";
import PropTypes from "prop-types";
export default class CheckOutHeader extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.confirm}>
          {this.props.confirmTitle}
          <div className={styles.circle}>{this.props.indexNumber}</div>
        </div>
      </div>
    );
  }
}

CheckOutHeader.propTypes = {
  indexNumber: PropTypes.string,
  confirmTitle: PropTypes.string
};
