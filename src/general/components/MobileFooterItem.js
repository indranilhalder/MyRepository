import React from "react";
import PropTypes from "prop-types";
import styles from "./MobileFooterItem.css";
import { Icon } from "xelpmoc-core";
export default class MobileFooterItem extends React.Component {
  handleSelect() {
    console.log("MOBILE FOOTER ITEM SELECT");
    if (this.props.onSelect) {
      this.props.onSelect();
    }
  }
  render() {
    console.log("RENDER");
    console.log(this.props.selected);
    return (
      <div className={styles.base} onClick={() => this.handleSelect()}>
        <div
          className={
            this.props.selected !== this.props.value
              ? styles.layer
              : styles.layerHidden
          }
        >
          <div className={styles.iconHolder}>
            <Icon image={this.props.basicIcon} size={25} />
          </div>
          <div className={styles.footerText}>{this.props.text}</div>
        </div>
        <div
          className={
            this.props.selected === this.props.value
              ? styles.layer
              : styles.layerHidden
          }
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
  onSelect: PropTypes.func,
  selected: PropTypes.string
};
