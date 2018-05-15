import React from "react";
import styles from "./AccountUsefulLink.css";
import arrowIcon from "../../general/components/img/down-arrow.svg";
import Icon from "../../xelpmoc-core/Icon";
export default class AccountUsefulLink extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.onClick()}>
        <div className={styles.iconHolder}>
          <Icon image={arrowIcon} size={10} />
        </div>
        {this.props.children}
      </div>
    );
  }
}
