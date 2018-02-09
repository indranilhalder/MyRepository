import React from "react";
import styles from "./ReviewList.css";
import ReviewPage from "./ReviewPage";
import PropTypes from "prop-types";
export default class ReviewList extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.reviewList &&
          this.props.reviewList.map((data, i) => {
            return (
              <ReviewPage
                rating={data.averageRating}
                heading={data.productName}
                text={data.productDescription}
                label={data.label}
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
