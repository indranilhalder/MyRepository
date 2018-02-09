import React from "react";
import styles from "./IconWithHeader.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";

export default class IconWithHeader extends React.Component {
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
IconWithHeader.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string
};
