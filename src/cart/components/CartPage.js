import React from "react";
import CartItem from "./CartItem";
import Checkout from "./Checkout";
import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import styles from "./CartPage.css";
import PropTypes from "prop-types";
import SecondaryLoader from "../../general/components/SecondaryLoader";
import {
  SUCCESS,
  HOME_ROUTER,
  NO,
  BANK_COUPON_COOKIE
} from "../../lib/constants";
import SavedProduct from "./SavedProduct";
import filter from "lodash.filter";
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
  YES,
  YOUR_BAG,
  MY_ACCOUNT_PAGE,
  SAVE_LIST_PAGE,
  COUPON_COOKIE
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import {
  setDataLayerForCartDirectCalls,
  ADOBE_CALLS_FOR_ON_CLICK_CHECKOUT
} from "../../lib/adobeUtils";
const PRODUCT_NOT_SERVICEABLE_MESSAGE =
  "Product is not Serviceable,Please try with another pin code";
const CHECKOUT_BUTTON_TEXT = "Checkout";
class CartPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pinCode: "",
      isServiceable: false,
      changePinCode: false,
      appliedCouponCode: null
    };
  }
  navigateToHome() {
    this.props.history.push(HOME_ROUTER);
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
    // delete bank coupon localstorage if it is exits.
    // because we user can not have bank offer cookie on cart page
    if (localStorage.getItem(BANK_COUPON_COOKIE)) {
      localStorage.removeItem(BANK_COUPON_COOKIE);
    }
  }

  componentWillReceiveProps(nextProps) {
    let cartCouponCode =
      nextProps.cart &&
      nextProps.cart.cartDetails &&
      nextProps.cart.cartDetails.appliedCoupon
        ? nextProps.cart.cartDetails.appliedCoupon
        : undefined;
    this.setState({
      appliedCouponCode: cartCouponCode
    });
  }
  componentDidUpdate(prevProps, prevState) {
    this.props.setHeaderText(YOUR_BAG);
    if (prevProps.cart) {
      if (prevProps.cart.cartDetails !== this.props.cart.cartDetails) {
        let productServiceAvailability = filter(
          this.props.cart &&
            this.props.cart.cartDetails &&
            this.props.cart.cartDetails.products,
          product => {
            return (
              product.isGiveAway === NO &&
              (product.pinCodeResponse === undefined ||
                (product.pinCodeResponse &&
                  product.pinCodeResponse.isServicable === "N") ||
                product.isOutOfStock)
            );
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
      <div className={styles.cartLoader}>
        <div className={styles.spinner}>
          <SecondaryLoader />
        </div>
      </div>
    );
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
    let couponDetails = Object.assign(this.props.cart.coupons, this.props);
    this.props.showCouponModal(couponDetails);
  };

  navigateToLogin() {
    const url = this.props.location.pathname;
    this.props.setUrlToRedirectToAfterAuth(url);
    this.props.history.replace(LOGIN_PATH);
  }
  onClickImage(productCode) {
    if (productCode) {
      this.props.history.push(`/p-${productCode.toLowerCase()}`);
    }
  }
  renderToCheckOutPage() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

    if (!customerCookie || !userDetails) {
      return this.navigateToLogin();
    }
    let pinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

    if (pinCode && this.state.isServiceable === true) {
      setDataLayerForCartDirectCalls(ADOBE_CALLS_FOR_ON_CLICK_CHECKOUT);
      this.props.history.push({
        pathname: CHECKOUT_ROUTER
      });
    }
    if (!pinCode) {
      this.props.displayToast("Please enter Pin code / Zip code");
    } else if (!this.state.isServiceable) {
      this.props.displayToast(PRODUCT_NOT_SERVICEABLE_MESSAGE);
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
        val,
        true // this is for setting data layer for change pincode
      );
    } else {
      this.props.getCartDetails(
        ANONYMOUS_USER,
        JSON.parse(globalCookie).access_token,
        JSON.parse(cartDetailsAnonymous).guid,
        val,
        true // this is for setting data layer for change pincode
      );
    }
  };

  goToWishList = () => {
    if (this.props.history) {
      const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
      const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
      if (!userDetails || !customerCookie) {
        this.props.history.push(LOGIN_PATH);
      } else {
        this.props.history.push(`${MY_ACCOUNT_PAGE}${SAVE_LIST_PAGE}`);
      }
    }
  };

  changePinCode = () => {
    // show modal for address here
    this.props.addressModal({
      addressModalForCartPage: true,
      labelText: "Update",
      checkPinCodeAvailability: pinCode =>
        this.checkPinCodeAvailability(pinCode)
    });
  };

  renderEmptyBag = () => {
    let defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

    return (
      <div className={styles.base}>
        <div className={styles.content}>
          <TextWithUnderLine
            heading={
              defaultPinCode && defaultPinCode !== "undefined"
                ? `Pincode-${defaultPinCode}`
                : "Enter Pincode"
            }
            boxShadow={
              defaultPinCode && defaultPinCode !== "undefined" ? true : false
            }
            onClick={() => this.changePinCode()}
            buttonLabel="Change"
          />
        </div>
        <div className={styles.content}>
          <EmptyBag
            onContinueShopping={() => this.navigateToHome()}
            viewSavedProduct={() => this.goToWishList()}
          />
        </div>
      </div>
    );
  };

  render() {
    const globalAccessToken = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    const cartDetailsForAnonymous = Cookie.getCookie(
      CART_DETAILS_FOR_ANONYMOUS
    );

    if (
      this.props.cart.loadingForDisplayCoupon ||
      this.props.cart.loadingForCartDetail
    ) {
      return this.renderLoader();
    } else {
      if (this.props.cart.loading) {
        this.props.showSecondaryLoader();
      } else {
        this.props.hideSecondaryLoader();
      }
    }

    if (!globalAccessToken && !cartDetailsForAnonymous) {
      return <Redirect exact to={HOME_ROUTER} />;
    }

    if (this.props.cart.cartDetails && this.props.cart.cartDetails.products) {
      const cartDetails = this.props.cart.cartDetails;
      let defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
      let deliveryCharge = "0.00";
      let couponDiscount = "0.00";
      let totalDiscount = "0.00";
      if (cartDetails.products) {
        if (cartDetails.deliveryCharge) {
          deliveryCharge = cartDetails.deliveryCharge
            ? cartDetails.deliveryCharge
            : "0.00";
        }
        if (cartDetails.cartAmount.totalDiscountAmount) {
          totalDiscount = cartDetails.cartAmount.totalDiscountAmount.value
            ? Math.round(
                cartDetails.cartAmount.totalDiscountAmount.value * 100
              ) / 100
            : "0.00";
        }

        if (cartDetails.cartAmount.couponDiscountAmount) {
          couponDiscount = cartDetails.cartAmount.couponDiscountAmount.value
            ? Math.round(
                cartDetails.cartAmount.couponDiscountAmount.value * 100
              ) / 100
            : "0.00";
        }
      }
      return (
        <div className={styles.base}>
          <div className={styles.content}>
            <TextWithUnderLine
              onClick={() => this.changePinCode()}
              buttonLabel="Change"
              checkPinCodeAvailability={pinCode =>
                this.checkPinCodeAvailability(pinCode)
              }
            />
          </div>
          <div className={styles.content}>
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
                      color={product.color}
                      size={product.size}
                      price={product.price}
                      offerPrice={product.offerPrice}
                      isGiveAway={product.isGiveAway}
                      index={i}
                      entryNumber={product.entryNumber}
                      deliveryInformation={product.elligibleDeliveryMode}
                      deliverTime={
                        product.elligibleDeliveryMode &&
                        product.elligibleDeliveryMode[0].desc
                      }
                      deliveryType={
                        product.elligibleDeliveryMode &&
                        product.elligibleDeliveryMode[0].code
                      }
                      onRemove={this.removeItemFromCart}
                      onQuantityChange={this.updateQuantityInCart}
                      maxQuantityAllowed={
                        parseInt(product.maxQuantityAllowed, 10) <
                        product.availableStockCount
                          ? parseInt(product.maxQuantityAllowed, 10)
                          : product.availableStockCount
                      }
                      isOutOfStock={product.isOutOfStock}
                      qtySelectedByUser={product.qtySelectedByUser}
                      isClickable={false}
                      onClickImage={() =>
                        this.onClickImage(product.productcode)
                      }
                    />
                  </div>
                );
              })}
            {cartDetails.products && (
              <SavedProduct
                saveProduct={() => this.goToWishList()}
                onApplyCoupon={() => this.goToCouponPage()}
                appliedCouponCode={this.state.appliedCouponCode}
              />
            )}
            {cartDetails.products &&
              cartDetails.cartAmount && (
                <Checkout
                  disabled={!this.state.isServiceable}
                  amount={
                    cartDetails.cartAmount.paybleAmount.value
                      ? Math.round(
                          cartDetails.cartAmount.paybleAmount.value * 100
                        ) / 100
                      : "0.00"
                  }
                  bagTotal={
                    cartDetails.cartAmount.bagTotal.value
                      ? Math.round(
                          cartDetails.cartAmount.bagTotal.value * 100
                        ) / 100
                      : "0.00"
                  }
                  coupons={couponDiscount}
                  discount={totalDiscount}
                  delivery={deliveryCharge}
                  payable={
                    cartDetails.cartAmount.paybleAmount.value
                      ? Math.round(
                          cartDetails.cartAmount.paybleAmount.value * 100
                        ) / 100
                      : "0.00"
                  }
                  onCheckout={() => this.renderToCheckOutPage()}
                  label={CHECKOUT_BUTTON_TEXT}
                />
              )}
          </div>
        </div>
      );
    } else {
      return this.renderEmptyBag();
    }
  }

  componentWillUnmount() {
    if (this.props.clearCartDetails) {
      this.props.clearCartDetails();
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
