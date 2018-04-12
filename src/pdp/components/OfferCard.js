import React from "react";
import styles from "./OfferCard.css";
import PropTypes from "prop-types";

export default class OfferCard extends React.Component {
  handleClick(val) {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  handleShowDetails = () => {
    console.log("in click");
    if (this.props.showDetails) {
      console.log("in click");
      this.props.showDetails({
        potentialPromotions: { ...this.props.potentialPromotions },
        secondaryPromotions: { ...this.props.secondaryPromotions }
      });
    }
  };
  render() {
    console.log(this.props);
    if (this.props.potentialPromotions || this.props.secondaryPromotions) {
      return (
        <div className={styles.base} onClick={this.handleShowDetails}>
          {this.props.potentialPromotions && (
            <div className={styles.headingText}>
              {this.props.potentialPromotions.title}
            </div>
          )}
          {this.props.secondaryPromotions && (
            <div className={styles.headingText}>
              {this.props.secondaryPromotions.messageID}
            </div>
          )}
        </div>
      );
    } else {
      return null;
    }
  }
}
