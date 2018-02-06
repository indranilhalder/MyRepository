import React from "react";
import styles from "./OfferCard.css";
import Counter from "./TimerCounter.js";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";

export default class OfferCard extends React.Component {
  handleClick(val) {
    if (this.props.onClick()) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headingText}>
          {this.props.heading}
          <div className={styles.iconHolder}>
            <span className={styles.timer}>
              {" "}
              <Counter endTime={this.props.endTime} />
            </span>
            <div className={styles.timerHolder}>
              <Icon image={this.props.imageUrl} size={this.props.size} />
            </div>
          </div>
        </div>
        <div className={styles.headingDescription}>
          {this.props.description}
          <span className={styles.text}>{this.props.couponCode}</span>
        </div>
        <div className={styles.description}>{this.props.descriptionData}</div>
        <div className={styles.button} onClick={() => this.handleClick()}>
          {this.props.buttonText}
        </div>
      </div>
    );
  }
}

OfferCard.propTypes = {
  image: PropTypes.string,
  heading: PropTypes.string,
  endTime: PropTypes.string,
  description: PropTypes.string,
  couponCode: PropTypes.string,
  descriptionData: PropTypes.string,
  buttonText: PropTypes.string,
  onClick: PropTypes.func
};
OfferCard.defaultProps = {
  size: 20
};
