import React from "react";
import styles from "./ReviewList.css";
import ReviewPage from "./ReviewPage";
import PropTypes from "prop-types";
export default class ReviewList extends React.Component {
  render() {
    console.log("REVIEW LIST");
    console.log(this.props.reviewList);
    return (
      <div className={styles.base}>
        {this.props.reviewList &&
          this.props.reviewList.map((data, i) => {
            return (
              <ReviewPage
                rating={data.rating}
                heading={data.headline}
                text={data.comment}
                date={data.date}
              />
            );
          })}
      </div>
    );
  }
}

ReviewList.propTypes = {
  reviewList: PropTypes.arrayOf(
    PropTypes.shape({
      rating: PropTypes.String,
      heading: PropTypes.string,
      text: PropTypes.string,
      label: PropTypes.String
    })
  )
};
