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
        <div className={styles.headingText}>
          {this.props.heading}
          {this.props.validTill && (
            <div className={styles.iconHolder}>
              <div className={styles.timer}>
                {" "}
                <TimerCounter endTime={this.props.endTime} />
              </div>
              <div className={styles.timerHolder}>
                <Icon image={ClockImage} size={this.props.size} />
              </div>
            </div>
          )}
        </div>
        <div
          className={styles.description}
          dangerouslySetInnerHTML={{
            __html: this.props.description
              .replace("<p>", "")
              .replace("</p>", "")
          }}
        />

        <div className={styles.button}>
          {this.props.buttonText}
          <UnderLinedButton
            color="#fff"
            label="More Offers"
            onClick={() => this.handleClick()}
          />
        </div>
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
