import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./Input.css";
import { Input as CoreInput, CircleButton } from "xelpmoc-core";

export default class Input extends React.Component {
  render() {
    return (
      <div className={styles.container}>
        <CoreInput {...this.props} styles={styles} />
      </div>
    );
  }
}
