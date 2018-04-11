import React from "react";
import styles from "./OfferCard.css";
import TimerCounter from "../../general/components/TimerCounter.js";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import { Icon } from "xelpmoc-core";
import ClockImage from "./img/clockWhite.svg";
import PropTypes from "prop-types";

export default class OfferCard extends React.Component {
  handleClick(val) {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.heading && (
          <div className={styles.headingText}>{this.props.heading}</div>
        )}

        {this.props.isDescription && (
          <div>
            <div
              className={styles.description}
              dangerouslySetInnerHTML={{
                __html: this.props.description
                  .replace("<p>", "")
                  .replace("</p>", "")
              }}
            />
          </div>
        )}
        {this.props.messageId && (
          <div className={styles.descriptionText}>{this.props.messageId}</div>
        )}
      </div>
    );
  }
}

OfferCard.propTypes = {
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
