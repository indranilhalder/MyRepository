import React from "react";
import styles from "./BagPageItem.css";
import ProductImage from "../../general/components/ProductImage.js";
import PropTypes from "prop-types";
import {
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  RUPEE_SYMBOL,
  NO
} from "../../lib/constants";

const NOT_SERVICEABLE = "Service Not Available";
const OUT_OF_STOCK = "Product is out of stock";

export default class BagPageItem extends React.Component {
  onClick() {
    if (this.props.onClickImage) {
      this.props.onClickImage();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.productDescription}>
          {this.props.isGiveAway === NO && !this.props.isServiceAvailable
            ? localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE) && (
                <div className={styles.serviceAvailabilityText}>
                  {NOT_SERVICEABLE}
                </div>
              )
            : this.props.isOutOfStock && (
                <div className={styles.serviceAvailabilityText}>
                  {OUT_OF_STOCK}
                </div>
              )}
          {this.props.productName && (
            <div className={styles.informationText}>
              {this.props.productName}
            </div>
          )}
          {this.props.productDetails && (
            <div className={styles.informationText}>
              {this.props.productDetails}
            </div>
          )}
          {this.props.price && (
            <div className={styles.informationText}>
              {` ${RUPEE_SYMBOL}${this.props.price}`}
            </div>
          )}
          {this.props.size &&
            this.props.size !== "No Size" && (
              <div className={styles.informationText}>
                {`Size: ${this.props.size}`}
              </div>
            )}
          {this.props.color && (
            <div className={styles.informationText}>
              {`Color: ${this.props.color}`}
            </div>
          )}
        </div>
        <div className={styles.productImage}>
          <ProductImage
            image={this.props.productImage}
            onClickImage={() => this.onClick()}
          />
        </div>
      </div>
    );
  }
}
BagPageItem.propTypes = {
  productImage: PropTypes.string,
  productName: PropTypes.string,
  price: PropTypes.string,
  productDetails: PropTypes.string
};
