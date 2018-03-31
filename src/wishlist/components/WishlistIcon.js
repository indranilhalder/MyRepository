import React from "react";
import PropTypes from "prop-types";
import { Icon } from "xelpmoc-core";
import downloadIcon from "./img/download.svg";
export default class WishlistIcon extends React.Component {
  onClick(e) {
    e.stopPropagation();
    const { productListingId, winningUssID, wishlistItems } = this.props;
    const indexOfProduct = wishlistItems.findIndex(item => {
      return (
        item.productcode === productListingId || item.USSID === winningUssID
      );
    });
    if (indexOfProduct < 0) {
      this.props.addProductToWishList({
        productcode: productListingId,
        USSID: winningUssID
      }); // adding product to wishlist
    } else {
      this.props.displayToast(); // product is a already in wish list show toast
    }
  }
  render() {
    return (
      <div onClick={e => this.onClick(e)}>
        <Icon image={downloadIcon} size={this.props.size} />
      </div>
    );
  }
}
WishlistIcon.propTypes = {
  productListingId: PropTypes.string.isRequired,
  winningUssID: PropTypes.string.isRequired,
  wishlistItems: PropTypes.arrayOf(
    PropTypes.shape({
      productListingId: PropTypes.string,
      winningUssID: PropTypes.string
    })
  ),
  addProductToWishList: PropTypes.func,
  size: PropTypes.number
};
WishlistIcon.defaultProps = {
  size: 20,
  addProductToWishList: () => {}
};
