import React from "react";
import PropTypes from "prop-types";
import StarRating from "../../general/components/StarRating.js";
import PdpLink from "./PdpLink";
import styles from "./RatingAndTextLink.css";
export default class RatingAndTextLink extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <PdpLink onClick={() => this.onClick()}>
        <div className={styles.base}>
          <div className={styles.ratingsHolder}>
            <StarRating averageRating={this.props.averageRating} />
          </div>
          <div className={styles.textHolder}>
            <span>{this.props.numberOfReview}</span>
            reviews for this product
          </div>
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
