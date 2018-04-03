import React from "react";
import CartItem from "./CartItem";
import Checkout from "./Checkout";
import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import styles from "./CartPage.css";
import PropTypes from "prop-types";
import MDSpinner from "react-md-spinner";
import { SUCCESS, HOME_ROUTER } from "../../lib/constants";
import SavedProduct from "./SavedProduct";
import filter from "lodash/filter";
import { Redirect } from "react-router-dom";
import { MAIN_ROUTER } from "../../lib/constants";
import TextWithUnderLine from "./TextWithUnderLine.js";
import EmptyBag from "./EmptyBag.js";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER,
  CHECKOUT_ROUTER,
  LOGIN_PATH,
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  YES
} from "../../lib/constants";
import { YOUR_BAG } from "../../lib/headerName";
import * as Cookie from "../../lib/Cookie";

class CartPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pinCode: "",
      isServiceable: false,
      changePinCode: false
    };
  }

  componentDidMount() {
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    const cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);

    const defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
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
      this.props.displayCouponsForLoggedInUser(
        JSON.parse(userDetails).userName,
        JSON.parse(customerCookie).access_token,
        JSON.parse(cartDetailsLoggedInUser).guid
      );
    } else {
      if (globalCookie !== undefined && cartDetailsAnonymous !== undefined) {
        this.props.getCartDetails(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token,
          JSON.parse(cartDetailsAnonymous).guid,
          defaultPinCode
        );
        this.props.displayCouponsForAnonymous(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token
        );
      }
    }
  }

  componentDidUpdate(prevProps, prevState) {
    this.props.setHeaderText(YOUR_BAG);
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

  removeItemFromCart = cartListItemPosition => {
    const pinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
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

  updateQuantityInCart = (selectedItem, quantity) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    if (userDetails) {
      if (this.props.updateQuantityInCartLoggedIn) {
        this.props.updateQuantityInCartLoggedIn(
          selectedItem,
          quantity,
          localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        );
      }
    } else {
      if (this.props.updateQuantityInCartLoggedOut) {
        this.props.updateQuantityInCartLoggedOut(
          selectedItem,
          quantity,
          localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        );
      }
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
    let pinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (customerCookie) {
      if (pinCode && this.state.isServiceable === true) {
        this.props.history.push({
          pathname: CHECKOUT_ROUTER,
          state: {
            productValue: this.props.cart.cartDetails.cartAmount.bagTotal.value,
            isRequestComeThrowMyBag: true
          }
        });
      }
      if (!pinCode) {
        this.props.displayToast("Please enter Pin code / Zip code");
      } else {
        this.setState({ isServiceable: false });
      }
    } else {
      this.props.history.push(LOGIN_PATH);
    }
  }

  checkPinCodeAvailability = val => {
    this.setState({ pinCode: val, changePinCode: false });
    localStorage.setItem(DEFAULT_PIN_CODE_LOCAL_STORAGE, val);
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
  };

  changePinCode = () => {
    this.setState({ changePinCode: true });
  };
  render() {
    const globalAccessToken = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    const cartDetailsForAnonymous = Cookie.getCookie(
      CART_DETAILS_FOR_ANONYMOUS
    );

    if (!globalAccessToken && !cartDetailsForAnonymous) {
      return <Redirect exact to={HOME_ROUTER} />;
    }
    if (this.props.cart.cartDetailsStatus === SUCCESS) {
      const cartDetails = this.props.cart.cartDetails;
      let defaultPinCode;
      let deliveryCharge = 0;
      let couponDiscount = 0;
      let totalDiscount = 0;
      if (cartDetails.products) {
        if (
          cartDetails.products &&
          cartDetails.products[0].elligibleDeliveryMode
        ) {
          deliveryCharge =
            cartDetails.products[0].elligibleDeliveryMode[0].charge
              .formattedValue;
        }
        if (cartDetails.cartAmount.totalDiscountAmount) {
          totalDiscount =
            cartDetails.cartAmount.totalDiscountAmount.formattedValue;
        }

        if (cartDetails.cartAmount.couponDiscountAmount) {
          couponDiscount =
            cartDetails.cartAmount.couponDiscountAmount.formattedValue;
        }
        if (cartDetails.products) {
          defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
        }
      }

      return (
        <div className={styles.base}>
          <div className={styles.content}>
            {(!defaultPinCode || this.state.changePinCode) && (
              <div className={styles.search}>
                <SearchAndUpdate
                  value={defaultPinCode}
                  getPinCode={val => this.setState({ pinCode: val })}
                  checkPinCodeAvailability={val =>
                    this.checkPinCodeAvailability(val)
                  }
                  labelText="check"
                />
              </div>
            )}
            {!this.state.changePinCode &&
              defaultPinCode && (
                <TextWithUnderLine
                  heading={defaultPinCode}
                  onClick={() => this.changePinCode()}
                  buttonLabel="Change"
                />
              )}
          </div>
          {!cartDetails.products && <EmptyBag />}

          <div
            className={defaultPinCode === "" ? styles.disabled : styles.content}
          >
            {cartDetails.products &&
              cartDetails.products.map((product, i) => {
                let serviceable = false;
                if (product.pinCodeResponse) {
                  if (product.pinCodeResponse.isServicable === YES) {
                    serviceable = true;
                  }
                }

                return (
                  <div className={styles.cartItem} key={i}>
                    <CartItem
                      pinCode={defaultPinCode}
                      product={product}
                      productIsServiceable={serviceable}
                      productImage={product.imageURL}
                      productDetails={product.description}
                      productName={product.productName}
                      price={product.offerPrice}
                      index={i}
                      entryNumber={product.entryNumber}
                      deliveryInformation={product.elligibleDeliveryMode}
                      deliverTime={
                        product.elligibleDeliveryMode &&
                        product.elligibleDeliveryMode[0].desc
                      }
                      deliveryType={
                        product.elligibleDeliveryMode &&
                        product.elligibleDeliveryMode[0].name
                      }
                      option={[
                        {
                          value: product.qtySelectedByUser,
                          label: product.qtySelectedByUser
                        }
                      ]}
                      onSave={this.addProductToWishList}
                      onRemove={this.removeItemFromCart}
                      onQuantityChange={this.updateQuantityInCart}
                      maxQuantityAllowed={product.maxQuantityAllowed}
                      qtySelectedByUser={product.qtySelectedByUser}
                    />
                  </div>
                );
              })}

            {cartDetails.products && (
              <SavedProduct onApplyCoupon={() => this.goToCouponPage()} />
            )}
            {cartDetails.products &&
              cartDetails.cartAmount && (
                <Checkout
                  amount={cartDetails.cartAmount.paybleAmount.formattedValue}
                  bagTotal={cartDetails.cartAmount.bagTotal.formattedValue}
                  coupons={couponDiscount}
                  discount={totalDiscount}
                  delivery={deliveryCharge}
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
  getCartDetails: PropTypes.func,
  updateQuantityInCartLoggedIn: PropTypes.func,
  updateQuantityInCartLoggedOut: PropTypes.func
};

CartPage.defaultProps = {
  cartOfferText: "Congratulations your cart qualifies for.",
  cartOffer: "FREE shipping",
  cartTax: "included",
  delivery: "Free",
  offers: "Apply"
};

export default CartPage;
