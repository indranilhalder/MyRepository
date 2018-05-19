import React from "react";
import PropTypes from "prop-types";
import StarRating from "../../general/components/StarRating.js";
import PdpLink from "./PdpLink";
import styles from "./RatingAndTextLink.css";
const NO_REVIEW_TEXT = "There are no reviews yet";
export default class RatingAndTextLink extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <PdpLink onClick={() => this.onClick()}>
        {this.props.numberOfReview !== 0 && (
          <div
            itemProp="aggregateRating"
            itemScope
            itemType="http://schema.org/AggregateRating"
            className={styles.base}
          >
            <div className={styles.ratingsHolder}>
              <StarRating averageRating={this.props.averageRating} />
            </div>

            <div className={styles.textHolder}>
              <span itemProp="ratingValue">
                {this.props.averageRating
                  ? Math.floor(this.props.averageRating)
                  : ""}
              </span>{" "}
              based on
              <span itemProp="reviewCount"> {this.props.numberOfReview}</span>
              reviews
            </div>
          </div>
        )}
        {this.props.numberOfReview === 0 && (
          <div className={styles.base}>
            <div className={styles.ratingsHolder}>
              <StarRating averageRating={this.props.averageRating} />
            </div>
            <div className={styles.textHolder}>{NO_REVIEW_TEXT}</div>
          </div>
        )}
      </PdpLink>
    );
  }
}
RatingAndTextLink.propTypes = {
  averageRating: PropTypes.number,
  onClick: PropTypes.func,
  numberOfReview: PropTypes.number
};
