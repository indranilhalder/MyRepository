import React from "react";
import LocationCard from "./LocationIcon";
import styles from "./IconWithHeader.css";
import UnderLinedButton from "./UnderLinedButton";
import PropTypes from "prop-types";

export default class IconWithHeader extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <LocationCard image={this.props.image} text={this.props.text}>
          <div className={styles.headingHolder}>{this.props.heading}</div>
          <div className={styles.headingHolder}>
            {this.props.deliverText}
            <span className={styles.text}>{this.props.textHeading}</span>
          </div>
          <div className={styles.headingHolder}>
            <span>{this.props.deliveryOptions}</span>
            <span className={styles.buttonHolder}>
              <UnderLinedButton
                label="1011"
                color={this.props.color}
                onClick={() => {
                  this.handleClick();
                }}
              />
            </span>
          </div>
        </LocationCard>
        <LocationCard image={this.props.image} text={this.props.text}>
          <div className={styles.headerHolder}>
            <div className={styles.buttonHolder}>
              <UnderLinedButton
                label="check for pickup options"
                color={this.props.color}
                onClick={() => {
                  this.handleClick();
                }}
              />
            </div>
          </div>
        </LocationCard>
      </div>
    );
  }
}
IconWithHeader.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string,
  heading: PropTypes.string,
  color: PropTypes.string,
  deliveryOptions: PropTypes.string,
  onClick: PropTypes.func
};
