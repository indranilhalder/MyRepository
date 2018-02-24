import React from "react";
import CheckoutFrame from "./CheckoutFrame";
import CartItem from "./CartItem";
import DummyTab from "./DummyTab";
import DeliveryAddressSet from "./DeliveryAddressSet";
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
    console.log(this.props);
    const selectedAddress = this.props.addressDetailsList
      ? this.props.addressDetailsList.filter(val => {
          return val.defaultAddress;
        })
      : [];
    return (
      <CheckoutFrame>
        <DeliveryAddressSet
          selectedAddress={selectedAddress.line1}
          addressType={selectedAddress.addressType}
        />
        {this.props.products &&
          this.props.products.map((val, i) => {
            return (
              <CartItem
                key={i}
                productImage={val.imageURL}
                productDetails={val.description}
                productName={val.productName}
                price={val.priceValue.sellingPrice.formattedValue}
                deliveryInformation={val.elligibleDeliveryMode}
                deliverTime={val.elligibleDeliveryMode[0].desc}
                option={[
                  {
                    value: val.qtySelectedByUser,
                    label: val.qtySelectedByUser
                  }
                ]}
              />
            );
          })}
        <DummyTab title="Payment Method" indexNumber="3" />
      </CheckoutFrame>
    );
  }
}
