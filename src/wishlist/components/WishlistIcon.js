import React from "react";
import PropTypes from "prop-types";
import { Icon } from "xelpmoc-core";
import downloadIcon from "./img/download.svg";
export default class WishlistIcon extends React.Component {
  onClick() {
    const { product, wishlistItems } = this.props;
    const indexOfProduct = wishlistItems.findIndex(item => {
      return item.ussId === product.ussId;
    });
    if (indexOfProduct < 0) {
      this.props.onAddToWishlist(product); // adding product to wishlist
    } else {
      this.props.displayToast(); // product is a already in wish list show toast
    }
  }
  render() {
    return (
      <div onClick={() => this.onClick()}>
        <Icon image={downloadIcon} size={this.props.size} />
      </div>
    );
  }
}
WishlistIcon.propTypes = {
  product: PropTypes.shape({
    productId: PropTypes.string
  }).isRequired,
  wishlistItems: PropTypes.arrayOf(
    PropTypes.shape({
      productId: PropTypes.string
    })
  ),
  onAddToWishlist: PropTypes.func
};
WishlistIcon.defaultProps = {
  product: {},
  onAddToWishlist: () => {}
};
