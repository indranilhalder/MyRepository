import React from "react";
import styles from "./RatingHolder.css";
import HorizontalRating from "./HorizontalRating.js";
import PropTypes from "prop-types";
export default class RatingHolder extends React.Component {
  render() {
    let calculateWidth = this.props.ratingData
      .map(function(item) {
        return item.score;
      })
      .reduce(function(a, b) {
        return a + b;
      });
    return (
      <div className={styles.base}>
        {this.props.ratingData.map((data, i) => {
          return (
            <HorizontalRating
              score={data.score}
              numberOfRatings={data.numberOfRatings}
              width={data.score / calculateWidth * 100}
            />
          );
        })}
      </div>
    );
  }
}
RatingHolder.propTypes = {
  rattingData: PropTypes.arrayOf(
    PropTypes.shape({
      score: PropTypes.score,
      numberOfRatings: PropTypes.string
    })
  )
};
