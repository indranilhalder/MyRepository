import React from "react";
import styles from "./ReviewPage.css";
import PropTypes from "prop-types";
import StarRating from "../../general/components/StarRating";
import format from "date-fns/format";

const INVALID_DATE = "Invalid Date";
export default class ReviewPage extends React.Component {
  render() {
    let formattedDate;
    if (this.props.date && this.props.date.indexOf("T") > -1) {
      let dateOfBirth = new Date(this.props.date.split("T").join());
      formattedDate = format(dateOfBirth, "YYYY-MM-DD");
    } else if (this.props.date) {
      formattedDate = this.props.date
        .split("/")
        .reverse()
        .join("-");
    }
    let date = new Date(formattedDate).toDateString();
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
            {"  "}
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
