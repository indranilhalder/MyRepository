import React from "react";
import CartItem from "./CartItem";
import Checkout from "./Checkout";
import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import styles from "./CartPage.css";
import PropTypes from "prop-types";
import MDSpinner from "react-md-spinner";
import { SUCCESS } from "../../lib/constants";
class CartPage extends React.Component {
  componentDidMount() {
    this.props.getCartDetails();
  }

  renderLoader = () => {
    return (
      <div>
        <MDSpinner />
      </div>
    );
  };

  render() {
    if (this.props.cart.cartDetailsStatus === SUCCESS) {
      const cartDetails = this.props.cart.cartDetails;
      return (
        <div className={styles.base}>
          <div className={styles.content}>
            <div className={styles.search}>
              <SearchAndUpdate />
            </div>
            <div className={styles.offer}>
              <div className={styles.offerText}>{this.props.cartOfferText}</div>
              <div className={styles.offerName}>{this.props.cartOffer}</div>
            </div>

            {cartDetails.products.map((val, i) => {
              return (
                <div className={styles.cartItem}>
                  <CartItem
                    key={i}
                    productImage={val.imageURL}
                    productDetails={val.description}
                    productName={val.productName}
                    price={val.priceValue.sellingPrice.formattedValue}
                    deliveryInformation={val.elligibleDeliveryMode}
                    deliverTime={val.elligibleDeliveryMode[0].desc}
                    option={[
                      {
                        value: val.qtySelectedByUser,
                        label: val.qtySelectedByUser
                      }
                    ]}
                  />
                </div>
              );
            })}
          </div>
          <Checkout
            amount={cartDetails.cartAmount.bagTotal.formattedValue}
            bagTotal={cartDetails.cartAmount.bagTotal.formattedValue}
            tax={this.props.cartTax}
            offers={this.props.offers}
            delivery={this.props.delivery}
            payable={cartDetails.cartAmount.paybleAmount.formattedValue}
          />
        </div>
      );
    } else {
      return this.renderLoader();
    }
  }
}

CartPage.propTypes = {
  cartOfferText: PropTypes.string,
  cartOffer: PropTypes.string,
  cartTax: PropTypes.string,
  delivery: PropTypes.string,
  offers: PropTypes.string
};

CartPage.defaultProps = {
  cartOfferText: "Congratulations your cart qualifies for.",
  cartOffer: "FREE shipping",
  cartTax: "included",
  delivery: "Free",
  offers: "Apply"
};

export default CartPage;
