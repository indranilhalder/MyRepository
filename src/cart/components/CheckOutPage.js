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
  JUS_PAY_AUTHENTICATION_FAILED
} from "../../lib/constants";
import { HOME_ROUTER, SUCCESS, CHECKOUT } from "../../lib/constants";
import MDSpinner from "react-md-spinner";
const SEE_ALL_BANK_OFFERS = "See All Bank Offers";
const PAYMENT_CHARGED = "CHARGED";
const PAYMENT_MODE = "EMI";
const NET_BANKING = "NB";
const CART_GU_ID = "cartGuid";
const DELIVERY_MODE_ADDRESS_ERROR = "No Delivery Modes At Selected Address";
const CONTINUE = "Continue";
const PROCEED = "Proceed";
const COUPON_AVAILABILITY_ERROR_MESSAGE = "Your applied coupon has expired";
class CheckOutPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
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
      selectedSlaveId: null,
      ussIdAndDeliveryModesObj: {}, // this object we are using for check when user will continue after  delivery mode then we ll check for all products we selected delivery mode or not
      selectedProductsUssIdForCliqAndPiq: null,
      orderId: "",
      savedCardDetails: "",
      binValidationCOD: false,
      isGiftCard: false,
      isRemainingAmount: true,
      payableAmount: "",
      cliqCashAmount: "",
      bagAmount: "",
      selectedDeliveryDetails: null,
      ratingExperience: false
    };
  }
  onClickImage(productCode) {
    if (productCode) {
      this.props.history.push(`/p-${productCode.toLowerCase()}`);
    }
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
  renderLoader() {
    return (
      <div className={styles.cartLoader}>
        <div className={styles.spinner}>
          <MDSpinner />
        </div>
      </div>
    );
  }
  componentDidUpdate() {
    if (
      this.props.cart.orderConfirmationDetails ||
      this.props.cart.cliqCashPaymentDetails
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

    this.setState({
      ussIdAndDeliveryModesObj: currentSelectedDeliveryModes,
      isSelectedDeliveryModes: true
    });
  }

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
    this.setState(prevState => ({
      showPickupPerson: !prevState.showPickupPerson
    }));
  }
  addStoreCNC(selectedSlaveId) {
    this.handleSelectDeliveryMode(
      COLLECT,
      this.state.selectedProductsUssIdForCliqAndPiq
    );
    this.togglePickupPersonForm();
    this.setState({ selectedSlaveId });
    this.props.addStoreCNC(
      this.state.selectedProductsUssIdForCliqAndPiq,
      selectedSlaveId
    );
  }
  addPickupPersonCNC(mobile, name) {
    this.setState({ showCliqAndPiq: false });
    this.props.addPickupPersonCNC(mobile, name);
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
        <DummyTab title="Delivery Mode" number={2} />
        <DummyTab title="Payment Method" number={3} />
      </div>
    );
  };
  renderDeliverModes = () => {
    return (
      <div className={styles.products}>
        <div className={styles.header}>
          <CheckOutHeader indexNumber="2" confirmTitle="Choose delivery mode" />
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
                  productDetails={val.productBrand}
                  productName={val.productName}
                  price={val.offerPrice}
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
        selectedSlaveId={this.state.selectedSlaveId}
        numberOfStores={availableStores.length}
        showPickupPerson={this.state.showPickupPerson}
        productName={currentSelectedProduct.productName}
        productColour={currentSelectedProduct.color}
        hidePickupPersonDetail={() => this.togglePickupPersonForm()}
        addStoreCNC={slavesId => this.addStoreCNC(slavesId)}
        addPickupPersonCNC={(mobile, name) =>
          this.addPickupPersonCNC(mobile, name)
        }
        changePincode={pincode => this.changePincodeOnCliqAndPiq(pincode)}
        goBack={() => this.removeCliqAndPiq()}
      />
    );
  }
  renderPaymentModes = () => {
    if (this.state.paymentMethod) {
      return <div>Payment Modes Expand</div>;
    }
  };

  renderBankOffers = () => {
    return (
      <GridSelect
        elementWidthMobile={100}
        offset={0}
        limit={1}
        onSelect={val => this.applyBankCoupons(val)}
      >
        <BankOffer
          bankName={
            this.props.cart.paymentModes.paymentOffers.coupons[0].offerTitle
          }
          offerText={
            this.props.cart.paymentModes.paymentOffers.coupons[0]
              .offerMinCartValue
          }
          label={SEE_ALL_BANK_OFFERS}
          applyBankOffers={() => this.openBankOffers()}
          value={
            this.props.cart.paymentModes.paymentOffers.coupons[0].offerCode
          }
        />
      </GridSelect>
    );
  };

  renderInitialAddAddressForm() {
    return (
      <div className={styles.addInitialAddAddress}>
        <AddDeliveryAddress
          addUserAddress={address => this.addAddress(address)}
          {...this.state}
          onChange={val => this.onChange(val)}
        />
        <DummyTab title="Delivery Mode" number={2} />
        <DummyTab title="Payment Method" number={3} />
      </div>
    );
  }
  changeDeliveryAddress = () => {
    this.setState({
      confirmAddress: false,
      deliverMode: false,
      isSelectedDeliveryModes: false
    });
  };

  componentWillReceiveProps(nextProps) {
    // adding default address is selected
    if (
      nextProps.cart.status === SUCCESS &&
      !this.state.addressId &&
      nextProps.cart &&
      nextProps.cart.userAddress &&
      nextProps.cart.userAddress.addresses
    ) {
      let defaultAddressId = null;
      let defaultAddress = nextProps.cart.userAddress.addresses.find(
        address => {
          return address.defaultAddress;
        }
      );
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
          //check for delivery modes is exit or not for every pincode
          // if we don;t have any delivery modes for any product
          // then we ned to navigate user to mybag

          if (!product.elligibleDeliveryMode) {
            this.navigateToMyBag();
          }
          // end of navigate user to myBag

          if (
            product.elligibleDeliveryMode &&
            product.elligibleDeliveryMode.findIndex(mode => {
              return mode.code === HOME_DELIVERY;
            }) >= 0
          ) {
            let newObjectAdd = {};
            newObjectAdd[product.USSID] = HOME_DELIVERY;
            Object.assign(defaultSelectedDeliveryModes, newObjectAdd);
          } else if (
            product.elligibleDeliveryMode &&
            product.elligibleDeliveryMode.findIndex(mode => {
              return mode.code === EXPRESS;
            }) >= 0
          ) {
            let newObjectAdd = {};
            newObjectAdd[product.USSID] = EXPRESS;
            Object.assign(defaultSelectedDeliveryModes, newObjectAdd);
          }
        });
      }
      this.setState({ ussIdAndDeliveryModesObj: defaultSelectedDeliveryModes });
    }

    // end if adding selected default delivery modes for every product
    if (nextProps.cart.cliqCashPaymentDetails) {
      if (
        this.state.isRemainingAmount !==
        nextProps.cart.cliqCashPaymentDetails.isRemainingAmount
      ) {
        this.setState({
          isRemainingAmount:
            nextProps.cart.cliqCashPaymentDetails.isRemainingAmount,
          payableAmount:
            Math.round(
              nextProps.cart.cliqCashPaymentDetails.paybleAmount.value * 100
            ) / 100,
          cliqCashAmount:
            Math.round(
              nextProps.cart.cliqCashPaymentDetails.cliqCashBalance.value * 100
            ) / 100,
          bagAmount:
            Math.round(
              nextProps.cart.cartDetailsCNC.cartAmount.bagTotal.value * 100
            ) / 100
        });
      }
    } else if (this.state.isGiftCard) {
      this.setState({
        isRemainingAmount: true,
        payableAmount: Math.round(this.props.location.state.amount * 100) / 100,
        bagAmount: Math.round(this.props.location.state.amount * 100) / 100
      });
    } else {
      if (nextProps.cart.cartDetailsCNC && this.state.isRemainingAmount) {
        let cliqCashAmount = 0;
        if (
          nextProps.cart.paymentModes &&
          nextProps.cart.paymentModes.cliqCash &&
          nextProps.cart.paymentModes.cliqCash.totalCliqCashBalance
        ) {
          cliqCashAmount =
            Math.round(
              nextProps.cart.paymentModes.cliqCash.totalCliqCashBalance * 100
            ) / 100;
        }
        this.setState({
          payableAmount:
            Math.round(
              nextProps.cart.cartDetailsCNC.cartAmount.paybleAmount.value * 100
            ) / 100,
          cliqCashAmount: cliqCashAmount,
          bagAmount:
            Math.round(
              nextProps.cart.cartDetailsCNC.cartAmount.bagTotal.value * 100
            ) / 100
        });
      }
    }

    if (nextProps.cart.justPayPaymentDetails !== null) {
      if (nextProps.cart.justPayPaymentDetails.payment) {
        window.location.replace(
          nextProps.cart.justPayPaymentDetails.payment.authentication.url
        );
      }
    }
    if (nextProps.cart.orderConfirmationDetailsStatus === SUCCESS) {
      this.setState({ orderConfirmation: true });
    }
    if (nextProps.cart.cliqCashJusPayDetails) {
      this.setState({ orderId: nextProps.cart.cliqCashJusPayDetails.orderId });
      this.setState({ orderConfirmation: true });
    }
    if (nextProps.cart.binValidationCODStatus === SUCCESS) {
      this.setState({ binValidationCOD: true });
    }
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (
      nextProps.cart.binValidationStatus === SUCCESS &&
      nextProps.cart.justPayPaymentDetailsStatus === null
    ) {
      if (this.state.paymentModeSelected === PAYMENT_MODE) {
        return false;
      }
    }
    return true;
  }

  componentDidMount() {
    const parsedQueryString = queryString.parse(this.props.location.search);
    const value = parsedQueryString.status;
    const orderId = parsedQueryString.order_id;

    if (value === PAYMENT_CHARGED) {
      this.setState({ orderId: orderId });

      if (this.props.updateTransactionDetails) {
        let cartId;
        cartId = localStorage.getItem(OLD_CART_GU_ID);
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
      this.props.location.state.isFromGiftCard
    ) {
      this.setState({ isGiftCard: true });
      let guIdObject = new FormData();
      guIdObject.append(CART_GU_ID, this.props.location.state.egvCartGuid);
      this.props.getPaymentModes(guIdObject);
      this.props.getNetBankDetails();
    } else {
      if (this.props.getCartDetailsCNC) {
        let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
        let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
        let cartDetailsLoggedInUser = Cookie.getCookie(
          CART_DETAILS_FOR_LOGGED_IN_USER
        );

        if (userDetails && customerCookie && cartDetailsLoggedInUser) {
          this.props.getCartDetailsCNC(
            JSON.parse(userDetails).userName,
            JSON.parse(customerCookie).access_token,
            JSON.parse(cartDetailsLoggedInUser).code,
            localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE),
            false
          );
        }
        this.props.getUserAddress(
          localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        );
        if (cartDetailsLoggedInUser) {
          let guIdObject = new FormData();
          guIdObject.append(
            CART_GU_ID,
            JSON.parse(cartDetailsLoggedInUser).guid
          );

          this.props.getPaymentModes(guIdObject);
        }

        this.props.getCODEligibility();
        this.props.getNetBankDetails();
        if (this.props.location && this.props.location.state) {
          this.props.getEmiBankDetails(this.props.location.state.productValue);
        }
      }
    }
  }

  onSelectAddress(selectedAddress) {
    let addressSelected = find(
      this.props.cart.cartDetailsCNC.addressDetailsList.addresses,
      address => {
        return address.id === selectedAddress[0];
      }
    );

    this.updateLocalStoragePinCode(addressSelected.postalCode);
    // here we are checking the if user selected any address then setting our state
    // and in else condition if user deselect then this function will again call and
    //  then we are resetting the previous selected address
    if (selectedAddress[0]) {
      this.setState({
        confirmAddress: false,
        selectedAddress: addressSelected
      });
      this.setState({ addressId: addressSelected.id });
    } else {
      this.setState({ addressId: null, selectedAddress: null });
    }
  }
  changeDeliveryModes = () => {
    this.setState({ deliverMode: false });
  };

  onChange(val) {
    this.setState(val);
  }

  availabilityOfUserCoupon = () => {
    if (!this.state.isGiftCard) {
      let couponCookie = Cookie.getCookie(COUPON_COOKIE);
      let cartDetailsCouponDiscount = this.props.cart.cartDetailsCNC.cartAmount
        .couponDiscountAmount;

      if (couponCookie && !cartDetailsCouponDiscount) {
        this.props.displayToast(COUPON_AVAILABILITY_ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  };
  handleSubmit = () => {
    if (this.availabilityOfUserCoupon()) {
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
        if (this.props.selectDeliveryMode) {
          this.props.selectDeliveryMode(
            this.state.ussIdAndDeliveryModesObj,
            localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
          );
          // this.props.getOrderSummary(this.state.selectedAddress.postalCode);
        }
        this.setState({
          deliverMode: true
        });
      }

      if (this.state.savedCardDetails !== "") {
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
      if (!this.state.isRemainingAmount) {
        this.props.softReservationForCliqCash(
          localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        );
      }

      if (this.state.binValidationCOD) {
        this.softReservationForCODPayment();
      }
      if (this.state.paymentModeSelected === PAYTM) {
        this.softReservationPaymentForWallet(PAYTM);
      }
    } else {
      this.props.history.goBack();
    }
  };

  addAddress = address => {
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
        pinCode: address.postalCode,
        isSoftReservation: false
      };
      this.props.addUserAddress(address, getCartDetailCNCObj);

      this.setState({ addNewAddress: false });
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
    }
  };
  applyBankCoupons = val => {
    if (val.length > 0) {
      this.props.applyBankOffer(val);
    } else {
      this.props.releaseBankOffer(val);
    }
  };
  openBankOffers = () => {
    this.props.showCouponModal(this.props.cart.paymentModes.paymentOffers);
  };

  applyCliqCash = () => {
    this.props.applyCliqCash();
  };

  removeCliqCash = () => {
    this.props.removeCliqCash();
  };

  binValidation = (paymentMode, binNo) => {
    localStorage.setItem(PAYMENT_MODE_TYPE, paymentMode);
    this.setState({ paymentModeSelected: paymentMode });
    this.props.binValidation(paymentMode, binNo);
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
    this.setState({ paymentModeSelected: paymentMode });
    this.props.binValidationForCOD(paymentMode);
  };

  binValidationForNetBank = (paymentMode, bankName) => {
    localStorage.setItem(PAYMENT_MODE_TYPE, paymentMode);
    this.setState({ paymentModeSelected: paymentMode });
    this.props.binValidationForNetBanking(paymentMode, bankName);
  };

  binValidationForSavedCard = cardDetails => {
    this.setState({
      paymentModeSelected: `${cardDetails.cardType} Card`
    });
    localStorage.setItem(PAYMENT_MODE_TYPE, `${cardDetails.cardType} Card`);
    this.setState({ savedCardDetails: cardDetails });
    this.props.binValidation(
      `${cardDetails.cardType} Card`,
      cardDetails.cardISIN
    );
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

  jusPayTokenizeForGiftCard = cartDetails => {
    if (this.props.jusPayTokenizeForGiftCard) {
      this.props.jusPayTokenizeForGiftCard(
        cartDetails,
        this.state.paymentModeSelected,
        this.props.location.state.egvCartGuid
      );
    }
  };
  createJusPayOrderForGiftCardNetBanking = bankName => {
    if (this.props.createJusPayOrderForGiftCardNetBanking) {
      this.props.createJusPayOrderForGiftCardNetBanking(
        bankName,
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
  render() {
    if (
      this.props.cart.paymentModesStatus === REQUESTING ||
      this.props.cart.codEligibilityStatus === REQUESTING ||
      this.props.netBankDetailsStatus === REQUESTING ||
      this.props.cart.getUserAddressStatus === REQUESTING
    ) {
      return this.renderLoader();
    } else {
      if (
        this.props.cart.loading ||
        this.props.cart.jusPaymentLoader ||
        this.props.cart.selectDeliveryModeLoader
      ) {
        this.props.showSecondaryLoader();
      } else {
        this.props.hideSecondaryLoader();
      }
    }
    if (this.props.cart.transactionStatus === REQUESTING) {
      return false;
    }

    const cartData = this.props.cart;
    let deliveryCharge = 0;
    let couponDiscount = 0;
    let totalDiscount = 0;
    if (
      this.props.cart &&
      this.props.cart.cartDetailsCNC &&
      this.props.cart.cartDetailsCNC.products
    ) {
      if (
        this.props.cart.cartDetailsCNC.products &&
        this.props.cart.cartDetailsCNC.products[0].elligibleDeliveryMode &&
        this.props.cart.cartDetailsCNC.products[0].elligibleDeliveryMode[0] &&
        this.props.cart.cartDetailsCNC.products[0].elligibleDeliveryMode[0]
          .charge
      ) {
        deliveryCharge =
          Math.round(
            this.props.cart.cartDetailsCNC.products[0].elligibleDeliveryMode[0]
              .charge.value * 100
          ) / 100;
      }
      if (this.props.cart.cartDetailsCNC.cartAmount.totalDiscountAmount) {
        totalDiscount =
          Math.round(
            this.props.cart.cartDetailsCNC.cartAmount.totalDiscountAmount
              .value * 100
          ) / 100;
      }

      if (this.props.cart.cartDetailsCNC.cartAmount.couponDiscountAmount) {
        couponDiscount =
          Math.round(
            this.props.cart.cartDetailsCNC.cartAmount.couponDiscountAmount
              .value * 100
          ) / 100;
      }
    }
    if (
      this.state.addNewAddress &&
      !this.state.orderConfirmation &&
      !this.state.isGiftCard
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
            displayToast={message => this.props.displayToast(message)}
          />
        </div>
      );
    } else if (
      (!this.state.addNewAddress &&
        this.props.cart &&
        !this.state.orderConfirmation) ||
      this.state.isGiftCard
    ) {
      return (
        <div className={styles.base}>
          {!this.state.confirmAddress &&
            !this.state.isGiftCard &&
            (cartData.userAddress && cartData.userAddress.addresses
              ? this.renderCheckoutAddress()
              : this.renderInitialAddAddressForm())}

          {this.state.confirmAddress &&
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

          {this.props.cart.cartDetailsCNC &&
            this.state.confirmAddress &&
            !this.state.deliverMode &&
            !this.state.isGiftCard &&
            (this.state.showCliqAndPiq
              ? this.renderCliqAndPiq()
              : this.renderDeliverModes())}

          {this.state.deliverMode &&
            !this.state.isGiftCard && (
              <div className={styles.deliveryAddress}>
                <DeliveryModeSet
                  productDelivery={this.props.cart.cartDetailsCNC.products}
                  changeDeliveryModes={() => this.changeDeliveryModes()}
                  selectedDeliveryDetails={this.state.ussIdAndDeliveryModesObj}
                />
              </div>
            )}

          {!this.state.appliedCoupons &&
            (this.state.confirmAddress && this.state.deliverMode) &&
            this.props.cart.paymentModes &&
            !this.state.isGiftCard &&
            this.renderBankOffers()}

          {((!this.state.paymentMethod &&
            (this.state.confirmAddress && this.state.deliverMode)) ||
            this.state.isGiftCard) && (
            <div className={styles.paymentCardHolder}>
              <PaymentCardWrapper
                isRemainingBalance={this.state.isRemainingAmount}
                isFromGiftCard={this.state.isGiftCard}
                cart={this.props.cart}
                cliqCashAmount={this.state.cliqCashAmount}
                applyCliqCash={() => this.applyCliqCash()}
                removeCliqCash={() => this.removeCliqCash()}
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
                jusPayTokenizeForGiftCard={cardDetails =>
                  this.jusPayTokenizeForGiftCard(cardDetails)
                }
                createJusPayOrderForGiftCardNetBanking={bankName =>
                  this.createJusPayOrderForGiftCardNetBanking(bankName)
                }
                addGiftCard={() => this.addGiftCard()}
                binValidationForPaytm={val => this.binValidationForPaytm(val)}
                displayToast={message => this.props.displayToast(message)}
              />
            </div>
          )}

          <Checkout
            label={
              (this.state.confirmAddress && !this.state.deliverMode) ||
              this.state.isGiftCard
                ? PROCEED
                : CONTINUE
            }
            amount={this.state.payableAmount}
            bagTotal={this.state.bagAmount}
            payable={this.state.payableAmount}
            coupons={`Rs. ${couponDiscount}`}
            discount={`Rs. ${totalDiscount}`}
            delivery={`Rs. ${deliveryCharge}`}
            onCheckout={this.handleSubmit}
          />
        </div>
      );
    } else if (this.state.orderConfirmation) {
      return (
        <div>
          {this.props.cart.orderConfirmationDetails && (
            <div className={styles.orderConfirmationHolder}>
              <OrderConfirmation
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
