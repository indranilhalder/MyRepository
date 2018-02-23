import React from "react";
import CheckoutFrame from "./CheckoutFrame";
import DeliveryModeSet from "./DeliveryModeSet";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
export default class extends React.Component {
  componentDidMount() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
    if (userDetails) {
      this.props.getCartDetails(
        JSON.parse(userDetails).customerInfo.mobileNumber,
        JSON.parse(customerCookie).access_token,
        JSON.parse(cartDetailsLoggedInUser).code
      );
    } else {
      this.props.getCartDetails(
        ANONYMOUS_USER,
        JSON.parse(globalCookie).access_token,
        JSON.parse(cartDetailsAnonymous).guid
      );
    }
  }
  render() {
    console.log(this.props);
    return (
      <CheckoutFrame>
        <DeliveryModeSet
          productDelivery={[
            {
              productName: "Apple iPhone 7 upgraded vrson with gorila glass",
              deliveryWay: " Express Shipping"
            },
            {
              productName: "Apple iPhone 7 upgraded vrson with gorila glass",
              deliveryWay: "Free home delivery"
            }
          ]}
        />
      </CheckoutFrame>
    );
  }
}
