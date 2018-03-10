import React from "react";
import PdpFooter from "./PdpFooter";
import HollowHeader from "./HollowHeader.js";
import styles from "./PdpFrame.css";
import PropTypes from "prop-types";

export default class PdpFrame extends React.Component {
  onSave() {
    if (this.props.addProductToWishList) {
      this.props.addProductToWishList();
    }
  }
  onAddToBag() {
    if (this.props.addProductToBag) {
      this.props.addProductToBag();
    }
  }
  goBack = () => {
    if (this.props.gotoPreviousPage) {
      this.props.gotoPreviousPage();
    }
  };

  goToCartPage = () => {
    if (this.props.goToCart) {
      this.props.goToCart();
    }
  };

  goToWishList = () => {
    if (this.props.goToWishList) {
      this.props.goToWishList();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.pageHeader}>
          <HollowHeader
            goToCart={() => this.goToCartPage()}
            goToWishList={() => this.goToWishList()}
            gotoPreviousPage={() => this.goBack()}
          />
        </div>
        {this.props.children}
        <PdpFooter
          onSave={() => this.onSave()}
          onAddToBag={() => this.onAddToBag()}
        />
      </div>
    );
  }
}
PdpFrame.propTypes = {
  onSave: PropTypes.func,
  onAddToBag: PropTypes.func
};
