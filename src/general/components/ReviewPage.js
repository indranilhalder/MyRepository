import React from "react";
import styles from "./ReviewPage.css";
import propTypes from "prop-types";
import StarRating from "./StarRating.js";
export default class ReviewPage extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.reviewHolder}>
          <div className={styles.starHolder}>
            <StarRating />
          </div>

          {this.props.heading && (
            <div className={styles.heading}>{this.props.heading}</div>
          )}

          {this.props.text && (
            <div className={styles.text}>{this.props.text}</div>
          )}

          {this.props.label && (
            <div className={styles.dateTimeBox}>{this.props.label}</div>
          )}
        </div>
      </div>
    );
  }
}
ReviewPage.propTypes = {
  text: propTypes.string,
  label: propTypes.string,
  heading: propTypes.string
};
