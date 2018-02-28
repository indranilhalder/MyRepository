import React from "react";
import styles from "./JewelleryDetailsAndLink.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class JewelleryDetailsAndLink extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.detailsContainer}>
          {this.props.productName && (
            <div className={styles.productName}>{this.props.productName}</div>
          )}
          {this.props.productDescription && (
            <div className={styles.productDescription}>
              {this.props.productDescription}
            </div>
          )}
          {this.props.productMaterial && (
            <div className={styles.productMaterial}>
              {this.props.productMaterial}
            </div>
          )}
        </div>
        <div className={styles.priceContainer}>
          {this.props.price && (
            <div className={styles.price}>{`Rs. ${this.props.price}`}</div>
          )}
          {this.props.deletePrice && (
            <div className={styles.deletePriceAndDiscount}>
              <div className={styles.deletePrice}>
                {`Rs. ${this.props.deletePrice}`}
              </div>
              <div className={styles.discount}>
                {this.props.discount && `(${this.props.discount}%)`}
              </div>
            </div>
          )}
          <div className={styles.button} onClick={() => this.handleClick()}>
            <UnderLinedButton label="Price Breakup" color={"#ff1744"} />
          </div>
        </div>
      </div>
    );
  }
}
JewelleryDetailsAndLink.propTypes = {
  productName: PropTypes.string,
  productDescription: PropTypes.string,
  productMaterial: PropTypes.string,
  deletePrice: PropTypes.string,
  discount: PropTypes.string,
  onClick: PropTypes.func
};
