import React from "react";
import HeaderWithIcon from "./HeaderWithIcon";
import styles from "./DeliveryInformation.css";
import UnderLinedButton from "./UnderLinedButton";
import PropTypes from "prop-types";

export default class DeliveryInformation extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <HeaderWithIcon image={this.props.image} header={this.props.header}>
          <div className={styles.placeTime}>{this.props.placedTime}</div>
          <div className={styles.placeTime}>
            {this.props.deliverText}
            <span className={styles.text}>{this.props.textHeading}</span>
          </div>
          <div className={styles.placeTime}>
            <span>{this.props.deliveryOptions}</span>
            <span className={styles.buttonHolder}>
              <UnderLinedButton
                label={this.props.label}
                onClick={() => {
                  this.handleClick();
                }}
              />
            </span>
          </div>
        </HeaderWithIcon>
      </div>
    );
  }
}
DeliveryInformation.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string,
  heading: PropTypes.string,
  color: PropTypes.string,
  deliveryOptions: PropTypes.string,
  onClick: PropTypes.func
};
