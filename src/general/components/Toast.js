import React from "react";
import styles from "./Toast.css";
import PropTypes from "prop-types";
import { TOAST_DELAY } from "../toast.actions";
import delay from "lodash.delay";
export default class Toast extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.textHolder}>{this.props.data}</div>
      </div>
    );
  }
}
Toast.propTypes = {
  data: PropTypes.string
};
