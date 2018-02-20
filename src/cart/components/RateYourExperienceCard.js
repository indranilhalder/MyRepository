import React from "react";
import styles from "./RateYourExperienceCard.css";
import PropTypes from "prop-types";
import ExperienceRateGrid from "./ExperienceRateGrid.js";
import Button from "../../general/components/Button.js";

export default class RateyourExperienceCard extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.retingInnerBox}>
          <div className={styles.heading}>{this.props.heading}</div>
          <div className={styles.retingLabel}>{this.props.label}</div>
          <div className={styles.rating}>
            <ExperienceRateGrid />
          </div>
          <div className={styles.buttonHolder}>
            <Button
              type="primary"
              backgroundColor="#ff1744"
              height={40}
              label={this.props.buttonText}
              width={211}
              textStyle={{ color: "#FFF", fontSize: 50 }}
            />
          </div>
        </div>
      </div>
    );
  }
}
RateyourExperienceCard.propTypes = {
  heading: PropTypes.string,
  label: PropTypes.string,
  buttonText: PropTypes.string
};
