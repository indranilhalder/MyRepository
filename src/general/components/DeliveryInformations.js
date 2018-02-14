import React from "react";
import IconWithHeader from "./IconWithHeader";
import styles from "./DeliveryInformations.css";
import UnderLinedButton from "./UnderLinedButton";
import PropTypes from "prop-types";
import ExpressImage from "./img/expressDelivery.svg";
import HomeImage from "./img/homeDelivery.svg";
import CollectImage from "./img/collect.svg";
const EXPRESS = "express-delivery";
const COLLECT = "click-and-collect";

export default class DeliveryInformation extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let iconImage = HomeImage;
    if (this.props.type === EXPRESS) {
      iconImage = ExpressImage;
    } else if (this.props.type === COLLECT) {
      iconImage = CollectImage;
    }
    return (
      <div className={styles.base}>
        <IconWithHeader image={iconImage} header={this.props.header}>
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
        </IconWithHeader>
      </div>
    );
  }
}
DeliveryInformation.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string,
  heading: PropTypes.string,
  type: PropTypes.string,
  color: PropTypes.string,
  deliveryOptions: PropTypes.string,
  onClick: PropTypes.func
};
