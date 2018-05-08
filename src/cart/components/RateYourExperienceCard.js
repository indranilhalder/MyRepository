import React from "react";
import styles from "./RateYourExperienceCard.css";
import PropTypes from "prop-types";
import ExperienceRateGrid from "./ExperienceRateGrid.js";
import Button from "../../general/components/Button.js";

export default class RateyourExperienceCard extends React.Component {
  continueShopping() {
    if (this.props.continueShopping) {
      this.props.continueShopping();
    }
  }

  captureOrderExperience = rating => {
    this.props.captureOrderExperience(rating);
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.retingInnerBox}>
          <div className={styles.heading}>{this.props.heading}</div>
          <div className={styles.retingLabel}>{this.props.label}</div>
          <div className={styles.rating}>
            <ExperienceRateGrid
              onSelect={val => this.captureOrderExperience(val[0])}
            />
          </div>
          {this.props.isContinue && (
            <div className={styles.buttonHolder}>
              <Button
                type="primary"
                backgroundColor="#ff1744"
                height={40}
                label={this.props.buttonText}
                width={211}
                textStyle={{ color: "#FFF", fontSize: 50 }}
                onClick={() => this.continueShopping()}
              />
            </div>
          )}
        </div>
      </div>
    );
  }
}
RateyourExperienceCard.propTypes = {
  heading: PropTypes.string,
  label: PropTypes.string,
  buttonText: PropTypes.string,
  onCheckout: PropTypes.func,
  isContinue: PropTypes.bool
};

RateyourExperienceCard.defaultProps = {
  heading: "Rate your experience",
  label:
    "We'd love to know your feedback. Tell us, did you enjoy shopping on Tata CLiQ? ",
  buttonText: "Continue shopping",
  isContinue: false
};
