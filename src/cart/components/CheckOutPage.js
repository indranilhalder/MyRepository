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
import _ from "lodash";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER
} from "../../lib/constants";
const SEE_ALL_BANK_OFFERS = "See All Bank Offers";
class CheckOutPage extends React.Component {
  state = {
    confirmAddress: false,
    deliverMode: false,
    paymentMethod: false,
    addressId: null,
    addNewAddress: false,
    deliverModeUssId: "",
    appliedCoupons: false
  };

  renderConfirmAddress = () => {
    if (this.state.confirmAddress) {
      return <div> Address Expand</div>;
    }
  };

  handleSelectDeliveryMode(code, id, cartId) {
    let deliveryModesUssId = `"${code}":"${id}"`;

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
        {this.props.cart.cartDetailsCnc.products &&
          this.props.cart.cartDetailsCnc.products.map((val, i) => {
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
    if (nextProps.cart.justPayPaymentDetails) {
      this.props.history.push(
        nextProps.cart.jusPayDetails.payment.authentication.url
      );
    }
  }
  componentDidMount() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );

    this.props.getCartDetailsCNC(
      JSON.parse(userDetails).customerInfo.mobileNumber,
      JSON.parse(customerCookie).access_token,
      JSON.parse(cartDetailsLoggedInUser).code,
      this.props.location.state.pinCode,
      false
    );
    this.props.getUserAddress(this.props.location.state.pinCode);
    this.props.getPaymentModes();
    this.props.getNetBankDetails();
  }

  onSelectAddress(selectedAddress) {
    let addressSelected = _.filter(
      this.props.cart.cartDetailsCnc.addressDetailsList.addresses,
      address => {
        return address.id === selectedAddress[0];
      }
    );
    console.log(addressSelected[0].id);
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
    console.log(this.state);
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
  };

  addAddress = address => {
    if (this.props.addUserAddress) {
      this.props.addUserAddress(address);
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
    this.props.binValidation(paymentMode, binNo);
  };

  softReservationForPayment = cardDetails => {
    cardDetails.pinCode = "110001";
    console.log(this.state.addressId[0]);
    this.props.softReservationForPayment(cardDetails, this.state.addressId[0]);
  };
  render() {
    console.log(this.props.cart);
    const cartData = this.props.cart;
    if (this.state.addNewAddress || !cartData.userAddress) {
      return (
        <AddDeliveryAddress
          addUserAddress={address => this.addAddress(address)}
          {...this.state}
          onChange={val => this.onChange(val)}
        />
      );
    }
    if (!this.state.addNewAddress && this.props.cart) {
      return (
        <div>
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

          {this.props.cart.cartDetailsCnc &&
            this.state.confirmAddress &&
            !this.state.deliverMode &&
            this.renderDeliverModes()}

          {this.state.deliverMode && (
            <DeliveryModeSet
              productDelivery={this.props.cart.cartDetailsCnc.products}
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
                softReservationForPayment={pinCode =>
                  this.softReservationForPayment(pinCode)
                }
              />
            )}

          {this.props.cart.cartDetailsCnc && (
            <Checkout
              amount={this.props.cart.cartDetailsCnc.totalPrice}
              totalDiscount={
                this.props.cart.cartDetailsCnc.cartAmount.totalDiscountAmount
                  .formattedValue
              }
              bagTotal={
                this.props.cart.cartDetailsCnc.cartAmount.bagTotal
                  .formattedValue
              }
              tax={this.props.tax}
              offers={this.props.offers}
              delivery={this.props.delivery}
              payable={this.props.cart.cartDetailsCnc.totalPrice}
              onCheckout={this.handleSubmit}
            />
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
