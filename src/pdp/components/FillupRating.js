import React from "react";
import styles from "./FillupRating.css";
import PropTypes from "prop-types";
export default class FillupRating extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      rating: this.props.rating && null
    };
  }
  rate(rating) {
    this.setState(
      {
        rating
      },
      () => {
        if (this.props.onChange) {
          this.props.onChange(this.state.rating);
        }
      }
    );
  }
  render() {
    const starSpans = [];
    for (let i = 1; i <= this.props.rating; i++) {
      let classStar = styles.ratingStar;
      if (this.state.rating >= i && this.state.rating !== null) {
        classStar = styles.ratingFillStar;
      }
      starSpans.push(
        <div className={styles.ratingHolder}>
          <div className={styles.startHolder}>
            <div className={classStar} onClick={() => this.rate(i)} />
          </div>
        </div>
      );
    }
    return <div className={styles.base}>{starSpans}</div>;
  }
}
FillupRating.propTypes = {
  rating: PropTypes.number,
  onChange: PropTypes.func
};
