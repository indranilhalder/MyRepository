import React from "react";
import styles from "./CenterAdd.css";
import PropTypes from "prop-types";

export default class CenterAdd extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.textHolder}>{this.props.text}</div>
      </div>
    );
  }
}

CenterAdd.propTypes = {
  text: PropTypes.string
};
