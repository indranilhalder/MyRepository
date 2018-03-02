import React from "react";
import PropTypes from "prop-types";
import styles from "./MobileFooterItem.css";
import { Icon } from "xelpmoc-core";
export default class MobileFooterItem extends React.Component {
  onSelect() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.onSelect()}>
        <div
          className={!this.props.selected ? styles.layer : styles.layerHidden}
        >
          <div className={styles.iconHolder}>
            <Icon image={this.props.basicIcon} size={25} />
          </div>
          <div className={styles.footerText}>{this.props.text}</div>
        </div>
        <div
          className={this.props.selected ? styles.layer : styles.layerHidden}
        >
          <div className={styles.iconHolder}>
            <Icon image={this.props.activeIcon} size={25} />
          </div>
          <div className={styles.textSelect}>{this.props.text}</div>
        </div>
      </div>
    );
  }
}

MobileFooterItem.propTypes = {
  activeIcon: PropTypes.string,
  basicIcon: PropTypes.string,
  text: PropTypes.string,
  onSelect: PropTypes.func
};
