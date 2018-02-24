import React from "react";
import CheckoutFrame from "./CheckoutFrame";
import CartItem from "./CartItem";
import DummyTab from "./DummyTab";
import CheckOutHeader from "./CheckOutHeader";
import DeliveryAddressSet from "./DeliveryAddressSet";
import styles from "./CheckoutDeliveryMode.css";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
export default class CheckoutDeliveryMode extends React.Component {
  componentDidMount() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
    if (userDetails) {
      this.props.getCartDetailsCNC(
        JSON.parse(userDetails).customerInfo.mobileNumber,
        JSON.parse(customerCookie).access_token,
        JSON.parse(cartDetailsLoggedInUser).code
      );
    } else {
      this.props.getCartDetailsCNC(
        ANONYMOUS_USER,
        JSON.parse(globalCookie).access_token,
        JSON.parse(cartDetailsAnonymous).guid
      );
    }
  }
  render() {
    console.log("comes in checkout");
    const cartDetails = this.props.cart.cart.cartDetails;
    console.log(this.props.cart.cart);
    console.log(cartDetails);
    const selectedAddress = cartDetails.addressDetailsList
      ? cartDetails.addressDetailsList.addresses.filter(val => {
          return val.defaultAddress;
        })[0]
      : [];
    console.log(cartDetails.addressDetailsList.addresses);
    console.log(selectedAddress);
    return (
      <CheckoutFrame>
        <DeliveryAddressSet
          address={selectedAddress.line1}
          addressType={selectedAddress.addressType}
        />
        <div className={styles.products}>
          <div className={styles.header}>
            <CheckOutHeader
              indexNumber="2"
              confirmTitle="Choose delivery mode"
            />
          </div>
          {cartDetails.products &&
            cartDetails.products.map((val, i) => {
              return (
                <div className={styles.row}>
                  <CartItem
                    key={i}
                    productImage={val.imageURL}
                    hasFooter={false}
                    productDetails={val.description}
                    productName={val.productName}
                    price={val.offerPrice}
                    deliveryInformation={val.elligibleDeliveryMode}
                    showDelivery={true}
                    deliveryInfoToggle={false}
                    selectDeliveryMode={val => console.log(val)}
                  />
                </div>
              );
            })}
        </div>
        <DummyTab title="Payment Method" number="3" />
      </CheckoutFrame>
    );
  }
}
