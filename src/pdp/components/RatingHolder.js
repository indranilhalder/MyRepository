import React from "react";
import styles from "./RatingHolder.css";
import HorizontalRating from "./HorizontalRating.js";
import PropTypes from "prop-types";
export default class RatingHolder extends React.Component {
  render() {
    let calculateWidth = this.props.rattingData
      .map(function(item) {
        return item.score;
      })
      .reduce(function(a, b) {
        return a + b;
      }, 0);
    return (
      <div className={styles.base}>
        {this.props.rattingData.map((data, i) => {
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
