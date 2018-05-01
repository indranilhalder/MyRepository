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
        <div
          itemprop="aggregateRating"
          itemscope
          itemtype="http://schema.org/AggregateRating"
          className={styles.base}
        >
          <div className={styles.ratingsHolder}>
            <StarRating averageRating={this.props.averageRating} />
          </div>
          {this.props.numberOfReview !== 0 && (
            <div className={styles.textHolder}>
              <span itemprop="reviewCount">{this.props.numberOfReview}</span>
              reviews for this product
            </div>
          )}
          {this.props.numberOfReview === 0 && (
            <div itemprop="reviewCount" className={styles.text}>
              {NO_REVIEW_TEXT}
            </div>
          )}
        </div>
      </PdpLink>
    );
  }
}
RatingAndTextLink.propTypes = {
  averageRating: PropTypes.number,
  onClick: PropTypes.func,
  numberOfReview: PropTypes.number
};
