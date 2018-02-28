import React from "react";
import PropTypes from "prop-types";
import styles from "./ProfileMenu.css";
import { Icon } from "xelpmoc-core";
export default class ProfileMenu extends React.Component {
  onSelect() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.onSelect()}>
        <div className={styles.iconeHolder}>
          <Icon image={this.props.image} size={25} />
        </div>
        <div className={styles.ProfileMenuText}>{this.props.text}</div>
      </div>
    );
  }
}

ProfileMenu.propTypes = {
  onClick: PropTypes.func,
  text: PropTypes.string
};
