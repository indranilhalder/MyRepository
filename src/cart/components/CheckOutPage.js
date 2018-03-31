import React from "react";
import cloneDeep from "lodash/cloneDeep";
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
import filter from "lodash/filter";
import OrderConfirmation from "./OrderConfirmation";
import queryString from "query-string";
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
  WALLET
} from "../../lib/constants";
import { HOME_ROUTER, SUCCESS } from "../../lib/constants";
import MDSpinner from "react-md-spinner";
const SEE_ALL_BANK_OFFERS = "See All Bank Offers";
const PAYMENT_CHARGED = "CHARGED";
const PAYMENT_MODE = "EMI";
const NET_BANKING = "NB";
const CART_GU_ID = "cartGuid";

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
    selectedDeliveryDetails: "",
    ratingExperience: false
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

  handleSelectDeliveryMode(deliveryMode, ussId, cartId) {
    let newDeliveryObj = {};
    newDeliveryObj[ussId] = deliveryMode;
    let currentSelectedDeliveryModes = cloneDeep(
      this.state.ussIdAndDeliveryModesObj
    );
    if (!currentSelectedDeliveryModes[ussId]) {
      Object.assign(currentSelectedDeliveryModes, newDeliveryObj);
      this.setState({ ussIdAndDeliveryModesObj: currentSelectedDeliveryModes });
    }
    let details = filter(this.props.cart.cartDetailsCNC.products, product => {
      return product.USSID === ussId;
    });

    if (details) {
      let SelectedDeliveryDetails = filter(
        details[0].elligibleDeliveryMode,
        elgibleDeliverMode => {
          return elgibleDeliverMode.code === deliveryMode;
        }
      );
      this.setState({ selectedDeliveryDetails: SelectedDeliveryDetails[0] });
    }
  }

  getAllStores = selectedProductsUssIdForCliqAndPiq => {
    this.setState({ showCliqAndPiq: true, selectedProductsUssIdForCliqAndPiq });
    this.props.getAllStoresCNC(this.state.selectedAddress.postalCode);
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
    return (
      <div className={styles.addInitialAddAddress}>
        <ConfirmAddress
          address={
            cartData.userAddress.addresses &&
            cartData.userAddress.addresses.map(address => {
              return {
                addressTitle: address.addressType,
                addressDescription: `${address.line1} ${address.town} ${
                  address.city
                }, ${address.state} ${address.postalCode}`,
                value: address.id,
                selected: address.defaultAddress
              };
            })
          }
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
        return slaves.CNCServiceableSlavesData.map(slave => {
          return slave.serviceableSlaves.map(serviceableSlave => {
            return serviceableSlave;
          });
        });
      })
      .map(val => {
        return val.map(v => {
          return v;
        });
      });

    const allStoreIds = [].concat
      .apply([], [].concat.apply([], someData))
      .map(store => {
        return store.slaveId;
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
    this.setState({ confirmAddress: false });
  };

  componentWillReceiveProps(nextProps) {
    if (nextProps.cart.cliqCashPaymentDetails) {
      if (
        this.state.isRemainingAmount !==
        nextProps.cart.cliqCashPaymentDetails.isRemainingAmount
      ) {
        this.setState({
          isRemainingAmount:
            nextProps.cart.cliqCashPaymentDetails.isRemainingAmount,
          payableAmount: nextProps.cart.cliqCashPaymentDetails.paybleAmount,
          cliqCashAmount:
            nextProps.cart.cliqCashPaymentDetails.cliqCashBalance.value,
          bagAmount: nextProps.cart.cartDetailsCNC.cartAmount.bagTotal.value
        });
      }
    } else if (this.state.isGiftCard) {
      this.setState({
        isRemainingAmount: true,
        payableAmount: this.props.location.state.amount,
        bagAmount: this.props.location.state.amount
      });
    } else {
      if (nextProps.cart.cartDetailsCNC && this.state.isRemainingAmount) {
        let cliqCashAmount = 0;
        if (nextProps.cart.paymentModes) {
          cliqCashAmount =
            nextProps.cart.paymentModes.cliqCash.totalCliqCashBalance.value;
        }
        this.setState({
          payableAmount:
            nextProps.cart.cartDetailsCNC.cartAmount.paybleAmount
              .formattedValue,
          cliqCashAmount: cliqCashAmount,
          bagAmount: nextProps.cart.cartDetailsCNC.cartAmount.bagTotal.value
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
    } else if (this.props.location.state.isFromGiftCard) {
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
        let guIdObject = new FormData();
        guIdObject.append(CART_GU_ID, JSON.parse(cartDetailsLoggedInUser).guid);
        this.props.getPaymentModes(guIdObject);
        this.props.getPaymentModes(guIdObject);
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
    // here we are checking the if user selected any address then setting our state
    // and in else condition if user deselect then this function will again call and
    //  then we are resetting the previous selected address
    if (selectedAddress[0]) {
      this.setState({
        confirmAddress: false,
        selectedAddress: addressSelected[0]
      });
      this.setState({ addressId: addressSelected });
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
  handleSubmit = () => {
    if (!this.state.confirmAddress && !this.state.isGiftCard) {
      this.props.addAddressToCart(
        this.state.addressId[0].id,
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
          this.state.selectedAddress.postalCode
        );
        // this.props.getOrderSummary(this.state.selectedAddress.postalCode);
      }
      this.setState({ deliverMode: true });
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
          this.state.addressId[0],
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
      this.state.addressId[0],
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

  render() {
    if (this.props.cart.loading) {
      return <div className={styles.base}>{this.renderLoader()}</div>;
    }
    const cartData = this.props.cart;
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
            onChange={val => this.onChange(val)}
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
                  addressType={this.state.addressId[0].addressType}
                  address={this.state.addressId[0].line1}
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
                  selectedDeliveryDetails={this.state.selectedDeliveryDetails}
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
              />
            </div>
          )}

          {(this.state.isGiftCard || !this.state.showCliqAndPiq) && (
            <Checkout
              amount={this.state.payableAmount}
              bagTotal={this.state.bagAmount}
              tax={this.props.tax}
              offers={this.props.offers}
              delivery={this.props.delivery}
              payable={this.state.payableAmount}
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
                orderId={this.props.cart.orderConfirmationDetails.orderRefNo}
                captureOrderExperience={rating =>
                  this.captureOrderExperience(rating)
                }
                orderStatusMessage={
                  this.props.cart.orderConfirmationDetails.orderStatusMessage
                }
                continueShopping={() => this.continueShopping()}
              />
            </div>
          )}
          {this.props.cart.cliqCashJusPayDetails && (
            <div>
              <OrderConfirmation
                orderId={this.props.cart.cliqCashJusPayDetails.orderId}
                orderStatusMessage={this.props.orderConfirmationText}
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

CheckOutPage.defaultProps = {
  cartTax: "included",
  delivery: "Free",
  offers: "Apply",
  orderConfirmationText: "Your Order Successfully Placed"
};
