import React, { Component } from "react";
import { Switch, Route } from "react-router-dom";
import {
  MY_ACCOUNT_ORDERS_PAGE,
  SAVE_LIST_PAGE,
  MY_ACCOUNT_PAGE,
  MY_ACCOUNT_SAVED_CARDS_PAGE,
  MY_ACCOUNT_ADDRESS_PAGE,
  MY_ACCOUNT_GIFT_CARD_PAGE,
  MY_ACCOUNT_UPDATE_PROFILE_PAGE,
  MY_ACCOUNT_ALERTS_PAGE,
  MY_ACCOUNT_COUPON_PAGE,
  MY_ACCOUNT_BRANDS_PAGE,
  MY_ACCOUNT_CLIQ_CASH_PAGE,
  MY_ACCOUNT_ADDRESS_EDIT_PAGE,
  MY_ACCOUNT_ADDRESS_ADD_PAGE,
  ORDER_PREFIX
} from "../../lib/constants.js";
import AllOrderContainer from "../containers/AllOrderContainer";

import MyAccountContainer from "../containers/MyAccountContainer";
import UserAlertsAndCouponsContainer from "../containers/UserAlertsAndCouponsContainer";

import MyAccountBrandsContainer from "../containers/MyAccountBrandsContainer";
import UpdateProfileContainer from "../containers/UpdateProfileContainer.js";

import EditAddressBookContainer from "../containers/EditAddressBookContainer.js";
import AddAddressContainer from "../containers/AddAddressContainer.js";
import SaveListContainer from "../containers/SaveListContainer";
import CliqCashContainer from "../containers/CliqCashContainer.js";
import GiftCardContainer from "../containers/GiftCardContainer";
import SavedCardContainer from "../containers/SavedCardContainer.js";
import AddressBookContainer from "../containers/AddressBookContainer.js";
import OrderDetailsContainer from "../containers/OrderDetailsContainer.js";

export default class MyAccountWrapper extends React.Component {
  render() {
    return (
      <Switch>
        <Route exact path={MY_ACCOUNT_PAGE} component={MyAccountContainer} />
        <Route
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_SAVED_CARDS_PAGE}`}
          component={SavedCardContainer}
        />
        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ALERTS_PAGE}`}
          component={UserAlertsAndCouponsContainer}
        />
        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_COUPON_PAGE}`}
          component={UserAlertsAndCouponsContainer}
        />
        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_GIFT_CARD_PAGE}`}
          component={GiftCardContainer}
        />
        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_CLIQ_CASH_PAGE}`}
          component={CliqCashContainer}
        />

        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_BRANDS_PAGE}`}
          component={MyAccountBrandsContainer}
        />
        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_UPDATE_PROFILE_PAGE}`}
          component={UpdateProfileContainer}
        />

        <Route
          path={`${MY_ACCOUNT_PAGE}${SAVE_LIST_PAGE}`}
          component={SaveListContainer}
        />
        <Route
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ORDERS_PAGE}`}
          component={AllOrderContainer}
        />

        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ADDRESS_PAGE}`}
          component={AddressBookContainer}
        />
        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ADDRESS_EDIT_PAGE}`}
          component={EditAddressBookContainer}
        />
        <Route
          exact
          path={`${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ADDRESS_ADD_PAGE}`}
          component={AddAddressContainer}
        />
        <Route path={`${ORDER_PREFIX}`} component={OrderDetailsContainer} />
      </Switch>
    );
  }
}
