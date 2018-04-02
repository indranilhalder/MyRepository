import React, { Component } from "react";
import ModalContainer from "./general/containers/ModalContainer";
import ToastContainer from "./general/containers/ToastContainer";
import { Route, Switch, Redirect } from "react-router-dom";
import { default as AppStyles } from "./App.css";
import Auth from "./auth/components/MobileAuth.js";
import HomeContainer from "./home/containers/HomeContainer.js";
import ProductListingsContainer from "./plp/containers/ProductListingsContainer";
import ProductDescriptionContainer from "./pdp/containers/ProductDescriptionContainer";
import ProductDescriptionPageWrapperContainer from "./pdp/containers/ProductDescriptionPageWrapperContainer";
import ProductReviewContainer from "./pdp/containers/ProductReviewContainer";
import LoginContainer from "./auth/containers/LoginContainer";
import SignUpContainer from "./auth/containers/SignUpContainer.js";
import FilterContainer from "./plp/containers/FilterContainer";
import BrandsLandingPageDefaultContainer from "./blp/containers/BrandsLandingPageDefaultContainer";
import ProductSellerContainer from "./pdp/containers/ProductSellerContainer";
import CheckoutAddressContainer from "./cart/containers/CheckoutAddressContainer";
import CartContainer from "./cart/containers/CartContainer";
import DeliveryModesContainer from "./cart/containers/DeliveryModesContainer";
import CategoriesPageContainer from "./clp/containers/CategoriesPageContainer";
import PlpBrandCategoryWrapperContainer from "./plp/containers/PlpBrandCategoryWrapperContainer";
import DisplayOrderSummaryContainer from "./cart/containers/DisplayOrderSummaryContainer";
import CheckOutContainer from "./cart/containers/CheckOutContainer";
import BrandLandingPageContainer from "./blp/containers/BrandLandingPageContainer";
import MobileFooter from "./general/components/MobileFooter.js";
// importing All container for my Accounts
import MyAccountContainer from "./account/containers/MyAccountContainer";
import UserAlertsAndCouponsContainer from "./account/containers/UserAlertsAndCouponsContainer";

import MyAccountBrandsContainer from "./account/containers/MyAccountBrandsContainer";
import * as Cookie from "./lib/Cookie";
import MDSpinner from "react-md-spinner";
import HeaderContainer from "./general/containers/HeaderContainer.js";
import AllOrderContainer from "./account/containers/AllOrderContainer";
import SavedCardContainer from "./account/containers/SavedCardContainer.js";
import OrderDetailsContainer from "./account/containers/OrderDetailsContainer.js";
import AddressBookContainer from "./account/containers/AddressBookContainer.js";

import ReturnFlowContainer from "./account/containers/ReturnFlowContainer.js";

import EditAddressBookContainer from "./account/containers/EditAddressBookContainer.js";
import AddAddressContainer from "./account/containers/AddAddressContainer.js";
import SaveListContainer from "./account/containers/SaveListContainer";
import CliqCashContainer from "./account/containers/CliqCashContainer.js";
import GiftCardContainer from "./account/containers/GiftCardContainer";

import PlpBrandCategoryWrapper from "./plp/components/PlpBrandCategoryWrapper";
import CancelOrderContainer from "./account/containers/CancelOrderContainer";

