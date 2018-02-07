import React from "react";
import styles from "./StarRating.css";
import PropTypes from "prop-types";
import FilledStar from "./img/star-fill.svg";
import Star from "./img/star-stroke.svg";
import { Icon } from "xelpmoc-core";
export default class StarRating extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    const starSpans = [];
    const rating = this.props.averageRating;
    for (let i = 1; i <= 5; i++) {
      if (i <= rating) {
        starSpans.push(
          <div key={i} className={styles.star}>
            <Icon image={FilledStar} size={15} />
          </div>
        );
      } else {
        starSpans.push(
          <div key={i} className={styles.star}>
            <Icon image={Star} size={15} />
          </div>
        );
      }
    }
    return (
      <div className={styles.base}>
        <div className={styles.starHolder}>{starSpans}</div>
        {this.props.children && (
          <div className={styles.content} onClick={() => this.onClick()}>
            {this.props.children}
          </div>
        )}
      </div>
    );
  }
}
StarRating.propTypes = {
  averageRating: PropTypes.number,
  onClick: PropTypes.func
};
