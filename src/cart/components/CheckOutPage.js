import React from "react";
import PropTypes from "prop-types";
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
import filter from "lodash/filter";
import OrderConfirmation from "./OrderConfirmation";
import queryString from "query-string";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  CART_DETAILS_FOR_LOGGED_IN_USER
} from "../../lib/constants";
import { HOME_ROUTER, SUCCESS } from "../../lib/constants";
import MDSpinner from "react-md-spinner";
const SEE_ALL_BANK_OFFERS = "See All Bank Offers";
const PAYMENT_CHARGED = "CHARGED";
const PAYMENT_MODE = "EMI";

class CheckOutPage extends React.Component {
  state = {
    confirmAddress: false,
    deliverMode: false,
    paymentMethod: false,
    addressId: null,
    addNewAddress: false,
    deliverModeUssId: "",
    appliedCoupons: false,
    paymentModeSelected: null,
    orderConfirmation: false,

    orderId: "",
    savedCardDetails: "",

    binValidationCOD: false
  };

  renderLoader() {
    return (
      <div className={styles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  }

  renderConfirmAddress = () => {
    if (this.state.confirmAddress) {
      return <div> Address Expand</div>;
    }
  };

  handleSelectDeliveryMode(code, id, cartId) {
    let deliveryModesUssId = `"${id}":"${code}"`;

    if (this.state.deliverModeUssId) {
      this.setState({
        deliverModeUssId: `${this.state.deliverModeUssId},${deliveryModesUssId}`
      });
    } else {
      this.setState({
        deliverModeUssId: `${deliveryModesUssId}`
      });
    }
  }

  getAllStores = val => {
    this.props.getAllStoresCNC(this.props.location.state.pinCode);
    this.props.addStoreCNC("273544ASB001", "273544 - 110003");
    this.props.addPickupPersonCNC("7503721061", "Suraj Kumars");
  };

  renderCheckoutAddress = () => {
    const cartData = this.props.cart;
    return (
      <ConfirmAddress
        address={cartData.userAddress.addresses.map(address => {
          return {
            addressTitle: address.addressType,
            addressDescription: `${address.line1} ${address.town} ${
              address.city
            }, ${address.state} ${address.postalCode}`,
            value: address.id,
            selected: address.defaultAddress
          };
        })}
        onNewAddress={() => this.addNewAddress()}
        onSelectAddress={address => this.onSelectAddress(address)}
      />
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
                  productImage={val.imageURL}
                  hasFooter={false}
                  productDetails={val.productBrand}
                  productName={val.productName}
                  price={val.offerPrice}
                  deliveryInformation={val.elligibleDeliveryMode}
                  showDelivery={true}
                  deliveryInfoToggle={false}
                  selectDeliveryMode={code =>
                    this.handleSelectDeliveryMode(
                      code,
                      val.USSID,
                      this.state.cartId
                    )
                  }
                  onPiq={val => this.getAllStores(val)}
                />
              </div>
            );
          })}
      </div>
    );
  };

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

  changeDeliveryAddress = () => {
    this.setState({ confirmAddress: false });
  };

  componentWillReceiveProps(nextProps) {
    if (nextProps.cart.justPayPaymentDetails !== null) {
      if (nextProps.cart.justPayPaymentDetails.payment) {
        window.location.replace(
          nextProps.cart.justPayPaymentDetails.payment.authentication.url
        );
      }
    }
    if (nextProps.cart.orderConfirmationDetailsStatus === SUCCESS) {
      Cookie.deleteCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
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
        let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
        let cartId = JSON.parse(cartDetails).guid;
        if (cartId) {
          this.props.updateTransactionDetails(
            this.state.paymentModeSelected,
            orderId,
            cartId
          );
        }
      }
    } else {
      if (this.props.getCartDetailsCNC) {
        let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
        let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
        let cartDetailsLoggedInUser = Cookie.getCookie(
          CART_DETAILS_FOR_LOGGED_IN_USER
        );

        this.props.getCartDetailsCNC(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token,
          JSON.parse(cartDetailsLoggedInUser).code,
          this.props.location.state.pinCode,
          false
        );
        this.props.getUserAddress(this.props.location.state.pinCode);
        this.props.getPaymentModes();
        this.props.getCODEligibility();
        this.props.getNetBankDetails();
        this.props.getEmiBankDetails(this.props.location.state.productValue);
      }
    }
  }

  onSelectAddress(selectedAddress) {
    let addressSelected = filter(
      this.props.cart.cartDetailsCNC.addressDetailsList.addresses,
      address => {
        return address.id === selectedAddress[0];
      }
    );

    this.setState({ confirmAddress: false });
    this.setState({ addressId: addressSelected });
  }

  changeDeliveryModes = () => {
    this.setState({ deliverMode: false });
  };

  onChange(val) {
    this.setState(val);
  }
  handleSubmit = () => {
    if (!this.state.confirmAddress) {
      this.props.addAddressToCart(
        this.state.addressId[0].id,
        this.props.location.state.pinCode
      );
      this.setState({ confirmAddress: true });
    }
    if (!this.state.deliverMode && this.state.confirmAddress) {
      if (this.props.selectDeliveryMode) {
        this.props.selectDeliveryMode(
          this.state.deliverModeUssId,
          this.props.location.state.pinCode
        );
      }
      this.setState({ deliverMode: true });
    }

    if (this.state.savedCardDetails !== "") {
      this.props.softReservationPaymentForSavedCard(
        this.state.savedCardDetails,
        this.state.addressId[0],
        this.state.paymentModeSelected
      );
    }

    if (this.state.binValidationCOD) {
      this.softReservationForCODPayment();
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
    this.setState({ paymentModeSelected: paymentMode });
    this.props.binValidation(paymentMode, binNo);
  };

  binValidationForCOD = paymentMode => {
    this.setState({ paymentModeSelected: paymentMode });
    this.props.binValidationForCOD(paymentMode);
  };

  binValidationForNetBank = (paymentMode, bankName) => {
    this.setState({ paymentModeSelected: paymentMode });
    this.props.binValidationForNetBanking(paymentMode, bankName);
  };

  binValidationForSavedCard = cardDetails => {
    this.setState({
      paymentModeSelected: `${cardDetails.cardType} Card`
    });
    this.setState({ savedCardDetails: cardDetails });
    this.props.binValidation(
      `${cardDetails.cardType} Card`,
      cardDetails.cardISIN
    );
  };

  softReservationForPayment = cardDetails => {
    cardDetails.pinCode = this.props.location.state.pinCode;
    this.props.softReservationForPayment(
      cardDetails,
      this.state.addressId[0],
      this.state.paymentModeSelected
    );
  };

  softReservationPaymentForNetBanking = bankName => {
    this.props.softReservationPaymentForNetBanking(
      this.state.paymentModeSelected,
      bankName,
      this.props.location.state.pinCode
    );
  };

  captureOrderExperience = rating => {
    if (this.props.captureOrderExperience) {
      this.props.captureOrderExperience(this.state.orderId, rating);
    }
  };

  continueShopping = () => {
    this.props.history.index = 0;
    this.props.history.push(HOME_ROUTER);
  };

  softReservationForCODPayment = () => {
    this.props.softReservationForCODPayment(this.props.location.state.pinCode);
  };

  render() {
    if (this.props.cart.loading) {
      return <div className={styles.base}>{this.renderLoader()}</div>;
    }
    const cartData = this.props.cart;
    if (
      (this.state.addNewAddress || !cartData.userAddress) &&
      !this.state.orderConfirmation
    ) {
      return (
        <div className={styles.base}>
          <AddDeliveryAddress
            addUserAddress={address => this.addAddress(address)}
            {...this.state}
            onChange={val => this.onChange(val)}
          />
        </div>
      );
    } else if (
      !this.state.addNewAddress &&
      this.props.cart &&
      !this.state.orderConfirmation
    ) {
      return (
        <div className={styles.base}>
          {cartData.userAddress &&
            !this.state.confirmAddress &&
            this.renderCheckoutAddress()}

          {this.state.confirmAddress && (
            <DeliveryAddressSet
              addressType={this.state.addressId[0].addressType}
              address={this.state.addressId[0].line1}
              changeDeliveryAddress={() => this.changeDeliveryAddress()}
            />
          )}

          {this.props.cart.cartDetailsCNC &&
            this.state.confirmAddress &&
            !this.state.deliverMode &&
            this.renderDeliverModes()}

          {this.state.deliverMode && (
            <DeliveryModeSet
              productDelivery={this.props.cart.cartDetailsCNC.products}
              changeDeliveryModes={() => this.changeDeliveryModes()}
            />
          )}

          {!this.state.appliedCoupons &&
            (this.state.confirmAddress && this.state.deliverMode) &&
            this.props.cart.paymentModes &&
            this.renderBankOffers()}

          {!this.state.paymentMethod &&
            (this.state.confirmAddress && this.state.deliverMode) && (
              <PaymentCardWrapper
                cart={this.props.cart}
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
              />
            )}

          {this.props.cart.cartDetailsCNC && (
            <Checkout
              amount={this.props.cart.cartDetailsCNC.totalPrice}
              totalDiscount={
                this.props.cart.cartDetailsCNC.cartAmount.totalDiscountAmount
                  .formattedValue
              }
              bagTotal={
                this.props.cart.cartDetailsCNC.cartAmount.bagTotal
                  .formattedValue
              }
              tax={this.props.tax}
              offers={this.props.offers}
              delivery={this.props.delivery}
              payable={this.props.cart.cartDetailsCNC.totalPrice}
              onCheckout={this.handleSubmit}
            />
          )}
        </div>
      );
    } else if (this.state.orderConfirmation) {
      return (
        <div>
          {this.props.cart.orderConfirmationDetails && (
            <div>
              <OrderConfirmation
                orderDetails={this.props.cart.orderConfirmationDetails}
                captureOrderExperience={rating =>
                  this.captureOrderExperience(rating)
                }
                continueShopping={() => this.continueShopping()}
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
