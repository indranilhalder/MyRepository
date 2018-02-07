import React from "react";
import styles from "./RatingHolder.css";
import HorizontalRating from "./HorizontalRating.js";
import PropTypes from "prop-types";
export default class RatingHolder extends React.Component {
  render() {
    if (this.props.ratingData) {
      let calculateWidth = this.props.ratingData
        .map(function(item) {
          return item.score;
        })
        .reduce(function(accumulator, currentValue) {
          return accumulator + currentValue;
        });
      return (
        <div className={styles.base}>
          {this.props.ratingData.map((data, i) => {
            return (
              <HorizontalRating
                score={data.score}
                ratingsNumber={data.ratingsNumber}
                width={data.score / calculateWidth * 100}
              />
            );
          })}
        </div>
      );
    } else {
      return <div />;
    }
  }
}
RatingHolder.propTypes = {
  rattingData: PropTypes.arrayOf(
    PropTypes.shape({
      score: PropTypes.score,
      ratingsNumber: PropTypes.string
    })
  )
};
