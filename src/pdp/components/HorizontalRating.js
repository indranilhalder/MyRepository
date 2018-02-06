import React from "react";
import styles from "./HorizontalRating.css";
import PropTypes from "prop-types";
export default class HorizontalRating extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.pointNumber}>{this.props.score}</div>
        <div className={styles.indexNumberWithIcon}>
          <div className={styles.ratingIcon} />
          {this.props.ratingsNumber}
        </div>
        <div className={styles.defaultBar}>
          <div
            className={styles.filledBar}
            style={{ width: this.props.width }}
          />
        </div>
      </div>
    );
  }
}
HorizontalRating.propTypes = {
  score: PropTypes.number,
  ratingsNumber: PropTypes.string,
  width: PropTypes.number
};
