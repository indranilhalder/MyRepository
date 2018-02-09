import React from "react";
import IconWithHeader from "./IconWithHeader";
import styles from "./DeliveryOption.css";
import PropTypes from "prop-types";
import CheckBox from "./CheckBox.js";

export default class DeliveryOption extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <IconWithHeader image={this.props.image} header={this.props.header}>
          <span className={styles.checkCircle}>
            <CheckBox selected={this.props.selected} />
          </span>
          <div className={styles.placeTime}>{this.props.deliveryText}</div>
        </IconWithHeader>
      </div>
    );
  }
}
