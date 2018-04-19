import React from "react";
import PropTypes from "prop-types";
import styles from "./EmptyBag.css";
import Button from "../../general/components/Button.js";
import bagIcon from "../../general/components/img/order-history.svg";
import Icon from "../../xelpmoc-core/Icon";
export default class EmptyBag extends React.Component {
  handleOnContinue() {
    if (this.props.onContinueShopping) {
      this.props.onContinueShopping();
    }
  }
  handleOnSaved() {
    if (this.props.viewSavedProduct) {
      this.props.viewSavedProduct();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.bagIconAndTextHolder}>
          <div className={styles.bagImageHolder}>
            <div className={styles.bagImage}>
              <Icon image={bagIcon} size={80} />
            </div>
          </div>
          <div className={styles.headingText}>Your Bag is empty </div>
          <div className={styles.infoText}>
            Add some wonderful products from Tata CLiQ
          </div>
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              type="primary"
              backgroundColor="#ff1744"
              height={36}
              label="Continue Shopping"
              width={210}
              textStyle={{ color: "#FFF", fontSize: 14 }}
              onClick={() => this.handleOnContinue()}
            />
          </div>
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              type="hollow"
              height={36}
              label="View saved products"
              width={210}
              textStyle={{ color: "#212121", fontSize: 14 }}
              onClick={() => this.handleOnSaved()}
            />
          </div>
        </div>
      </div>
    );
  }
}
EmptyBag.propTypes = {
  onContinueShopping: PropTypes.func,
  viewSavedProduct: PropTypes.func
};
