import React from "react";
import cloneDeep from "lodash.clonedeep";
import PropTypes from "prop-types";
import DummyTab from "./DummyTab";
import ConfirmAddress from "./ConfirmAddress";
import Checkout from "./Checkout";
import AddDeliveryAddress from "./AddDeliveryAddress";
import * as Cookie from "../../lib/Cookie";
import DeliveryAddressSet from "./DeliveryAddressSet";
import DeliveryModeSet from "./DeliveryModeSet";
import styles from "./CheckOutPage.css";
import CheckOutHeader from "./CheckOutHeader";
import PaymentCardWrapper from "./PaymentCardWrapper.js";
import CartItem from "./CartItem";
import BankOffer from "./BankOffer.js";
import GridSelect from "../../general/components/GridSelect";
import find from "lodash.find";
import OrderConfirmation from "./OrderConfirmation";
import queryString, { parse } from "query-string";
import PiqPage from "./PiqPage";
import size from "lodash.size";
import TransactionFailed from "./TransactionFailed.js";
import cardValidator from "simple-card-validator";
import * as Cookies from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  COLLECT,
  PRODUCT_CART_ROUTER,
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  OLD_CART_GU_ID,
  PAYMENT_MODE_TYPE,
  PAYTM,
  WALLET,
  HOME_DELIVERY,
  EXPRESS,
  ORDER_PREFIX,
  MY_ACCOUNT,
  ORDER,
  ORDER_CODE,
  REQUESTING,
  THANK_YOU,
  COUPON_COOKIE,
  JUS_PAY_CHARGED,
  JUS_PAY_SUCCESS,
  JUS_PAY_AUTHORIZATION_FAILED,
  CREDIT_CARD,
  NET_BANKING_PAYMENT_MODE,
  DEBIT_CARD,
  EMI,
  CASH_ON_DELIVERY_PAYMENT_MODE,
  LOGIN_PATH,
  NO_COST_EMI_COUPON,
  OLD_CART_CART_ID,
  SAVED_CARD_PAYMENT_MODE,
  E_WALLET,
  NO_COST_EMI,
  STANDARD_EMI,
  CASH_ON_DELIVERY,
  YES,
  NO,
  SAVE_TEXT,
  PINCODE_TEXT,
  NAME_TEXT,
  LAST_NAME_TEXT,
  ADDRESS_TEXT,
  EMAIL_TEXT,
  LANDMARK_TEXT,
  LANDMARK_ENTER_TEXT,
  MOBILE_TEXT,
  PINCODE_VALID_TEXT,
  EMAIL_VALID_TEXT,
  PHONE_VALID_TEXT,
  PHONE_TEXT,
  CITY_TEXT,
  STATE_TEXT,
  SELECT_ADDRESS_TYPE,
  ISO_CODE,
  OTHER_LANDMARK,
  BANK_COUPON_COOKIE,
  SELECTED_BANK_NAME,
  MY_ACCOUNT_CART_PAGE
} from "../../lib/constants";
import {
  EMAIL_REGULAR_EXPRESSION,
  MOBILE_PATTERN
} from "../../auth/components/Login";
import { HOME_ROUTER, SUCCESS, CHECKOUT } from "../../lib/constants";
import SecondaryLoader from "../../general/components/SecondaryLoader";
import {
  setDataLayerForCheckoutDirectCalls,
  ADOBE_LANDING_ON_ADDRESS_TAB_ON_CHECKOUT_PAGE,
  ADOBE_CALL_FOR_SELECT_DELIVERY_MODE,
  ADOBE_CALL_FOR_PROCCEED_FROM_DELIVERY_MODE
} from "../../lib/adobeUtils";
import {
  CART_ITEM_COOKIE,
  ADDRESS_FOR_PLACE_ORDER,
  CART_PATH
} from "../actions/cart.actions";
const SEE_ALL_BANK_OFFERS = "See All Bank Offers";
const PAYMENT_MODE = "EMI";
const NET_BANKING = "NB";
const CART_GU_ID = "cartGuid";
const DELIVERY_MODE_ADDRESS_ERROR = "No Delivery Modes At Selected Address";
const CONTINUE = "Continue";
const PROCEED = "Proceed";
const COUPON_AVAILABILITY_ERROR_MESSAGE = "Your applied coupon has expired";
const PRODUCT_NOT_SERVICEABLE_MESSAGE =
  "Product is not Serviceable,Please try with another pin code";
const SELECT_DELIVERY_MODE_MESSAGE =
  "Please Select the delivery mode for all the products";
const ERROR_MESSAGE_FOR_PICK_UP_PERSON_NAME =
  "Please enter Pickup person name,character should be greater than 4 ";
const ERROR_MESSAGE_FOR_MOBILE_NUMBER = "Please enter valid mobile number";
const INVALID_CART_ERROR_MESSAGE =
  "Sorry your cart is not valid any more. Please Try again";
