import React from "react";
import styles from "./HeaderWithIcon.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";

export default class HeaderWithIcon extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.iconHolder}>
          <Icon image={this.props.image} />
        </div>
        <div className={styles.textHolder}>{this.props.header}</div>
        {this.props.children}
      </div>
    );
  }
}
HeaderWithIcon.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string
};
