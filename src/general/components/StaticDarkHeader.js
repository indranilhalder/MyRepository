import React from "react";
import PropTypes from "prop-types";
import styles from "./StaticDarkHeader.css";
export default class StaticDarkHeader extends React.Component {
  render() {
    return <div className={styles.base}>{this.props.text}</div>;
  }
}

StaticDarkHeader.propTypes = {
  text: PropTypes.string
};
