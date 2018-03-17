import React from "react";
import CartItem from "./CartItem";
import Checkout from "./Checkout";
import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import styles from "./CartPage.css";
import PropTypes from "prop-types";
import MDSpinner from "react-md-spinner";
import { SUCCESS } from "../../lib/constants";
import SavedProduct from "./SavedProduct";
import filter from "lodash/filter";

import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER,
  CHECKOUT_ROUTER,
  LOGIN_PATH,
  DEFAULT_PIN_CODE_LOCAL_STORAGE
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";

const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
const cartDetailsLoggedInUser = Cookie.getCookie(
  CART_DETAILS_FOR_LOGGED_IN_USER
);
const cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);

const defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

class CartPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pinCode: "",
      isServiceable: false
    };
  }

  componentDidMount() {
    if (
      userDetails !== undefined &&
      customerCookie !== undefined &&
      cartDetailsLoggedInUser !== undefined
    ) {
      this.props.getCartDetails(
        JSON.parse(userDetails).userName,
        JSON.parse(customerCookie).access_token,
        JSON.parse(cartDetailsLoggedInUser).code,
        defaultPinCode
      );
    } else {
      if (globalCookie !== undefined && cartDetailsAnonymous !== undefined) {
        this.props.getCartDetails(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token,
          JSON.parse(cartDetailsAnonymous).guid,
          defaultPinCode
        );
      }
    }
    if (userDetails) {
      if (this.props.getCoupons) {
        this.props.getCoupons();
      }
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (prevProps.cart) {
      if (prevProps.cart.cartDetails !== this.props.cart.cartDetails) {
        let productServiceAvailability = filter(
          this.props.cart.cartDetails.products,
          product => {
            if (product.pinCodeResponse) {
              return product.pinCodeResponse.isServicable === "N";
            }
          }
        );
        if (productServiceAvailability.length > 0) {
          this.setState({
            isServiceable: false
          });
        } else {
          this.setState({
            isServiceable: true
          });
        }
      }
    }
  }
  renderLoader = () => {
    return (
      <div>
        <MDSpinner />
      </div>
    );
  };

  addProductToWishList = product => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    if (userDetails) {
      if (this.props.addProductToWishList) {
        this.props.addProductToWishList(product);
      }
    } else {
      this.props.history.push(LOGIN_PATH);
    }
  };

  removeItemFromCart = (cartListItemPosition, pinCode) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    if (userDetails) {
      if (this.props.removeItemFromCartLoggedIn) {
        this.props.removeItemFromCartLoggedIn(cartListItemPosition, pinCode);
      }
    } else {
      if (this.props.removeItemFromCartLoggedOut) {
        this.props.removeItemFromCartLoggedOut(cartListItemPosition, "");
      }
    }
  };

  applyCoupon = couponCode => {
    if (this.props.applyCoupon) {
      this.props.applyCoupon();
    }
  };

  releaseCoupon = couponCode => {
    if (this.props.releaseCoupon) {
      this.props.releaseCoupon();
    }
  };
  goToCouponPage = () => {
    this.props.showCouponModal(this.props.cart.coupons);
  };
  renderToCheckOutPage() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (customerCookie) {
      if (defaultPinCode && this.state.isServiceable === true) {
        this.props.history.push({
          pathname: CHECKOUT_ROUTER,
          state: {
            pinCode: defaultPinCode,
            productValue: this.props.cart.cartDetails.cartAmount.bagTotal.value
          }
        });
      } else {
        this.setState({ isServiceable: false });
      }
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
      let cartDetailsLoggedInUser = Cookie.getCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER
      );
      let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
      if (userDetails) {
        this.props.getCartDetails(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token,
          JSON.parse(cartDetailsLoggedInUser).code,
          val
        );
      } else {
        this.props.getCartDetails(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token,
          JSON.parse(cartDetailsAnonymous).guid,
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
          </div>
          <div
            className={defaultPinCode === "" ? styles.disabled : styles.content}
          >
            <div className={styles.offer}>
              <div className={styles.offerText}>{this.props.cartOfferText}</div>
              <div className={styles.offerName}>{this.props.cartOffer}</div>
            </div>

            {cartDetails.products &&
              cartDetails.products.map((product, i) => {
                return (
                  <div className={styles.cartItem} key={i}>
                    <CartItem
                      pinCode={defaultPinCode}
                      product={product}
                      productIsServiceable={product.pinCodeResponse}
                      productImage={product.imageURL}
                      productDetails={product.description}
                      productName={product.productName}
                      price={product.price}
                      index={i}
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
                      onSave={this.addProductToWishList}
                      onRemove={this.removeItemFromCart}
                    />
                  </div>
                );
              })}
            <SavedProduct onApplyCoupon={() => this.goToCouponPage()} />
            {cartDetails.cartAmount && (
              <Checkout
                amount={cartDetails.cartAmount.bagTotal.formattedValue}
                bagTotal={cartDetails.cartAmount.bagTotal.formattedValue}
                tax={this.props.cartTax}
                offers={this.props.offers}
                delivery={this.props.delivery}
                payable={cartDetails.cartAmount.paybleAmount.formattedValue}
                onCheckout={() => this.renderToCheckOutPage()}
              />
            )}
          </div>
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
  offers: PropTypes.string,
  removeItemFromCartLoggedOut: PropTypes.func,
  removeItemFromCartLoggedIn: PropTypes.func,
  addProductToWishList: PropTypes.func,
  getCartDetails: PropTypes.func
};

CartPage.defaultProps = {
  cartOfferText: "Congratulations your cart qualifies for.",
  cartOffer: "FREE shipping",
  cartTax: "included",
  delivery: "Free",
  offers: "Apply"
};

export default CartPage;