import {
  HOME_ROUTER,
  PRODUCT_LISTINGS,
  MAIN_ROUTER,
  LOGIN_PATH,
  SIGN_UP_PATH,
  PRODUCT_DELIVERY_ADDRESSES,
  PRODUCT_FILTER_ROUTER,
  PRODUCT_CART_ROUTER,
  GLOBAL_ACCESS_TOKEN,
  CUSTOMER_ACCESS_TOKEN,
  REFRESH_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  LOGGED_IN_USER_DETAILS,
  PRODUCT_CART_DELIVERY_MODES,
  ORDER_SUMMARY_ROUTER,
  CHECKOUT_ROUTER,
  PRODUCT_DESCRIPTION_PRODUCT_CODE,
  BRAND_LANDING_PAGE,
  PRODUCT_DESCRIPTION_REVIEWS,
  PRODUCT_OTHER_SELLER_ROUTER,
  DEFAULT_BRANDS_LANDING_PAGE,
  CATEGORIES_LANDING_PAGE,
  BRAND_PAGE,
  CATEGORY_PAGE,
  BRAND_PAGE_WITH_SLUG,
  CATEGORY_PAGE_WITH_SLUG,
  MY_ACCOUNT_ORDERS_PAGE,
  SAVE_LIST_PAGE,
  MY_ACCOUNT_PAGE,
  MY_ACCOUNT_SAVED_CARDS_PAGE,
  MY_ACCOUNT_ADDRESS_PAGE,
  MY_ACCOUNT_ALERTS_PAGE,
  MY_ACCOUNT_COUPON_PAGE,
  MY_ACCOUNT_BRANDS_PAGE,
  ACCOUNT_SAVED_CARD_ROUTER,
  MY_ACCOUNT_CLIQ_CASH_PAGE,
  ORDER_PREFIX,
  RETURNS,
  SHORT_URL_ORDER_DETAIL,
  MY_ACCOUNT_GIFT_CARD_PAGE,
  MY_ACCOUNT_ADDRESS_EDIT_PAGE,
  MY_ACCOUNT_ADDRESS_ADD_PAGE,
  CATEGORY_PAGE_WITH_QUERY_PARAMS,
  CATEGORY_PAGE_WITH_SLUG_WITH_QUERY_PARAMS,
  BRAND_PAGE_WITH_QUERY_PARAMS,
  BRAND_PAGE_WITH_SLUG_WITH_QUERY_PARAMS,
  CATEGORY_PRODUCT_LISTINGS_WITH_PAGE,
  BRAND_PRODUCT_LISTINGS_WITH_PAGE,
  STATIC_CATEGORY_PAGES,
  BRAND_AND_CATEGORY_PAGE,
  CANCEL_PREFIX,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_REVIEWS_WITH_SLUG
} from "../src/lib/constants";

const auth = {
  isAuthenticated: false
};
class App extends Component {
  async componentDidMount() {
    let globalAccessToken = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let customerAccessToken = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let cartIdForAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
    let loggedInUserDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsForLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );

