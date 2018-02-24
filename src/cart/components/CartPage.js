import React from "react";
import CartItem from "./CartItem";
import Checkout from "./Checkout";
import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import styles from "./CartPage.css";
import PropTypes from "prop-types";
import MDSpinner from "react-md-spinner";
import { SUCCESS } from "../../lib/constants";
import SavedProduct from "./SavedProduct";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER,
  PRODUCT_DELIVERY_ADDRESSES
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
class CartPage extends React.Component {
  componentDidMount() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);

    if (userDetails) {
      this.props.getCartDetails(
        JSON.parse(userDetails).customerInfo.mobileNumber,
        JSON.parse(customerCookie).access_token,
        JSON.parse(cartDetailsLoggedInUser).code
      );
    } else {
      this.props.getCartDetails(
        ANONYMOUS_USER,
        JSON.parse(globalCookie).access_token,
        JSON.parse(cartDetailsAnonymous).guid
      );
    }
  }

  renderLoader = () => {
    return (
      <div>
        <MDSpinner />
      </div>
    );
  };

  goToCouponPage = () => {
    this.props.showCouponModal(this.props.productDetails);
  };
  renderToDeliveryPage() {
    this.props.history.push(PRODUCT_DELIVERY_ADDRESSES);
  }
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

            {cartDetails.products.map((product, i) => {
              return (
                <div className={styles.cartItem} key={i}>
                  <CartItem
                    productImage={product.imageURL}
                    productDetails={product.description}
                    productName={product.productName}
                    price={product.priceValue.sellingPrice.formattedValue}
                    deliveryInformation={product.elligibleDeliveryMode}
                    deliverTime={
                      product.elligibleDeliveryMode &&
                      product.elligibleDeliveryMode[0].desc
                    }
                    option={[
                      {
                        value: product.qtySelectedByUser,
                        label: product.qtySelectedByUser
                      }
                    ]}
                  />
                </div>
              );
            })}
          </div>
          <SavedProduct onApplyCoupon={() => this.goToCouponPage()} />
          <Checkout
            amount={cartDetails.cartAmount.bagTotal.formattedValue}
            bagTotal={cartDetails.cartAmount.bagTotal.formattedValue}
            tax={this.props.cartTax}
            offers={this.props.offers}
            delivery={this.props.delivery}
            payable={cartDetails.cartAmount.paybleAmount.formattedValue}
            onCheckout={() => this.renderToDeliveryPage()}
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
