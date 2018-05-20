import React from "react";
import styles from "./ReviewPage.css";
import PropTypes from "prop-types";
import StarRating from "../../general/components/StarRating";
const INVALID_DATE = "Invalid Date";
export default class ReviewPage extends React.Component {
  render() {
    const date = new Date(this.props.date).toDateString();
    return (
      <div className={styles.base}>
        {this.props.rating && (
          <div className={styles.starHolder}>
            <StarRating averageRating={this.props.rating} />
          </div>
        )}
        {this.props.heading && (
          <div className={styles.heading}>{this.props.heading}</div>
        )}
        {this.props.text && (
          <div className={styles.text}>{this.props.text}</div>
        )}
        {this.props.date &&
          this.props.date !== INVALID_DATE && (
            <div className={styles.dateTimeBox}>{date}</div>
          )}
      </div>
    );
  }
}
ReviewPage.propTypes = {
  text: PropTypes.string,
  date: PropTypes.string,
  heading: PropTypes.string,
  rating: PropTypes.number
};
