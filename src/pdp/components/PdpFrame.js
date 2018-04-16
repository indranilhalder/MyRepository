import React from "react";
import PdpFooter from "./PdpFooter";
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
        {this.props.children}
        <PdpFooter
          onAddToBag={() => this.onAddToBag()}
          productListingId={this.props.productListingId}
          winningUssID={
            this.props.ussId ? this.props.ussId : this.props.winningUssID
          }
        />
      </div>
    );
  }
}
PdpFrame.propTypes = {
  onAddToBag: PropTypes.func
};
