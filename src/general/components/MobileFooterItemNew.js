import React from "react";
import PropTypes from "prop-types";
import styles from "./MobileFooterItemNew.css";
import { Icon } from "xelpmoc-core";
export default class MobileFooterItemNew extends React.Component {
  onSelect() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let iconClass = styles.iconActive;
    let iconClassActive = styles.iconHidden;
    let textColor = styles.footerText;
    let textColorActive = styles.textSelect;
    if (this.props.selected) {
      iconClass = styles.iconHidden;
      iconClassActive = styles.iconActive;
      textColor = textColorActive;
    }
    return (
      <div className={styles.base} onClick={() => this.onSelect()}>
        <div className={styles.footerIconeHolder}>
          <div className={iconClass}>
            <Icon image={this.props.basicIcon} size={25} />
          </div>
          <div className={iconClassActive}>
            <Icon image={this.props.activeIcon} size={25} />
          </div>
        </div>
        <div className={textColor} onClick={this.props.onClick}>
          {this.props.text}
        </div>
      </div>
    );
  }
}

MobileFooterItemNew.propTypes = {
  activeIcon: PropTypes.string,
  basicIcon: PropTypes.string,
  text: PropTypes.string,
  onSelect: PropTypes.func
};
