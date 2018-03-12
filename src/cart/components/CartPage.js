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
  CHECKOUT_ROUTER,
  LOGIN_PATH
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
class CartPage extends React.Component {
  state = {
    pinCode: ""
  };
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
    if (this.props.getCoupons) {
      this.props.getCoupons();
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
    this.props.showCouponModal(this.props.cart.coupons);
  };
  renderToDeliveryPage() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (customerCookie) {
      this.props.history.push({
        pathname: CHECKOUT_ROUTER,
        state: { pinCode: this.state.pinCode }
      });
    } else {
      this.props.history.push(LOGIN_PATH);
    }
  }

  checkPinCodeAvailability = val => {
    this.setState({ pinCode: val });
    if (this.props.checkPinCodeServiceAvailability) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
      let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
      let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

      if (userDetails) {
        this.props.checkPinCodeServiceAvailability(
          JSON.parse(userDetails).customerInfo.mobileNumber,
          JSON.parse(customerCookie).access_token,
          val
        );
      } else {
        this.props.checkPinCodeServiceAvailability(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token,
          val
        );
      }
    }
  };

  render() {
    if (this.props.cart.cartDetailsStatus === SUCCESS) {
      const cartDetails = this.props.cart.cartDetails;
      return (
        <div className={styles.base}>
          <div className={styles.content}>
            <div className={styles.search}>
              <SearchAndUpdate
                getPinCode={val => this.setState({ pinCode: val })}
                checkPinCodeAvailability={val =>
                  this.checkPinCodeAvailability(val)
                }
              />
            </div>
            <div className={styles.offer}>
              <div className={styles.offerText}>{this.props.cartOfferText}</div>
              <div className={styles.offerName}>{this.props.cartOffer}</div>
            </div>

            {cartDetails.products &&
              cartDetails.products.map((product, i) => {
                return (
                  <div className={styles.cartItem} key={i}>
                    <CartItem
                      productImage={product.imageURL}
                      productDetails={product.description}
                      productName={product.productName}
                      price={product.price}
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
          {cartDetails.cartAmount && (
            <Checkout
              amount={cartDetails.cartAmount.bagTotal.formattedValue}
              bagTotal={cartDetails.cartAmount.bagTotal.formattedValue}
              tax={this.props.cartTax}
              offers={this.props.offers}
              delivery={this.props.delivery}
              payable={cartDetails.cartAmount.paybleAmount.formattedValue}
              onCheckout={() => this.renderToDeliveryPage()}
            />
          )}
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
