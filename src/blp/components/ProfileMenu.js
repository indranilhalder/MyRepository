import React from "react";
import PropTypes from "prop-types";
import styles from "./ProfileMenu.css";
import { Icon } from "xelpmoc-core";
export default class ProfileMenu extends React.Component {
  onSave(value) {
    if (this.props.onSave) {
      this.props.onSave(value);
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        onClick={value => this.onSave({ value: this.props.text })}
      >
        <div className={styles.iconeHolder}>
          <Icon image={this.props.image} size={25} />
        </div>
        <div className={styles.ProfileMenuText}>{this.props.text}</div>
      </div>
    );
  }
}

ProfileMenu.propTypes = {
  onSave: PropTypes.func,
  text: PropTypes.string
};