    let cartDetailsForAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);

    // Case 1. THe user is not logged in.

    if (!globalAccessToken && !this.props.cart.loading) {
      await this.props.getGlobalAccessToken();
      globalAccessToken = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    }

    if (!customerAccessToken && localStorage.getItem(REFRESH_TOKEN)) {
      await this.props.refreshToken(localStorage.getItem(REFRESH_TOKEN));
      customerAccessToken = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    }

    if (customerAccessToken) {
      if (!cartDetailsForLoggedInUser && !this.props.cart.loading) {
        this.props.generateCartIdForLoggedInUser();
      }
    } else {
      if (!cartDetailsForAnonymous && globalAccessToken) {
        this.props.generateCartIdForAnonymous();
      }
    }
  }

  renderLoader() {
    return (
      <div className={AppStyles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  }

  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }
    return (
      <React.Fragment>
        <div className={className}>
          <HeaderContainer />
          <Switch>
            <Route
              exact
              path={CATEGORY_PRODUCT_LISTINGS_WITH_PAGE}
              component={ProductListingsContainer}
            />
            <Route
              exact
              path={BRAND_PRODUCT_LISTINGS_WITH_PAGE}
              component={ProductListingsContainer}
            />
            <Route
              exact
              path={LOGIN_PATH}
              render={routeProps => (
                <LoginContainer {...routeProps} {...this.props} />
              )}
            />
            <Route path={CANCEL_PREFIX} component={CancelOrderContainer} />
            <Route
              exact
              path={SIGN_UP_PATH}
              render={routeProps => (
                <SignUpContainer {...routeProps} {...this.props} />
              )}
            />
            <Route path={RETURNS} component={ReturnFlowContainer} />

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
              path={MY_ACCOUNT_PAGE}
              component={MyAccountContainer}
            />
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
              path={`${SHORT_URL_ORDER_DETAIL}`}
              component={OrderDetailsContainer}
            />
            <Route path={`${ORDER_PREFIX}`} component={OrderDetailsContainer} />

            <Route
              exact
              path={BRAND_AND_CATEGORY_PAGE}
              component={ProductListingsContainer}
            />

            <Route
              exact
              path={CATEGORY_PAGE}
              component={PlpBrandCategoryWrapperContainer}
            />

            <Route
              exact
              path={BRAND_PAGE}
              component={PlpBrandCategoryWrapperContainer}
            />

            <Route
              exact
              path={BRAND_PAGE_WITH_QUERY_PARAMS}
              component={PlpBrandCategoryWrapperContainer}
            />

            <Route
              exact
              path={CATEGORY_PAGE_WITH_QUERY_PARAMS}
              component={PlpBrandCategoryWrapperContainer}
            />

            <Route
              exact
              path={BRAND_PAGE_WITH_SLUG}
              component={PlpBrandCategoryWrapperContainer}
            />

            <Route
              exact
              path={BRAND_PAGE_WITH_SLUG_WITH_QUERY_PARAMS}
              component={PlpBrandCategoryWrapperContainer}
            />

            <Route
              strict
              path={CATEGORY_PAGE_WITH_SLUG}
              component={PlpBrandCategoryWrapperContainer}
            />

            <Route
              exact
              path={CATEGORY_PAGE_WITH_SLUG_WITH_QUERY_PARAMS}
              component={PlpBrandCategoryWrapperContainer}
            />

            <Route
              path={PRODUCT_DESCRIPTION_REVIEWS}
              component={ProductReviewContainer}
            />

            <Route
              path={PRODUCT_DESCRIPTION_REVIEWS_WITH_SLUG}
              component={ProductReviewContainer}
            />
            <Route
              path={PRODUCT_OTHER_SELLER_ROUTER}
              component={ProductSellerContainer}
            />
            <Route
              exact
              path={PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE}
              component={ProductDescriptionPageWrapperContainer}
            />
            <Route
              exact
              path={PRODUCT_DESCRIPTION_PRODUCT_CODE}
              component={ProductDescriptionPageWrapperContainer}
            />

            <Route
              exact
              path={PRODUCT_LISTINGS}
              component={ProductListingsContainer}
            />

            <Route exact path={HOME_ROUTER} component={HomeContainer} />
            <Route
              exact
              path={MAIN_ROUTER}
              render={routeProps => <Auth {...routeProps} {...this.props} />}
            />

            <Route
              exact
              path={PRODUCT_FILTER_ROUTER}
              component={FilterContainer}
            />
            <Route
              exact
              path={BRAND_LANDING_PAGE}
              component={BrandLandingPageContainer}
            />
            <Route
              exact
              path={PRODUCT_DELIVERY_ADDRESSES}
              component={CheckoutAddressContainer}
            />
            <Route
              exact
              path={PRODUCT_CART_DELIVERY_MODES}
              component={DeliveryModesContainer}
            />
            <Route
              exact
              path={ORDER_SUMMARY_ROUTER}
              component={DisplayOrderSummaryContainer}
            />
            <Route path={CHECKOUT_ROUTER} component={CheckOutContainer} />
            <Route exact path={PRODUCT_CART_ROUTER} component={CartContainer} />
            <Route
              exact
              path={DEFAULT_BRANDS_LANDING_PAGE}
              component={BrandsLandingPageDefaultContainer}
            />
            <Route
              exact
              path={CATEGORIES_LANDING_PAGE}
              component={CategoriesPageContainer}
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
            {/* This *has* to be at the bottom */}
            <Route
              exact
              path={STATIC_CATEGORY_PAGES}
              component={PlpBrandCategoryWrapperContainer}
            />
          </Switch>
          <MobileFooter />

          <ModalContainer />
          <ToastContainer />
        </div>
      </React.Fragment>
    );
  }
}

export default App;
