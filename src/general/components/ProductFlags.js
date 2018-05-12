import React from "react";
import styles from "./ProductFlags.css";
import newFlag from "./img/new.svg";
import offerFlag from "./img/offer.svg";
import exclusiveFlag from "./img/exclusive.svg";
export default class ProductFlags extends React.Component {
  renderFlag = () => {
    if (this.props.outOfStock) {
      return (
        <div className={styles.overlay}>
          <div
            className={styles.base}
            style={{ backgroundImage: `url(${newFlag})` }}
          >
            Out of Stock
          </div>
        </div>
      );
    } else if (
      this.props.discountPercent &&
      this.props.discountPercent !== "0"
    ) {
      return (
        <div
          className={styles.base}
          style={{ backgroundImage: `url(${offerFlag})` }}
        >
          {parseInt(this.props.discountPercent, 10)}% off{" "}
        </div>
      );
    } else if (this.props.isOfferExisting) {
      return (
        <div
          className={styles.base}
          style={{ backgroundImage: `url(${offerFlag})` }}
        >
          On offer
        </div>
      );
    } else if (this.props.onlineExclusive) {
      return (
        <div
          className={styles.base}
          style={{ backgroundImage: `url(${exclusiveFlag})` }}
        >
          New
        </div>
      );
    } else if (this.props.newProduct) {
      return (
        <div
          className={styles.base}
          style={{ backgroundImage: `url(${newFlag})` }}
        >
          New
        </div>
      );
    } else {
      return null;
    }
  };
  render() {
    return <React.Fragment>{this.renderFlag()}</React.Fragment>;
  }
}
