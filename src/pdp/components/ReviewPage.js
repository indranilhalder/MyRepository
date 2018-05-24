import React from "react";
import styles from "./ReviewPage.css";
import PropTypes from "prop-types";
import StarRating from "../../general/components/StarRating";
const months = [
  "Jan",
  "Feb",
  "Mar",
  "Apr",
  "May",
  "Jun",
  "Jul",
  "Aug",
  "Sep",
  "Oct",
  "Nov",
  "Dec"
];

const INVALID_DATE = "Invalid Date";
export default class ReviewPage extends React.Component {
  render() {
    let getDate;
    let userReviewDate =
      this.props.date && this.props.date.split(" ")[0].split("-");
    if (userReviewDate) {
      getDate = userReviewDate[2].split("T")[0];
    }
    let date =
      getDate + " " + months[userReviewDate[1] - 1] + ", " + userReviewDate[0];
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
        {date && date !== INVALID_DATE ? (
          <div className={styles.dateTimeBox}>
            {this.props.name &&
              this.props.name.charAt(0).toUpperCase() +
                this.props.name.slice(1).toLowerCase()}
            {", "}
            {date}
          </div>
        ) : (
          <div className={styles.dateTimeBox}>
            {this.props.name &&
              this.props.name.charAt(0).toUpperCase() +
                this.props.name.slice(1).toLowerCase()}
          </div>
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
