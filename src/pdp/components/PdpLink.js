import React from "react";
import arrowIcon from "../../general/components/img/arrow.svg";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import styles from "./PdpLink.css";
export default class PdpLink extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.onClick()}>
        <div className={styles.linkArrow}>
          <Icon image={arrowIcon} size={20} />
        </div>
        {this.props.children}
      </div>
    );
  }
}
PdpLink.propTypes = {
  onClick: PropTypes.func
};