const PLACE_ORDER = "Place Order";
const PAY_NOW = "Pay Now";
const OUT_OF_STOCK_MESSAGE = "Some Products are out of stock";
export const EGV_GIFT_CART_ID = "giftCartId";
class CheckOutPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isCheckoutAddressSelected: false,
      isDeliveryModeSelected: false,
      confirmAddress: false, //render the render delivery Modes if confirmAddress= true
      isSelectedDeliveryModes: false, // To select the delivery Modes
      deliverMode: false, // render the payment Modes if deliverMode = true
      paymentMethod: false, // render the payment mode if it is true
      addressId: null,
      addNewAddress: false,
      deliverModeUssId: "",
      appliedCoupons: false,
      paymentModeSelected: null,
      orderConfirmation: false,
      showCliqAndPiq: false,
      showPickupPerson: false,
      selectedSlaveIdObj: {},
      ussIdAndDeliveryModesObj: {}, // this object we are using for check when user will continue after  delivery mode then we ll check for all products we selected delivery mode or not
      selectedProductsUssIdForCliqAndPiq: null,
      orderId: "",
      savedCardDetails: "",
      binValidationCOD: false,
      isGiftCard: false,
      isRemainingAmount: true,
      payableAmount: "",
      cliqCashAmount: "",
      userCliqCashAmount: "",
      bagAmount: "",
      selectedDeliveryDetails: null,
      ratingExperience: false,
      isFirstAddress: false,
      addressDetails: null,
      isNoCostEmiApplied: false,
      isNoCostEmiProceeded: false,
      selectedBankOfferCode: "",
      isPaymentFailed: false,
      deliveryCharge: "0.00",
      couponDiscount: "0.00",
      totalDiscount: "0.00",
      cliqPiqSelected: false,
      currentSelectedEMIType: null,
      currentPaymentMode: null, // holding selected payments modes
      cardDetails: {}, // for store card detail in card details
      cvvForCurrentPaymentMode: null, // in case on saved card
      bankCodeForNetBanking: null, // in case on net banking
      captchaReseponseForCOD: null, // in case of COD order its holding that ceptcha verification
      noCostEmiDiscount: "0.00",
      egvCartGuid: null,
      noCostEmiBankName: null,
      isCliqCashApplied: false,
      cliqCashPaidAmount: "0.00",
      showCartDetails: false
    };
  }

  onClickImage(productCode) {
    if (productCode) {
      this.props.history.push(`/p-${productCode.toLowerCase()}`);
    }
  }
  navigateToLogin() {
    const url = this.props.location.pathname;
    this.props.setUrlToRedirectToAfterAuth(url);
    this.props.history.replace(LOGIN_PATH);
  }
  navigateUserToMyBagAfter15MinOfpaymentFailure() {
    this.props.displayToast(INVALID_CART_ERROR_MESSAGE);
    this.props.history.replace(HOME_ROUTER);
  }
  navigateToCartForOutOfStock() {
    this.props.displayToast(OUT_OF_STOCK_MESSAGE);
    this.props.history.push(PRODUCT_CART_ROUTER);
  }
  onChangeCardDetail = val => {
    const cardDetails = cloneDeep(this.state.cardDetails);
    Object.assign(cardDetails, val);
    this.setState({ cardDetails });
  };
  onChangePaymentMode = val => {
    let noCostEmiCouponCode = localStorage.getItem(NO_COST_EMI_COUPON);
    if (
      val.currentPaymentMode !== EMI &&
      val.currentPaymentMode !== null &&
      noCostEmiCouponCode
    ) {
      this.removeNoCostEmi(noCostEmiCouponCode);
    }
    //here we need to reset captch if if already done .but payment mode is changed
    if (this.state.captchaReseponseForCOD) {
      window.grecaptcha.reset();
    }
    this.setState(val);
    this.setState({
      cardDetails: {},
      bankCodeForNetBanking: null,
      savedCardDetails: null,
      captchaReseponseForCOD: null,
      noCostEmiBankName: null,
      noCostEmiDiscount: "0.00",
      isNoCostEmiProceeded: false,
      paymentModeSelected: null,
      binValidationCOD: false
    });
  };
  navigateToJusPayOnGET(url) {
    window.location.replace(url);
  }
  navigateToJusPayOnPOST(juspayResponse) {
    if (
      !juspayResponse ||
      !juspayResponse.payment ||
      !juspayResponse.payment.authentication ||
      !juspayResponse.payment.authentication.url
    ) {
      return this.props.displayToast("Opps some thing went wrong");
    }
    var url = juspayResponse.payment.authentication.url;
    var method = juspayResponse.payment.authentication.method;

    var frm = document.createElement("form");
    frm.style.display = "none"; // ensure that the form is hidden from the user
    frm.setAttribute("method", method);
    frm.setAttribute("action", url);

    if (method === "POST") {
      var params = juspayResponse.payment.authentication.params;
      for (var key in params) {
        var value = params[key];
        var field = document.createElement("input");
        field.setAttribute("type", "hidden");
        field.setAttribute("name", key);
        field.setAttribute("value", value);
        frm.appendChild(field);
      }
    }

    document.body.appendChild(frm);
    // form is now ready
    frm.submit();
  }
  changeSubEmiOption(currentSelectedEMIType) {
    let noCostEmiCouponCode = localStorage.getItem(NO_COST_EMI_COUPON);
    if (noCostEmiCouponCode) {
      this.removeNoCostEmi(noCostEmiCouponCode);
    }

    this.setState({
      currentSelectedEMIType,
      cardDetails: {},
      noCostEmiBankName: null,
      noCostEmiDiscount: "0.00",
      isNoCostEmiApplied: false
    });
  }
  updateLocalStoragePinCode(pincode) {
    const postalCode = parseInt(pincode);
    localStorage.setItem(DEFAULT_PIN_CODE_LOCAL_STORAGE, postalCode);
  }
  navigateToMyBag() {
    if (this.props.displayToast) {
      this.props.displayToast(DELIVERY_MODE_ADDRESS_ERROR);
    }
    this.props.history.goBack();
  }
  selecteBankOffer(couponCode) {
    this.setState({ selectedBankOfferCode: couponCode });
  }
  renderLoader() {
    return (
      <div className={styles.cartLoader}>
        <div className={styles.spinner}>
          <SecondaryLoader />
        </div>
      </div>
    );
  }
  componentDidUpdate() {
    const parsedQueryString = queryString.parse(this.props.location.search);
    const value = parsedQueryString.status;
    if (value && value !== JUS_PAY_CHARGED && value !== JUS_PAY_SUCCESS) {
      const oldCartId = Cookies.getCookie(OLD_CART_GU_ID);
      if (!oldCartId) {
        return this.navigateUserToMyBagAfter15MinOfpaymentFailure();
      }
    }
    if (
      this.props.cart.orderConfirmationDetails ||
      this.props.cart.cliqCashJusPayDetails
    ) {
      this.props.setHeaderText(THANK_YOU);
    } else {
      this.props.setHeaderText(CHECKOUT);
    }
  }

  renderConfirmAddress = () => {
    if (this.state.confirmAddress) {
      return <div> Address Expand</div>;
    }
  };

  handleSelectDeliveryMode(deliveryMode, ussId, cartId) {
    let newDeliveryObj = {};
    newDeliveryObj[ussId] = deliveryMode;
    let currentSelectedDeliveryModes = cloneDeep(
      this.state.ussIdAndDeliveryModesObj
    );

    Object.assign(currentSelectedDeliveryModes, newDeliveryObj);
    setDataLayerForCheckoutDirectCalls(
      ADOBE_CALL_FOR_SELECT_DELIVERY_MODE,
      currentSelectedDeliveryModes
    );
    this.setState({
      ussIdAndDeliveryModesObj: currentSelectedDeliveryModes,
      isSelectedDeliveryModes: true,
      isDeliveryModeSelected: true
    });
  }

  getPaymentFailureOrderDetails = () => {
    if (this.props.getPaymentFailureOrderDetails) {
      this.props.getPaymentFailureOrderDetails();
    }
  };
  getAllStores = selectedProductsUssIdForCliqAndPiq => {
    const defalutPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
    this.setState({ showCliqAndPiq: true, selectedProductsUssIdForCliqAndPiq });
    this.props.getAllStoresCNC(defalutPinCode);
  };
  changePincodeOnCliqAndPiq = pincode => {
    this.updateLocalStoragePinCode(pincode);
    this.props.getAllStoresCNC(pincode);
  };
  togglePickupPersonForm() {
    const currentSelectedSlaveIdObj = cloneDeep(this.state.selectedSlaveIdObj);
    if (
      currentSelectedSlaveIdObj[this.state.selectedProductsUssIdForCliqAndPiq]
    ) {
      delete currentSelectedSlaveIdObj[
        this.state.selectedProductsUssIdForCliqAndPiq
      ];
    }

    this.setState({ selectedSlaveIdObj: currentSelectedSlaveIdObj });
  }
  addStoreCNC(selectedSlaveId) {
    this.handleSelectDeliveryMode(
      COLLECT,
      this.state.selectedProductsUssIdForCliqAndPiq
    );

    const selectedSlaveIdObj = cloneDeep(this.state.selectedSlaveIdObj);
    selectedSlaveIdObj[
      this.state.selectedProductsUssIdForCliqAndPiq
    ] = selectedSlaveId;
    this.setState({ selectedSlaveIdObj });
    this.props.addStoreCNC(
      this.state.selectedProductsUssIdForCliqAndPiq,
      selectedSlaveId
    );
  }
  async addPickupPersonCNC(mobile, name, productObj) {
    if (name.length < 4) {
      return this.props.displayToast(ERROR_MESSAGE_FOR_PICK_UP_PERSON_NAME);
    } else if (mobile.length !== 10) {
      return this.props.displayToast(ERROR_MESSAGE_FOR_MOBILE_NUMBER);
    }
    this.setState({ showCliqAndPiq: false });
    const addPickUpPerson = await this.props.addPickupPersonCNC(mobile, name);
    if (addPickUpPerson.status === SUCCESS) {
      const updatedDeliveryModeUssid = this.state.ussIdAndDeliveryModesObj;

      updatedDeliveryModeUssid[
        this.state.selectedProductsUssIdForCliqAndPiq
      ] = COLLECT;

      this.setState({
        ussIdAndDeliveryModesObj: updatedDeliveryModeUssid,
        cliqPiqSelected: true,
        isDeliveryModeSelected: true
      });
    }
  }
  removeCliqAndPiq() {
    this.setState({ showCliqAndPiq: false });
  }
  renderCheckoutAddress = () => {
    const cartData = this.props.cart;
    let defaultAddressId = null;

    if (this.state.addressId) {
      defaultAddressId = this.state.addressId;
    }
    return (
      <div className={styles.addInitialAddAddress}>
        <ConfirmAddress
          address={
            cartData.userAddress.addresses &&
            cartData.userAddress.addresses.map(address => {
              return {
                addressTitle: address.addressType,
                addressDescription: `${address.line1 ? address.line1 : ""} ${
                  address.line2 ? address.line2 : ""
                }  ${address.state ? address.state : ""} ${
                  address.postalCode ? address.postalCode : ""
                }`,
                value: address.id,
                selected: address.defaultAddress
              };
            })
          }
          selected={[defaultAddressId]}
          onNewAddress={() => this.addNewAddress()}
          onSelectAddress={address => this.onSelectAddress(address)}
        />
      </div>
    );
  };
  onFocusInput() {
    this.setState({ showCartDetails: false });
  }
  renderDeliverModes = () => {
    return (
      <div className={styles.products}>
        <div className={styles.header}>
          <CheckOutHeader indexNumber="2" confirmTitle="Choose Delivery Mode" />
        </div>
        {this.props.cart.cartDetailsCNC.products &&
          this.props.cart.cartDetailsCNC.products.map((val, i) => {
            return (
              <div className={styles.row}>
                <CartItem
                  key={i}
                  selected={this.state.ussIdAndDeliveryModesObj[val.USSID]}
                  productImage={val.imageURL}
                  hasFooter={false}
                  size={val.size}
                  color={val.color}
                  isGiveAway={val.isGiveAway}
                  //productDetails={val.productBrand}
                  productName={val.productName}
                  price={val.price}
                  offerPrice={val.offerPrice}
                  deliveryInformation={val.elligibleDeliveryMode}
                  showDelivery={true}
                  deliveryInfoToggle={false}
                  productIsServiceable={val.pinCodeResponse}
                  selectDeliveryMode={code =>
                    this.handleSelectDeliveryMode(
                      code,
                      val.USSID,
                      this.state.cartId
                    )
                  }
                  onPiq={() => this.getAllStores(val.USSID)}
                  onClickImage={() => this.onClickImage(val.productcode)}
                  isClickable={true}
                />
              </div>
            );
          })}
      </div>
    );
  };

  getUserDetails = () => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (userDetails && customerCookie) {
      if (this.props.getUserDetails) {
        this.props.getUserDetails();
      }
    }
  };

  renderCliqAndPiq() {
    let currentSelectedProduct = this.props.cart.cartDetailsCNC.products.find(
      product => {
        return product.USSID === this.state.selectedProductsUssIdForCliqAndPiq;
      }
    );

    const firstSlaveData =
      currentSelectedProduct.pinCodeResponse.validDeliveryModes;
    const someData = firstSlaveData
      .map(slaves => {
        return (
          slaves.CNCServiceableSlavesData &&
          slaves.CNCServiceableSlavesData.map(slave => {
            return (
              slave &&
              slave.serviceableSlaves.map(serviceableSlave => {
                return serviceableSlave;
              })
            );
          })
        );
      })
      .map(val => {
        return (
          val &&
          val.map(v => {
            return v;
          })
        );
      });

    const allStoreIds = [].concat
      .apply([], [].concat.apply([], someData))
      .map(store => {
        return store && store.slaveId;
      });
    const availableStores = this.props.cart.storeDetails
      ? this.props.cart.storeDetails.filter(val => {
          return allStoreIds.includes(val.slaveId);
        })
      : [];
    return (
      <PiqPage
        availableStores={availableStores}
        selectedSlaveId={
          this.state.selectedSlaveIdObj[
            this.state.selectedProductsUssIdForCliqAndPiq
          ] &&
          this.state.selectedSlaveIdObj[
            this.state.selectedProductsUssIdForCliqAndPiq
          ]
        }
        pinCodeUpdateDisabled={true}
        numberOfStores={availableStores.length}
        showPickupPerson={
          this.state.selectedSlaveIdObj[
            this.state.selectedProductsUssIdForCliqAndPiq
          ]
            ? true
            : false
        }
        productName={currentSelectedProduct.productName}
        productColour={currentSelectedProduct.color}
        hidePickupPersonDetail={() => this.togglePickupPersonForm()}
        addStoreCNC={slavesId => this.addStoreCNC(slavesId)}
        addPickupPersonCNC={(mobile, name) =>
          this.addPickupPersonCNC(mobile, name, currentSelectedProduct)
        }
        changePincode={pincode => this.changePincodeOnCliqAndPiq(pincode)}
        goBack={() => this.removeCliqAndPiq()}
        getUserDetails={() => this.getUserDetails()}
        userDetails={this.props.userDetails}
      />
    );
  }
  renderPaymentModes = () => {
    if (this.state.paymentMethod) {
      return <div>Payment Modes Expand</div>;
    }
  };

  renderInitialAddAddressForm() {
    if (
      !this.state.isFirstAddress &&
      this.props.cart.userAddress &&
      !this.props.cart.userAddress.addresses
    ) {
      this.setState({ isFirstAddress: true });
    }

    return (
      <div className={styles.addInitialAddAddress}>
        <AddDeliveryAddress
          addUserAddress={address => this.addAddress(address)}
          {...this.state}
          onChange={val => this.onChange(val)}
          isFirstAddress={true}
          showSecondaryLoader={this.props.showSecondaryLoader}
          hideSecondaryLoader={this.props.hideSecondaryLoader}
          loading={this.props.cart.loading}
          displayToast={message => this.props.displayToast(message)}
          getAddressDetails={val => this.setState({ addressDetails: val })}
          getPinCode={val => this.getPinCodeDetails(val)}
          getPinCodeDetails={this.props.getPinCodeDetails}
          resetAutoPopulateDataForPinCode={() =>
            this.props.resetAutoPopulateDataForPinCode()
          }
          onFocusInput={() => this.onFocusInput()}
          getPincodeStatus={this.props.getPincodeStatus}
          resetAddAddressDetails={() => this.props.resetAddAddressDetails()}
          getUserDetails={() => this.getUserDetails()}
          userDetails={this.props.userDetails}
          clearPinCodeStatus={() => this.props.clearPinCodeStatus()}
        />
      </div>
    );
  }
  changeDeliveryAddress = () => {
    let noCostEmiCouponCode = localStorage.getItem(NO_COST_EMI_COUPON);
    if (this.state.captchaReseponseForCOD) {
      window.grecaptcha.reset();
    }
    if (this.state.isCliqCashApplied) {
      this.removeCliqCash();
    }
    if (noCostEmiCouponCode) {
      this.removeNoCostEmi(noCostEmiCouponCode);
    }
    this.setState({
      cardDetails: {},
      bankCodeForNetBanking: null,
      savedCardDetails: null,
      captchaReseponseForCOD: null,
      noCostEmiBankName: null,
      noCostEmiDiscount: "0.00",
      isNoCostEmiProceeded: false,
      paymentModeSelected: null,
      binValidationCOD: false,
      confirmAddress: false,
      deliverMode: false,
      isSelectedDeliveryModes: false,
      currentPaymentMode: null,
      isCliqCashApplied: false
    });
  };

  componentWillReceiveProps(nextProps) {
    if (nextProps.cart.isSoftReservationFailed) {
      return this.navigateToCartForOutOfStock();
    }
    if (nextProps.cart.jusPayError && this.state.isPaymentFailed === false) {
      const oldCartId = Cookies.getCookie(OLD_CART_GU_ID);
      if (!oldCartId) {
        return this.navigateUserToMyBagAfter15MinOfpaymentFailure();
      }
      this.setState({ isPaymentFailed: true });
      this.props.getPaymentFailureOrderDetails();

      if (localStorage.getItem(EGV_GIFT_CART_ID)) {
        let giftCartObj = JSON.parse(localStorage.getItem(EGV_GIFT_CART_ID));
        this.setState({
          isGiftCard: true,
          isRemainingAmount: true,
          payableAmount: Math.round(giftCartObj.amount * 100) / 100,
          bagAmount: Math.round(giftCartObj.amount * 100) / 100,
          egvCartGuid: giftCartObj.egvCartGuid
        });
      }
    }
    this.availabilityOfUserCoupon();
    if (
      !this.state.isCheckoutAddressSelected &&
      nextProps.cart.getUserAddressStatus === SUCCESS &&
      nextProps.cart &&
      nextProps.cart.userAddress &&
      nextProps.cart.userAddress.addresses
    ) {
      let defaultAddressId = null;
      let defaultAddress;
      if (this.state.isFirstAddress) {
        defaultAddress = nextProps.cart.userAddress.addresses[0];
        this.setState({ isFirstAddress: false, confirmAddress: true });
        this.props.addAddressToCart(
          defaultAddress.id,
          defaultAddress.postalCode
        );
      } else {
        defaultAddress = nextProps.cart.userAddress.addresses.find(address => {
          return address.defaultAddress;
        });
      }

      if (defaultAddress) {
        defaultAddressId = defaultAddress.id;
      }
      this.updateLocalStoragePinCode(defaultAddress.postalCode);
      this.setState({
        addressId: defaultAddressId,
        selectedAddress: defaultAddress
      });
    }
    // end of adding default address is selected

    // adding selected default delivery modes for every product

    if (
      !this.state.isDeliveryModeSelected &&
      !this.state.isSelectedDeliveryModes &&
      nextProps.cart.cartDetailsCNCStatus === SUCCESS &&
      nextProps.cart &&
      nextProps.cart.cartDetailsCNC
    ) {
      let defaultSelectedDeliveryModes = {};
      if (
        nextProps.cart.cartDetailsCNC &&
        nextProps.cart.cartDetailsCNC.products
      ) {
        nextProps.cart.cartDetailsCNC.products.forEach(product => {
          if (product.isGiveAway === NO) {
            if (
              product.elligibleDeliveryMode &&
              product.elligibleDeliveryMode.findIndex(mode => {
                return mode.code === EXPRESS;
              }) >= 0
            ) {
              let newObjectAdd = {};
              newObjectAdd[product.USSID] = EXPRESS;
              Object.assign(defaultSelectedDeliveryModes, newObjectAdd);
            } else if (
              product.elligibleDeliveryMode &&
              product.elligibleDeliveryMode.findIndex(mode => {
                return mode.code === HOME_DELIVERY;
              }) >= 0
            ) {
              let newObjectAdd = {};
              newObjectAdd[product.USSID] = HOME_DELIVERY;
              Object.assign(defaultSelectedDeliveryModes, newObjectAdd);
            }
          }
        });
      }
      if (
        this.props.cart.cartDetailsCNC &&
        this.state.confirmAddress &&
        !this.state.deliverMode &&
        !this.state.isGiftCard &&
        !this.state.showCliqAndPiq
      ) {
        setDataLayerForCheckoutDirectCalls(
          ADOBE_CALL_FOR_SELECT_DELIVERY_MODE,
          defaultSelectedDeliveryModes
        );
      }

      this.setState({ ussIdAndDeliveryModesObj: defaultSelectedDeliveryModes });
    }
    // end if adding selected default delivery modes for every product

    if (nextProps.cart.cliqCashPaymentDetails && !this.state.isPaymentFailed) {
      this.setState({
        isRemainingAmount:
          nextProps.cart.cliqCashPaymentDetails.isRemainingAmount,
        payableAmount: nextProps.cart.cartDetailsCNC.cartAmount.paybleAmount
          .value
          ? Math.round(
              nextProps.cart.cartDetailsCNC.cartAmount.paybleAmount.value * 100
            ) / 100
          : "0.00",
        cliqCashAmount:
          nextProps.cart.cliqCashPaymentDetails.cliqCashBalance.value > 0
            ? Math.round(
                nextProps.cart.cliqCashPaymentDetails.cliqCashBalance.value *
                  100
              ) / 100
            : "0.00",
        bagAmount: nextProps.cart.cartDetailsCNC.cartAmount.bagTotal.value
          ? Math.round(
              nextProps.cart.cartDetailsCNC.cartAmount.bagTotal.value * 100
            ) / 100
          : "0.00",
        totalDiscount:
          nextProps.cart.cartDetailsCNC.cartAmount.totalDiscountAmount.value > 0
            ? Math.round(
                nextProps.cart.cartDetailsCNC.cartAmount.totalDiscountAmount
                  .value * 100
              ) / 100
            : "0.00",
        cliqCashPaidAmount:
          nextProps.cart.cliqCashPaymentDetails.cliqCashPaidAmount.value > 0
            ? Math.round(
                nextProps.cart.cliqCashPaymentDetails.cliqCashPaidAmount.value *
                  100
              ) / 100
            : "0.00"
      });
    } else {
      if (nextProps.cart.cartDetailsCNC && this.state.isRemainingAmount) {
        let cliqCashAmount = 0;
        if (
          nextProps.cart.paymentModes &&
          nextProps.cart.paymentModes.cliqCash &&
          nextProps.cart.paymentModes.cliqCash.totalCliqCashBalance
        ) {
          cliqCashAmount = nextProps.cart.paymentModes.cliqCash
            .totalCliqCashBalance.value
            ? Math.round(
                nextProps.cart.paymentModes.cliqCash.totalCliqCashBalance
                  .value * 100
              ) / 100
            : "0.00";
        }
        if (
          this.props.cart &&
          this.props.cart.emiEligibilityDetails &&
          this.props.cart.emiEligibilityDetails.isNoCostEMIEligible &&
          nextProps.cart.cartDetailsCNC.cartAmount &&
          nextProps.cart.cartDetailsCNC.cartAmount.noCostEMIDiscountValue
        ) {
          this.setState({
            noCostEmiDiscount: nextProps.cart.cartDetailsCNC.cartAmount
              .noCostEMIDiscountValue.value
              ? Math.round(
                  nextProps.cart.cartDetailsCNC.cartAmount
                    .noCostEMIDiscountValue.value * 100
                ) / 100
              : "0.00"
          });
        } else {
          this.setState({
            noCostEmiDiscount: "0.00"
          });
        }

        this.setState({
          payableAmount: nextProps.cart.cartDetailsCNC.cartAmount.paybleAmount
            .value
            ? Math.round(
                nextProps.cart.cartDetailsCNC.cartAmount.paybleAmount.value *
                  100
              ) / 100
            : "0.00",
          cliqCashAmount: cliqCashAmount,
          userCliqCashAmount: cliqCashAmount,
          bagAmount: nextProps.cart.cartDetailsCNC.cartAmount.bagTotal.value
            ? Math.round(
                nextProps.cart.cartDetailsCNC.cartAmount.bagTotal.value * 100
              ) / 100
            : "0.00",

          totalDiscount:
            nextProps.cart.cartDetailsCNC.cartAmount.totalDiscountAmount.value >
            0
              ? Math.round(
                  nextProps.cart.cartDetailsCNC.cartAmount.totalDiscountAmount
                    .value * 100
                ) / 100
              : "0.00"
        });

        if (
          !this.state.isPaymentFailed &&
          nextProps.cart.cartDetailsCNC &&
          nextProps.cart.cartDetailsCNC.deliveryCharge
        ) {
          this.setState({
            deliveryCharge:
              nextProps.cart.cartDetailsCNC &&
              nextProps.cart.cartDetailsCNC.deliveryCharge
          });
        } else {
          this.setState({
            deliveryCharge:
              nextProps.cart.paymentFailureOrderDetails &&
              nextProps.cart.paymentFailureOrderDetails.deliveryCharges &&
              nextProps.cart.paymentFailureOrderDetails.deliveryCharges.value
                ? Math.round(
                    nextProps.cart.paymentFailureOrderDetails.deliveryCharges
                      .value * 100
                  ) / 100
                : "0.00"
          });
        }

        if (
          this.state.isPaymentFailed &&
          nextProps.cart.paymentFailureOrderDetails
        ) {
          this.setState({
            isCliqCashApplied:
              nextProps.cart.paymentFailureOrderDetails.cliqCashApplied,
            cliqCashPaidAmount:
              nextProps.cart.paymentFailureOrderDetails.cliqCashPaidAmount.value
          });
        }
        if (
          nextProps.cart.cartDetailsCNC.cartAmount.couponDiscountAmount &&
          nextProps.cart.cartDetailsCNC.cartAmount.couponDiscountAmount.value
        ) {
          this.setState({
            couponDiscount: nextProps.cart.cartDetailsCNC.cartAmount
              .couponDiscountAmount
              ? Math.round(
                  nextProps.cart.cartDetailsCNC.cartAmount.couponDiscountAmount
                    .value * 100
                ) / 100
              : "0.00"
          });
        }
        if (
          nextProps.cart.cartDetailsCNC.cartAmount.couponDiscountAmount &&
          nextProps.cart.cartDetailsCNC.cartAmount.couponDiscountAmount.value
        ) {
          this.setState({
            couponDiscount: nextProps.cart.cartDetailsCNC.cartAmount
              .couponDiscountAmount
              ? Math.round(
                  nextProps.cart.cartDetailsCNC.cartAmount.couponDiscountAmount
                    .value * 100
                ) / 100
              : "0.00"
          });
        }
      }
    }

    if (nextProps.cart.justPayPaymentDetails !== null) {
      if (nextProps.cart.justPayPaymentDetails.payment) {
        if (
          nextProps.cart.justPayPaymentDetails &&
          nextProps.cart.justPayPaymentDetails.payment &&
          nextProps.cart.justPayPaymentDetails.payment.authentication &&
          nextProps.cart.justPayPaymentDetails.payment.authentication.method ===
            "GET"
        ) {
          if (nextProps.cart.justPayPaymentDetails.payment.authentication.url) {
            this.navigateToJusPayOnGET(
              nextProps.cart.justPayPaymentDetails.payment.authentication.url
            );
          } else {
            this.props.displayToast("Opps Some thing went wrong");
          }
        } else if (
          nextProps.cart.justPayPaymentDetails &&
          nextProps.cart.justPayPaymentDetails.payment &&
          nextProps.cart.justPayPaymentDetails.payment.authentication &&
          nextProps.cart.justPayPaymentDetails.payment.authentication.method ===
            "POST"
        ) {
          var juspayResponse = nextProps.cart.justPayPaymentDetails; // assuming that `res` holds the data return by JusPay API
          this.navigateToJusPayOnPOST(juspayResponse);
        } else {
          window.location.replace(
            nextProps.cart.justPayPaymentDetails.payment.authentication.url
          );
        }
      }
    }
    if (nextProps.cart.orderConfirmationDetailsStatus === SUCCESS) {
      this.setState({ orderConfirmation: true });
    }
    if (
      nextProps.cart.cliqCashJusPayDetails &&
      nextProps.cart.orderConfirmationDetailsStatus !== "requesting"
    ) {
      this.setState({ orderId: nextProps.cart.cliqCashJusPayDetails.orderId });
      this.props.orderConfirmation(
        nextProps.cart.cliqCashJusPayDetails.orderId
      );
    }
  }

  componentWillUnmount() {
    // if user go back from checkout page then
    // we have relsease coupon if user applied any coupon
    if (
      this.props.history.action === "POP" &&
      this.state.selectedBankOfferCode
    ) {
      this.props.releaseBankOffer(this.state.selectedBankOfferCode);
    }
    if (this.props.history.action === "POP") {
      this.props.clearCartDetails();
    }
    this.props.resetIsSoftReservationFailed();
  }
  componentDidMount() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    if (!customerCookie || !userDetails) {
      return this.navigateToLogin();
    }
    setDataLayerForCheckoutDirectCalls(
      ADOBE_LANDING_ON_ADDRESS_TAB_ON_CHECKOUT_PAGE
    );
    const parsedQueryString = queryString.parse(this.props.location.search);
    const value = parsedQueryString.status;
    const orderId = parsedQueryString.order_id;
    this.setState({ orderId: orderId });
    if (value && value !== JUS_PAY_CHARGED && value !== JUS_PAY_SUCCESS) {
      const oldCartId = Cookies.getCookie(OLD_CART_GU_ID);
      if (!oldCartId) {
        return this.navigateUserToMyBagAfter15MinOfpaymentFailure();
      }
      this.setState({ isPaymentFailed: true });
      this.props.getPaymentFailureOrderDetails();
      if (localStorage.getItem(EGV_GIFT_CART_ID)) {
        let giftCartObj = JSON.parse(localStorage.getItem(EGV_GIFT_CART_ID));
        this.setState({
          isGiftCard: true,
          isRemainingAmount: true,
          payableAmount: Math.round(giftCartObj.amount * 100) / 100,
          bagAmount: Math.round(giftCartObj.amount * 100) / 100,
          egvCartGuid: giftCartObj.egvCartGuid
        });
      }
      this.getPaymentModes();
    } else if (value === JUS_PAY_CHARGED) {
      if (this.props.updateTransactionDetails) {
        const cartId = parsedQueryString.value;

        if (cartId) {
          this.props.updateTransactionDetails(
            localStorage.getItem(PAYMENT_MODE_TYPE),
            orderId,
            cartId
          );
        }
      }
    } else if (
      this.props.location &&
      this.props.location.state &&
      this.props.location.state.isFromGiftCard &&
      this.props.location.state.amount
    ) {
      this.getPaymentModes();
      this.setState({
        isGiftCard: true,
        isRemainingAmount: true,
        payableAmount: Math.round(this.props.location.state.amount * 100) / 100,
        bagAmount: Math.round(this.props.location.state.amount * 100) / 100
      });
    } else {
      if (this.props.getCartDetailsCNC) {
        let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
        let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
        let cartDetailsLoggedInUser = Cookie.getCookie(
          CART_DETAILS_FOR_LOGGED_IN_USER
        );

        if (
          userDetails &&
          customerCookie &&
          cartDetailsLoggedInUser &&
          !this.state.isPaymentFailed
        ) {
          this.props.getCartDetailsCNC(
            JSON.parse(userDetails).userName,
            JSON.parse(customerCookie).access_token,
            JSON.parse(cartDetailsLoggedInUser).code,
            localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE),
            false
          );
        }
        if (!this.state.isPaymentFailed) {
          this.props.getUserAddress(
            localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
          );
        }
      }
    }
    if (this.props.location.state && this.props.location.state.egvCartGuid) {
      localStorage.setItem(
        EGV_GIFT_CART_ID,
        JSON.stringify(this.props.location.state)
      );
    }
  }

  getEmiBankDetails = () => {
    if (this.props.getEmiBankDetails) {
      if (this.state.isPaymentFailed) {
        this.props.getEmiBankDetails(
          this.props.cart.paymentFailureOrderDetails &&
            this.props.cart.paymentFailureOrderDetails.cartAmount &&
            this.props.cart.paymentFailureOrderDetails.cartAmount.paybleAmount
              .value
        );
      } else {
        this.props.getEmiBankDetails(
          this.props.cart.cartDetailsCNC.cartAmount &&
            this.props.cart.cartDetailsCNC.cartAmount.paybleAmount.value
        );
      }
    }
  };

  getEmiEligibility = () => {
    let carGuId;
    const parsedQueryString = queryString.parse(this.props.location.search);
    const value = parsedQueryString.status;

    if (value && value !== JUS_PAY_CHARGED && value !== JUS_PAY_SUCCESS) {
      carGuId = parsedQueryString.value;

      //get the NoCost Emi Coupon Code to release
      let noCostEmiCouponCode = localStorage.getItem(NO_COST_EMI_COUPON);
      let cartId = localStorage.getItem(OLD_CART_CART_ID);
      //  this.props.removeNoCostEmi(noCostEmiCouponCode, carGuId, cartId);
    } else {
      let cartDetailsLoggedInUser = Cookie.getCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER
      );
      localStorage.setItem(
        OLD_CART_CART_ID,
        JSON.parse(cartDetailsLoggedInUser).code
      );

      if (cartDetailsLoggedInUser) {
        carGuId = JSON.parse(cartDetailsLoggedInUser).guid;
      }
    }
    if (this.props.getEmiEligibility) {
      this.setState({ isNoCostEmiApplied: false, isNoCostEmiProceeded: false });
      this.props.getEmiEligibility(carGuId);
    }
  };

  getBankAndTenureDetails = () => {
    if (this.props.getBankAndTenureDetails) {
      this.setState({ isNoCostEmiApplied: false, isNoCostEmiProceeded: false });
      this.props.getBankAndTenureDetails();
    }
  };

  getEmiTermsAndConditionsForBank = (bankCode, bankName) => {
    if (this.props.getEmiTermsAndConditionsForBank) {
      this.props.getEmiTermsAndConditionsForBank(bankCode, bankName);
    }
  };
  applyNoCostEmi = async (couponCode, bankName) => {
    if (this.state.isPaymentFailed) {
      const parsedQueryString = queryString.parse(this.props.location.search);
      const cartGuId = parsedQueryString.value;
      const cartId = localStorage.getItem(OLD_CART_CART_ID);
      if (this.props.applyNoCostEmi) {
        const applyNoCostEmiResponse = await this.props.applyNoCostEmi(
          couponCode,
          cartGuId,
          cartId
        );
        if (applyNoCostEmiResponse.status === SUCCESS) {
          this.setState({
            isNoCostEmiApplied: true,
            isNoCostEmiProceeded: false,
            noCostEmiBankName: bankName
          });
        }
        return applyNoCostEmiResponse;
      }
    } else {
      let cartDetailsLoggedInUser = Cookie.getCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER
      );
      if (cartDetailsLoggedInUser) {
        let carGuId = JSON.parse(cartDetailsLoggedInUser).guid;
        let cartId = JSON.parse(cartDetailsLoggedInUser).code;
        if (this.props.applyNoCostEmi) {
          let applyNoCostEmiResponse = await this.props.applyNoCostEmi(
            couponCode,
            carGuId,
            cartId
          );
          if (applyNoCostEmiResponse.status === SUCCESS) {
            this.setState({
              isNoCostEmiApplied: true,
              isNoCostEmiProceeded: false,
              noCostEmiBankName: bankName
            });
          }
          return applyNoCostEmiResponse;
        }
      }
    }
  };

  removeNoCostEmi = async couponCode => {
    if (this.state.isPaymentFailed) {
      const parsedQueryString = queryString.parse(this.props.location.search);
      const cartGuId = parsedQueryString.value;
      const cartId = localStorage.getItem(OLD_CART_CART_ID);
      if (this.props.applyNoCostEmi) {
        this.props.applyNoCostEmi(couponCode, cartGuId, cartId);
      }
    } else {
      let cartDetailsLoggedInUser = Cookie.getCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER
      );

      if (this.props.removeNoCostEmi) {
        let carGuId = JSON.parse(cartDetailsLoggedInUser).guid;
        let cartId = JSON.parse(cartDetailsLoggedInUser).code;
        const removeNoCostEmiResponse = await this.props.removeNoCostEmi(
          couponCode,
          carGuId,
          cartId
        );
        if (removeNoCostEmiResponse.status === SUCCESS) {
          this.setState({
            isNoCostEmiApplied: false,
            isNoCostEmiProceeded: false,
            noCostEmiBankName: null,
            noCostEmiDiscount: "0.00"
          });
        }
        return removeNoCostEmiResponse;
      }
    }
  };

  getItemBreakUpDetails = (couponCode, noCostEmiText, noCostProductCount) => {
    if (this.state.isPaymentFailed) {
      const parsedQueryString = queryString.parse(this.props.location.search);
      const cartGuId = parsedQueryString.value;
      this.props.getItemBreakUpDetails(
        couponCode,
        cartGuId,
        noCostEmiText,
        noCostProductCount
      );
    } else {
      if (this.props.getItemBreakUpDetails) {
        this.props.getItemBreakUpDetails(
          couponCode,
          null,
          noCostEmiText,
          noCostProductCount
        );
      }
    }
  };

  getNetBankDetails = () => {
    if (this.props.getNetBankDetails) {
      this.props.getNetBankDetails();
    }
  };

  getCODEligibility = cartId => {
    if (this.props.getCODEligibility) {
      this.props.getCODEligibility(this.state.isPaymentFailed);
    }
  };

  getPaymentModes = () => {
    if (
      (this.props.location &&
        this.props.location.state &&
        this.props.location.state.egvCartGuid) ||
      (this.state.isGiftCard && this.state.egvCartGuid)
    ) {
      let egvGiftCartGuId;
      if (this.state.egvCartGuid) {
        egvGiftCartGuId = this.state.egvCartGuid;
      } else {
        egvGiftCartGuId = this.props.location.state.egvCartGuid;
      }
      this.props.getPaymentModes(egvGiftCartGuId);
    } else {
      let cartGuId;
      const parsedQueryString = queryString.parse(this.props.location.search);
      if (parsedQueryString.value) {
        cartGuId = parsedQueryString.value;
      } else {
        let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
        cartGuId = JSON.parse(cartDetails).guid;
      }
      this.props.getPaymentModes(cartGuId);
    }
  };
  onSelectAddress(selectedAddress) {
    let addressSelected = find(
      this.props.cart.cartDetailsCNC &&
        this.props.cart.cartDetailsCNC.addressDetailsList &&
        this.props.cart.cartDetailsCNC.addressDetailsList.addresses,
      address => {
        return address.id === selectedAddress[0];
      }
    );
    this.updateLocalStoragePinCode(
      addressSelected && addressSelected.postalCode
    );
    // here we are checking the if user selected any address then setting our state
    // and in else condition if user deselect then this function will again call and
    //  then we are resetting the previous selected address
    if (selectedAddress[0]) {
      this.setState({
        confirmAddress: false,
        selectedAddress: addressSelected,
        isCheckoutAddressSelected: true,
        addressId: addressSelected.id,
        isDeliveryModeSelected: false
      });
    } else {
      this.setState({
        addressId: null,
        selectedAddress: null,
        isDeliveryModeSelected: false
      });
    }
  }
  changeDeliveryModes = () => {
    let noCostEmiCouponCode = localStorage.getItem(NO_COST_EMI_COUPON);
    if (this.state.captchaReseponseForCOD) {
      window.grecaptcha.reset();
    }
    if (this.state.isCliqCashApplied) {
      this.removeCliqCash();
    }
    if (noCostEmiCouponCode) {
      this.removeNoCostEmi(noCostEmiCouponCode);
    }
    this.setState({
      cardDetails: {},
      bankCodeForNetBanking: null,
      savedCardDetails: null,
      captchaReseponseForCOD: null,
      noCostEmiBankName: null,
      noCostEmiDiscount: "0.00",
      isNoCostEmiProceeded: false,
      paymentModeSelected: null,
      binValidationCOD: false,
      deliverMode: false,
      currentPaymentMode: null,
      isCliqCashApplied: false
    });
  };

  onChange(val) {
    this.setState(val);
  }

  getPinCodeDetails = pinCode => {
    if (this.props.getPinCode) {
      this.props.getPinCode(pinCode);
    }
  };

  availabilityOfUserCoupon = () => {
    if (!this.state.isGiftCard) {
      let couponCookie = Cookie.getCookie(COUPON_COOKIE);
      let cartDetailsCouponDiscount;
      if (
        this.props.cart &&
        this.props.cart.cartDetailsCNC &&
        this.props.cart.cartDetailsCNC.cartAmount &&
        (this.props.cart.cartDetailsCNC.cartAmount.couponDiscountAmount ||
          this.props.cart.cartDetailsCNC.cartAmount.appliedCouponDiscount)
      ) {
        cartDetailsCouponDiscount = true;
      } else {
        cartDetailsCouponDiscount = false;
      }

      if (
        couponCookie &&
        !cartDetailsCouponDiscount &&
        this.props.cart.cartDetailsCNCStatus === SUCCESS
      ) {
        Cookies.deleteCookie(COUPON_COOKIE);
        this.props.displayToast(COUPON_AVAILABILITY_ERROR_MESSAGE);
      }
    }
  };

  checkAvailabilityOfService = () => {
    let productServiceAvailability = find(
      this.props.cart.cartDetailsCNC.products,
      product => {
        return (
          product.isGiveAway === NO &&
          (product.pinCodeResponse === undefined ||
            (product.pinCodeResponse &&
              product.pinCodeResponse.isServicable === "N"))
        );
      }
    );

    return productServiceAvailability;
  };
  handleSubmitAfterPaymentFailure = () => {
    if (this.state.isNoCostEmiApplied) {
      this.setState({ isNoCostEmiProceeded: true });
    }
    // navigate user to myBag page is old cart dose not exist
    const oldCartId = Cookies.getCookie(OLD_CART_GU_ID);
    if (!oldCartId) {
      return this.navigateUserToMyBagAfter15MinOfpaymentFailure();
    }
    if (this.state.savedCardDetails !== "") {
      if (this.state.isGiftCard) {
        this.props.createJusPayOrderForGiftCardFromSavedCards(
          this.state.savedCardDetails,
          this.state.egvCartGuid
        );
      } else {
        this.props.createJusPayOrderForSavedCards(
          this.state.savedCardDetails,
          JSON.parse(localStorage.getItem(CART_ITEM_COOKIE)),
          true // for payment failure we need to use old cart id);
        );
      }
    }
    if (
      this.state.currentPaymentMode === CREDIT_CARD ||
      (this.state.currentPaymentMode === EMI &&
        !this.state.isNoCostEmiApplied) ||
      this.state.currentPaymentMode === DEBIT_CARD
    ) {
      if (this.state.isGiftCard) {
        this.props.jusPayTokenizeForGiftCard(
          this.state.cardDetails,
          this.state.paymentModeSelected,
          this.state.egvCartGuid
        );
      } else {
        this.props.jusPayTokenize(
          this.state.cardDetails,
          JSON.parse(localStorage.getItem(ADDRESS_FOR_PLACE_ORDER)),
          JSON.parse(localStorage.getItem(CART_ITEM_COOKIE)),
          this.state.paymentModeSelected,
          true
        );
      }
    }

    if (
      this.state.currentPaymentMode === EMI &&
      this.state.isNoCostEmiProceeded
    ) {
      this.props.createJusPayOrder(
        "",
        JSON.parse(localStorage.getItem(CART_ITEM_COOKIE)),
        JSON.parse(localStorage.getItem(ADDRESS_FOR_PLACE_ORDER)),
        this.state.cardDetails,
        this.state.paymentModeSelected
      );
    }
    if (this.state.currentPaymentMode === NET_BANKING_PAYMENT_MODE) {
      if (this.state.isGiftCard) {
        if (this.props.createJusPayOrderForGiftCardNetBanking) {
          this.props.createJusPayOrderForGiftCardNetBanking(
            this.state.egvCartGuid
          );
        }
      } else {
        this.props.createJusPayOrderForNetBanking(
          NET_BANKING,
          JSON.parse(localStorage.getItem(CART_ITEM_COOKIE)),
          localStorage.getItem(SELECTED_BANK_NAME),
          localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        );
      }
    }
    if (this.state.paymentModeSelected === PAYTM) {
      if (this.state.isGiftCard) {
        if (this.props.createJusPayOrderForGiftCardNetBanking) {
          this.props.createJusPayOrderForGiftCardNetBanking(
            this.state.egvCartGuid
          );
        }
      } else {
        this.props.createJusPayOrderForNetBanking(
          WALLET,
          localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE),
          JSON.parse(localStorage.getItem(CART_ITEM_COOKIE))
        );
      }
    }
    if (!this.state.isRemainingAmount && this.state.isCliqCashApplied) {
      this.props.createJusPayOrderForCliqCash(
        localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE),
        true // for payment failure we need to use old cart id
      );
    }

    if (this.state.binValidationCOD && !this.state.isCliqCashApplied) {
      this.props.updateTransactionDetailsForCOD(CASH_ON_DELIVERY, "");
    }
    if (!this.state.isNoCostEmiApplied) {
      this.onChangePaymentMode({ currentPaymentMode: null });
    }
  };
  handleSubmit = () => {
    localStorage.setItem(
      ADDRESS_FOR_PLACE_ORDER,
      JSON.stringify(this.state.selectedAddress)
    );
    if (!this.state.isPaymentFailed) {
      if (this.state.isFirstAddress) {
        this.addAddress(this.state.addressDetails);
      }
      if (
        !this.state.confirmAddress &&
        !this.state.isGiftCard &&
        this.state.addressId &&
        this.state.selectedAddress.postalCode
      ) {
        this.props.addAddressToCart(
          this.state.addressId,
          this.state.selectedAddress.postalCode
        );
        this.setState({ confirmAddress: true });
      }
      if (
        !this.state.deliverMode &&
        this.state.confirmAddress &&
        !this.state.isGiftCard
      ) {
        if (
          this.props.selectDeliveryMode &&
          !this.checkAvailabilityOfService()
        ) {
          let sizeNew = size(this.state.ussIdAndDeliveryModesObj);
          let actualProductSize = this.props.cart.cartDetailsCNC.products.filter(
            product => {
              return product.isGiveAway === NO;
            }
          ).length;
          if (sizeNew === actualProductSize) {
            this.props.selectDeliveryMode(
              this.state.ussIdAndDeliveryModesObj,
              localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
            );
            this.setState({
              deliverMode: true
            });
          } else {
            this.props.displayToast(SELECT_DELIVERY_MODE_MESSAGE);
          }

          setDataLayerForCheckoutDirectCalls(
            ADOBE_CALL_FOR_PROCCEED_FROM_DELIVERY_MODE,
            this.state.ussIdAndDeliveryModesObj
          );
        } else {
          if (this.props.displayToast) {
            this.props.displayToast(PRODUCT_NOT_SERVICEABLE_MESSAGE);
          }
        }
      }

      if (this.state.savedCardDetails && this.state.savedCardDetails !== "") {
        if (this.state.isGiftCard) {
          this.props.createJusPayOrderForGiftCardFromSavedCards(
            this.state.savedCardDetails,
            this.props.location.state.egvCartGuid
          );
        } else {
          this.props.softReservationPaymentForSavedCard(
            this.state.savedCardDetails,
            this.state.addressId,
            this.state.paymentModeSelected
          );
        }
      }
      if (
        this.state.currentPaymentMode === CREDIT_CARD ||
        (this.state.currentPaymentMode === EMI &&
          !this.state.isNoCostEmiApplied) ||
        this.state.currentPaymentMode === DEBIT_CARD ||
        (this.state.currentPaymentMode === EMI &&
          this.state.isNoCostEmiProceeded)
      ) {
        if (this.state.isGiftCard) {
          this.props.jusPayTokenizeForGiftCard(
            this.state.cardDetails,
            this.state.paymentModeSelected,
            this.props.location.state.egvCartGuid
          );
        } else {
          this.softReservationForPayment(this.state.cardDetails);
        }
        this.onChangePaymentMode({ currentPaymentMode: null });
      }

      if (this.state.currentPaymentMode === NET_BANKING_PAYMENT_MODE) {
        if (this.state.isGiftCard) {
          this.props.createJusPayOrderForGiftCardNetBanking(
            this.props.location.state.egvCartGuid
          );
        } else {
          this.softReservationPaymentForNetBanking(
            this.state.bankCodeForNetBanking
          );
        }
      }

      if (!this.state.isRemainingAmount) {
        this.props.softReservationForCliqCash(
          localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        );
      }

      if (this.state.binValidationCOD && !this.state.isCliqCashApplied) {
        this.softReservationForCODPayment();
      }
      if (this.state.paymentModeSelected === PAYTM) {
        if (this.state.isGiftCard) {
          this.props.createJusPayOrderForGiftCardNetBanking(
            this.props.location.state.egvCartGuid
          );
        } else {
          this.softReservationPaymentForWallet(PAYTM);
        }
      }
      if (this.state.isNoCostEmiApplied) {
        this.setState({ isNoCostEmiProceeded: true });
      }
    }
    if (!this.state.isNoCostEmiApplied) {
      this.onChangePaymentMode({ currentPaymentMode: null });
    }
  };

  addAddress = address => {
    if (!address) {
      this.props.displayToast("Please enter the valid details");
      return false;
    }
    if (address && !address.postalCode) {
      this.props.displayToast(PINCODE_TEXT);
      return false;
    }
    if (address && address.postalCode && address.postalCode.length < 6) {
      this.props.displayToast(PINCODE_VALID_TEXT);
      return false;
    }
    if (address && !address.firstName) {
      this.props.displayToast(NAME_TEXT);
      return false;
    }
    if (address && !address.lastName) {
      this.props.displayToast(LAST_NAME_TEXT);
      return false;
    }
    if (address && !address.line1) {
      this.props.displayToast(ADDRESS_TEXT);
      return false;
    }

    if (address && !address.town) {
      this.props.displayToast(CITY_TEXT);
      return false;
    }
    if (address && !address.state) {
      this.props.displayToast(STATE_TEXT);
      return false;
    }
    if (address && !address.phone) {
      this.props.displayToast(PHONE_TEXT);
      return false;
    }
    if (address && !MOBILE_PATTERN.test(address.phone)) {
      this.props.displayToast(PHONE_VALID_TEXT);
      return false;
    }
    if (address && !address.addressType) {
      this.props.displayToast(SELECT_ADDRESS_TYPE);
      return false;
    }
    if (!address.userEmailId && !address.emailId && address.emailId === "") {
      this.props.displayToast("Please enter the EmailId");
      return false;
    }
    if (
      address.emailId &&
      address.emailId !== "" &&
      !EMAIL_REGULAR_EXPRESSION.test(address.emailId)
    ) {
      this.props.displayToast(EMAIL_VALID_TEXT);
      return false;
    } else {
      if (this.props.addUserAddress) {
        let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
        let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
        let cartDetailsLoggedInUser = Cookie.getCookie(
          CART_DETAILS_FOR_LOGGED_IN_USER
        );
        let getCartDetailCNCObj = {
          userId: JSON.parse(userDetails).userName,
          accessToken: JSON.parse(customerCookie).access_token,
          cartId: JSON.parse(cartDetailsLoggedInUser).code,
          pinCode: address && address.postalCode,
          isSoftReservation: false
        };
        this.props.addUserAddress(address, getCartDetailCNCObj);
        this.setState({ addNewAddress: false });
      }
    }
  };

  addNewAddress = () => {
    this.setState({ addNewAddress: true });
  };
  binValidationForPaytm = val => {
    if (val) {
      localStorage.setItem(PAYMENT_MODE_TYPE, PAYTM);
      this.setState({ paymentModeSelected: PAYTM });
      this.props.binValidation(PAYTM, "");
    } else {
      this.setState({ paymentModeSelected: null });
    }
  };
  applyBankCoupons = async val => {
    if (val.length > 0) {
      const applyCouponReq = await this.props.applyBankOffer(val[0]);

      if (applyCouponReq.status === SUCCESS) {
        this.setState({ selectedBankOfferCode: val[0] });
      }
    } else {
      const releaseCouponReq = await this.props.releaseBankOffer(val[0]);
      if (releaseCouponReq.status === SUCCESS) {
        this.setState({ selectedBankOfferCode: "" });
      }
    }
  };
  openBankOffers = () => {
    this.props.showCouponModal({
      selectedBankOfferCode: localStorage.getItem(BANK_COUPON_COOKIE),
      coupons: this.props.cart.paymentModes.paymentOffers,
      selecteBankOffer: val => {
        this.selecteBankOffer(val);
      }
    });
  };

  applyCliqCash = () => {
    if (this.state.isNoCostEmiApplied) {
      const doCallForApplyCliqCash = () => {
        this.setState({
          isCliqCashApplied: true,
          currentPaymentMode: null,
          isNoCostEmiApplied: false,
          binValidationCOD: false,
          captchaReseponseForCOD: null,
          PAYMENT_MODE_TYPE: "Cliq Cash"
        });
        this.props.applyCliqCash();
      };
      this.props.showModalForCliqCashOrNoCostEmi({ doCallForApplyCliqCash });
    } else {
      this.setState({
        isCliqCashApplied: true,
        binValidationCOD: false,
        captchaReseponseForCOD: null,
        PAYMENT_MODE_TYPE: "Cliq Cash"
      });
      this.props.applyCliqCash();
    }
  };

  removeCliqCash = () => {
    this.setState({
      isCliqCashApplied: false,
      isCliqCashApplied: false,
      captchaReseponseForCOD: null,
      PAYMENT_MODE_TYPE: null,
      binValidationCOD: false
    });
    this.props.removeCliqCash();
  };

  binValidation = (paymentMode, binNo) => {
    if (this.state.isPaymentFailed) {
      localStorage.setItem(PAYMENT_MODE_TYPE, paymentMode);
      const parsedQueryString = queryString.parse(this.props.location.search);
      const cartGuId = parsedQueryString.value;
      this.props.binValidation(paymentMode, binNo, cartGuId);
    } else {
      localStorage.setItem(PAYMENT_MODE_TYPE, paymentMode);
      this.setState({ paymentModeSelected: paymentMode });
      this.props.binValidation(paymentMode, binNo);
    }
  };
  softReservationPaymentForWallet = bankName => {
    this.props.softReservationPaymentForNetBanking(
      WALLET,
      PAYTM,
      bankName,
      localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
    );
  };
  binValidationForCOD = paymentMode => {
    localStorage.setItem(PAYMENT_MODE_TYPE, paymentMode);
    this.setState({ paymentModeSelected: paymentMode, binValidationCOD: true });
    this.props.binValidationForCOD(paymentMode);
  };

  binValidationForNetBank = (paymentMode, bankName) => {
    localStorage.setItem(PAYMENT_MODE_TYPE, paymentMode);
    this.setState({ paymentModeSelected: paymentMode });
    this.props.binValidationForNetBanking(paymentMode, bankName);
  };

  binValidationForSavedCard = cardDetails => {
    if (cardDetails) {
      this.setState({
        paymentModeSelected: `${cardDetails.cardType} Card`
      });
      localStorage.setItem(PAYMENT_MODE_TYPE, `${cardDetails.cardType} Card`);
      this.setState({ savedCardDetails: cardDetails });
      if (
        !this.state.savedCardDetails ||
        (this.state.savedCardDetails &&
          this.state.savedCardDetails.cardEndingDigits !==
            cardDetails.cardEndingDigits)
      ) {
        this.props.binValidation(
          `${cardDetails.cardType} Card`,
          cardDetails.cardISIN
        );
      }
    } else {
      this.setState({ savedCardDetails: null });
    }
  };

  softReservationForPayment = cardDetails => {
    cardDetails.pinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
    this.props.softReservationForPayment(
      cardDetails,
      this.state.selectedAddress,
      this.state.paymentModeSelected
    );
  };

  softReservationPaymentForNetBanking = bankName => {
    this.props.softReservationPaymentForNetBanking(
      NET_BANKING,
      this.state.paymentModeSelected,
      bankName,
      localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
    );
  };

  createJusPayOrderForGiftCardNetBanking = () => {
    if (this.props.createJusPayOrderForGiftCardNetBanking) {
      this.props.createJusPayOrderForGiftCardNetBanking(
        this.props.location.state.egvCartGuid
      );
    }
  };

  captureOrderExperience = rating => {
    let orderId;
    if (this.props.cart.cliqCashJusPayDetails) {
      orderId = this.props.cart.cliqCashJusPayDetails.orderId;
    } else {
      orderId = this.props.cart.orderConfirmationDetails.orderRefNo;
    }
    if (this.props.captureOrderExperience) {
      this.props.captureOrderExperience(orderId, rating);
    }
  };

  continueShopping = () => {
    this.props.history.index = 0;
    this.props.history.push(HOME_ROUTER);
  };

  softReservationForCODPayment = () => {
    this.props.softReservationForCODPayment(
      localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
    );
  };

  addGiftCard = () => {
    this.props.addGiftCard();
  };

  orderConfirmationUpdate = () => {
    if (this.props.clearCaptureOrderExperience) {
      this.props.clearCaptureOrderExperience();
    }
  };
  navigateToOrderDetailPage(orderId) {
    this.props.history.push(`${MY_ACCOUNT}${ORDER}/?${ORDER_CODE}=${orderId}`);
  }

  validateCard() {
    if (
      !this.state.cardDetails.cardNumber ||
      (!this.state.cardDetails.cardName ||
        (this.state.cardDetails.cardName &&
          this.state.cardDetails.cardName.length < 3)) ||
      (this.state.cardDetails.cardName &&
        this.state.cardDetails.cardName.length < 1) ||
      !this.state.cardDetails.monthValue ||
      !this.state.cardDetails.yearValue
    ) {
      return true;
    } else {
      const card = new cardValidator(this.state.cardDetails.cardNumber);
      let cardDetails = card.getCardDetails();

      if (card.validateCard()) {
        if (
          cardDetails &&
          cardDetails.cvv_length &&
          cardDetails.cvv_length.includes(
            this.state.cardDetails.cvvNumber
              ? this.state.cardDetails.cvvNumber.length
              : 0
          )
        ) {
          return false;
        } else {
          return true;
        }
      } else {
        return true;
      }
    }
  }

  validateSubmitButton() {
    if (this.state.cardDetails) {
      if (
        this.state.currentPaymentMode === CREDIT_CARD ||
        this.state.currentPaymentMode === DEBIT_CARD
      ) {
        return this.validateCard();
      }
      if (this.state.currentPaymentMode === SAVED_CARD_PAYMENT_MODE) {
        if (!this.state.savedCardDetails) {
          return true;
        } else {
          return false;
        }
      } else if (this.state.currentPaymentMode === EMI) {
        if (this.state.isNoCostEmiApplied || this.state.cardDetails.emi_bank) {
          return false;
        } else {
          return true;
        }
      } else if (this.state.currentPaymentMode === NET_BANKING_PAYMENT_MODE) {
        if (!this.state.bankCodeForNetBanking) {
          return true;
        } else {
          return false;
        }
      } else if (
        this.state.currentPaymentMode === CASH_ON_DELIVERY_PAYMENT_MODE
      ) {
        if (this.state.captchaReseponseForCOD === null) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
  render() {
    let labelForButton,
      checkoutButtonStatus = false;

    if (
      !this.state.isPaymentFailed &&
      !this.state.confirmAddress &&
      !this.state.isGiftCard &&
      (this.props.cart.userAddress && this.props.cart.userAddress.addresses)
    ) {
      if (!this.state.addressId) {
        checkoutButtonStatus = true;
      }

      labelForButton = PROCEED;
    } else if (
      (this.state.confirmAddress && !this.state.deliverMode) ||
      this.state.isGiftCard
    ) {
      labelForButton = PROCEED;
    } else if (
      this.state.currentPaymentMode === CASH_ON_DELIVERY_PAYMENT_MODE
    ) {
      checkoutButtonStatus = this.validateSubmitButton();
      labelForButton = PLACE_ORDER;
    } else if (this.state.currentPaymentMode === EMI) {
      if (this.state.currentSelectedEMIType === NO_COST_EMI) {
        if (this.state.isNoCostEmiProceeded) {
          checkoutButtonStatus = this.validateCard();
          labelForButton = PAY_NOW;
        } else {
          if (this.state.isNoCostEmiApplied) {
            checkoutButtonStatus = false;
            labelForButton = CONTINUE;
          } else {
            checkoutButtonStatus = true;
            labelForButton = CONTINUE;
          }
        }
      } else if (this.state.currentSelectedEMIType === STANDARD_EMI) {
        if (
          this.state.cardDetails &&
          this.state.cardDetails.emi_bank &&
          this.state.cardDetails.emi_bank !== null
        ) {
          checkoutButtonStatus = this.validateCard();
          labelForButton = PAY_NOW;
        } else {
          checkoutButtonStatus = true;
        }
      } else {
        checkoutButtonStatus = true;
        labelForButton = PAY_NOW;
      }
    } else if (this.state.currentPaymentMode === E_WALLET) {
      if (this.state.paymentModeSelected === PAYTM) {
        labelForButton = PAY_NOW;
        checkoutButtonStatus = false;
      } else {
        labelForButton = PROCEED;

        checkoutButtonStatus = true;
      }
    } else if (this.state.currentPaymentMode === null) {
      labelForButton = PROCEED;
      checkoutButtonStatus = true;
    } else {
      checkoutButtonStatus = this.validateSubmitButton();
      labelForButton = PAY_NOW;
    }
    if (this.state.isFirstAddress) {
      labelForButton = CONTINUE;
      checkoutButtonStatus = false;
    }
    if (!this.state.isRemainingAmount && this.state.isCliqCashApplied) {
      checkoutButtonStatus = false;
      labelForButton = PAY_NOW;
    }
    if (this.props.cart.getUserAddressStatus === REQUESTING) {
      return this.renderLoader();
    } else {
      if (
        this.props.cart.loading ||
        this.props.cart.cartDetailsCNCLoader ||
        this.props.cart.jusPaymentLoader ||
        this.props.cart.selectDeliveryModeLoader ||
        (!this.props.cart.paymentModes && this.state.deliverMode) ||
        this.props.cart.isPaymentProceeded
      ) {
        this.props.showSecondaryLoader();
      } else {
        this.props.hideSecondaryLoader();
      }
    }
    if (this.props.cart.transactionStatus === REQUESTING) {
      return false;
    }
    if (
      this.state.addNewAddress &&
      !this.state.orderConfirmation &&
      !this.state.isGiftCard &&
      !this.props.cart.isPaymentProceeded
    ) {
      return (
        <div className={styles.addDeliveryAddressHolder}>
          <AddDeliveryAddress
            addUserAddress={address => this.addAddress(address)}
            {...this.state}
            showSecondaryLoader={this.props.showSecondaryLoader}
            hideSecondaryLoader={this.props.hideSecondaryLoader}
            loading={this.props.cart.loading}
            onChange={val => this.onChange(val)}
            isFirstAddress={false}
            displayToast={message => this.props.displayToast(message)}
            getPinCode={val => this.getPinCodeDetails(val)}
            getPinCodeDetails={this.props.getPinCodeDetails}
            getPincodeStatus={this.props.getPincodeStatus}
            onFocusInput={() => this.onFocusInput()}
            resetAddAddressDetails={() => this.props.resetAddAddressDetails()}
            getUserDetails={() => this.getUserDetails()}
            userDetails={this.props.userDetails}
            clearPinCodeStatus={() => this.props.clearPinCodeStatus()}
          />
        </div>
      );
    } else if (
      (!this.state.addNewAddress &&
        this.props.cart &&
        !this.state.orderConfirmation &&
        !this.props.cart.isPaymentProceeded) ||
      this.state.isGiftCard
    ) {
      return (
        <div className={styles.base}>
          {!this.state.isPaymentFailed &&
            !this.state.confirmAddress &&
            !this.state.isGiftCard &&
            (this.props.cart.userAddress &&
            this.props.cart.userAddress.addresses
              ? this.renderCheckoutAddress()
              : this.renderInitialAddAddressForm())}

          {!this.state.isPaymentFailed &&
            this.state.confirmAddress &&
            !this.state.isGiftCard &&
            !this.state.showCliqAndPiq && (
              <div className={styles.deliveryAddress}>
                <DeliveryAddressSet
                  addressType={this.state.selectedAddress.addressType}
                  address={this.state.selectedAddress.line1}
                  changeDeliveryAddress={() => this.changeDeliveryAddress()}
                />
              </div>
            )}

          {!this.state.isPaymentFailed &&
            this.props.cart.cartDetailsCNC &&
            this.state.confirmAddress &&
            !this.state.deliverMode &&
            !this.state.isGiftCard &&
            (this.state.showCliqAndPiq
              ? this.renderCliqAndPiq()
              : this.renderDeliverModes())}

          {!this.state.isPaymentFailed &&
            this.state.deliverMode &&
            !this.state.isGiftCard && (
              <div className={styles.deliveryAddress}>
                <DeliveryModeSet
                  productDelivery={this.props.cart.cartDetailsCNC.products}
                  changeDeliveryModes={() => this.changeDeliveryModes()}
                  selectedDeliveryDetails={this.state.ussIdAndDeliveryModesObj}
                />
              </div>
            )}

          {this.state.isPaymentFailed && (
            <div className={styles.paymentFailedCardHolder}>
              <TransactionFailed />
            </div>
          )}

          {((!this.state.paymentMethod &&
            (this.state.confirmAddress && this.state.deliverMode)) ||
            this.state.isPaymentFailed ||
            this.state.isGiftCard) && (
            <div className={styles.paymentCardHolderπp}>
              <PaymentCardWrapper
                applyBankCoupons={val => this.applyBankCoupons(val)}
                isCliqCashApplied={this.state.isCliqCashApplied}
                isRemainingBalance={this.state.isRemainingAmount}
                isPaymentFailed={this.state.isPaymentFailed}
                isFromGiftCard={this.state.isGiftCard}
                isNoCostEmiApplied={this.state.isNoCostEmiApplied}
                cart={this.props.cart}
                paymentModeSelected={this.state.paymentModeSelected}
                changeSubEmiOption={currentSelectedEMIType =>
                  this.changeSubEmiOption(currentSelectedEMIType)
                }
                selectedSavedCardDetails={this.state.savedCardDetails}
                selectedBankOfferCode={this.state.selectedBankOfferCode}
                openBankOffers={() => this.openBankOffers()}
                cliqCashAmount={this.state.cliqCashAmount}
                userCliqCashAmount={this.state.userCliqCashAmount}
                applyCliqCash={() => this.applyCliqCash()}
                removeCliqCash={() => this.removeCliqCash()}
                currentPaymentMode={this.state.currentPaymentMode}
                cardDetails={this.state.cardDetails}
                captchaReseponseForCOD={this.state.captchaReseponseForCOD}
                verifyCaptcha={captchaReseponseForCOD =>
                  this.setState({ captchaReseponseForCOD })
                }
                onChange={val => this.onChangePaymentMode(val)}
                bankCodeForNetBanking={this.state.bankCodeForNetBanking}
                onSelectBankForNetBanking={bankCodeForNetBanking =>
                  this.setState({ bankCodeForNetBanking })
                }
                onChangeCardDetail={val => this.onChangeCardDetail(val)}
                binValidation={(paymentMode, binNo) =>
                  this.binValidation(paymentMode, binNo)
                }
                binValidationForCOD={paymentMode =>
                  this.binValidationForCOD(paymentMode)
                }
                softReservationForPayment={cardDetails =>
                  this.softReservationForPayment(cardDetails)
                }
                softReservationForCODPayment={() =>
                  this.softReservationForCODPayment()
                }
                binValidationForNetBank={(paymentMode, bankName) =>
                  this.binValidationForNetBank(paymentMode, bankName)
                }
                softReservationPaymentForNetBanking={bankName =>
                  this.softReservationPaymentForNetBanking(bankName)
                }
                binValidationForSavedCard={cardDetails =>
                  this.binValidationForSavedCard(cardDetails)
                }
                createJusPayOrderForGiftCardNetBanking={() =>
                  this.createJusPayOrderForGiftCardNetBanking()
                }
                onFocusInput={() => this.onFocusInput()}
                addGiftCard={() => this.addGiftCard()}
                binValidationForPaytm={val => this.binValidationForPaytm(val)}
                displayToast={message => this.props.displayToast(message)}
                getCODEligibility={() => this.getCODEligibility()}
                getNetBankDetails={() => this.getNetBankDetails()}
                getEmiBankDetails={() => this.getEmiBankDetails()}
                getEmiEligibility={() => this.getEmiEligibility()}
                getBankAndTenureDetails={() => this.getBankAndTenureDetails()}
                getEmiTermsAndConditionsForBank={(bankCode, bankName) =>
                  this.getEmiTermsAndConditionsForBank(bankCode, bankName)
                }
                applyNoCostEmi={(couponCode, bankName) =>
                  this.applyNoCostEmi(couponCode, bankName)
                }
                removeNoCostEmi={couponCode => this.removeNoCostEmi(couponCode)}
                getItemBreakUpDetails={(
                  couponCode,
                  noCostEmiText,
                  noCostProductCount
                ) =>
                  this.getItemBreakUpDetails(
                    couponCode,
                    noCostEmiText,
                    noCostProductCount
                  )
                }
                isNoCostEmiProceeded={this.state.isNoCostEmiProceeded}
                changeNoCostEmiPlan={() =>
                  this.setState({
                    isNoCostEmiApplied: false,
                    isNoCostEmiProceeded: false
                  })
                }
                totalProductCount={
                  this.props.cart &&
                  this.props.cart.cartDetailsCNC &&
                  this.props.cart.cartDetailsCNC.products &&
                  this.props.cart.cartDetailsCNC.products.length
                }
              />
            </div>
          )}

          {!this.state.showCliqAndPiq && (
            <Checkout
              disabled={checkoutButtonStatus}
              label={labelForButton}
              noCostEmiEligibility={
                this.props.cart &&
                this.props.cart.emiEligibilityDetails &&
                this.props.cart.emiEligibilityDetails.isNoCostEMIEligible
              }
              noCostEmiDiscount={this.state.noCostEmiDiscount}
              amount={this.state.payableAmount}
              bagTotal={this.state.bagAmount}
              payable={this.state.payableAmount}
              coupons={this.state.couponDiscount}
              discount={this.state.totalDiscount}
              delivery={this.state.deliveryCharge}
              showDetails={this.state.showCartDetails}
              onCheckout={
                this.state.isPaymentFailed
                  ? this.handleSubmitAfterPaymentFailure
                  : this.handleSubmit
              }
              isCliqCashApplied={this.state.isCliqCashApplied}
              cliqCashPaidAmount={this.state.cliqCashPaidAmount}
            />
          )}
        </div>
      );
    } else if (this.state.orderConfirmation) {
      return (
        <div>
          {this.props.cart.orderConfirmationDetails && (
            <div className={styles.orderConfirmationHolder}>
              <OrderConfirmation
                clearCartDetails={() => this.props.clearCartDetails()}
                orderId={this.props.cart.orderConfirmationDetails.orderRefNo}
                captureOrderExperience={rating =>
                  this.captureOrderExperience(rating)
                }
                orderStatusMessage={
                  this.props.cart.orderConfirmationDetails.orderStatusMessage
                }
                continueShopping={() => this.continueShopping()}
                orderConfirmationUpdate={() => this.orderConfirmationUpdate()}
                trackOrder={() =>
                  this.navigateToOrderDetailPage(
                    this.props.cart.orderConfirmationDetails.orderRefNo
                  )
                }
                orderDetails={this.props.cart.orderConfirmationDetails}
              />
            </div>
          )}
          {this.props.cart.cliqCashJusPayDetails && (
            <div className={styles.orderConfirmationHolder}>
              <OrderConfirmation
                clearCartDetails={this.props.clearCartDetails}
                orderId={this.props.cart.cliqCashJusPayDetails.orderId}
                orderStatusMessage={this.props.orderConfirmationText}
                captureOrderExperience={rating =>
                  this.captureOrderExperience(rating)
                }
                continueShopping={() => this.continueShopping()}
                orderConfirmationUpdate={() => this.orderConfirmationUpdate()}
                trackOrder={() =>
                  this.navigateToOrderDetailPage(
                    this.props.cart.cliqCashJusPayDetails.orderId
                  )
                }
                orderDetails={this.props.cart.cliqCashJusPayDetails}
              />
            </div>
          )}
        </div>
      );
    } else {
      return null;
    }
  }
}

export default CheckOutPage;

CheckOutPage.propTypes = {
  getCartDetailsCNC: PropTypes.func,
  selectDeliveryMode: PropTypes.func,
  getAllStoresCNC: PropTypes.func,
  addStoreCNC: PropTypes.func,
  addPickupPersonCNC: PropTypes.func
};

CheckOutPage.defaultProps = {
  cartTax: "included",
  delivery: "Free",
  offers: "Apply",
  orderConfirmationText: "Your Order Successfully Placed"
};
