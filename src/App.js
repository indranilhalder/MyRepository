import React, { Component } from "react";
import ModalContainer from "./general/containers/ModalContainer";
import ToastContainer from "./general/containers/ToastContainer";
import { Switch } from "react-router-dom";
import Route from "./general/Route";
import { default as AppStyles } from "./App.css";
import Auth from "./auth/components/MobileAuth.js";
import HomeContainer from "./home/containers/HomeContainer.js";
import ErrorContainer from "./general/containers/ErrorContainer.js";
import HomeSkeleton from "./general/components/HomeSkeleton";
import MobileFooter from "./general/components/MobileFooter.js";
// importing All container for my Accounts
import StoryWidget from "./home/components/StoryWidget.js";
import * as Cookie from "./lib/Cookie";
import SecondaryLoader from "./general/components/SecondaryLoader";
import HeaderContainer from "./general/containers/HeaderContainer.js";
import StaticPageContainer from "./staticpage/containers/StaticPageContainer.js";
import PlpBrandCategoryWrapperContainer from "./plp/containers/PlpBrandCategoryWrapperContainer";

import SecondaryLoaderContainer from "./general/containers/SecondaryLoaderContainer.js";
import {
  HOME_ROUTER,
  PRODUCT_LISTINGS,
  MAIN_ROUTER,
  LOGIN_PATH,
  SIGN_UP_PATH,
  PRODUCT_DELIVERY_ADDRESSES,
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
  RETURNS,
  SHORT_URL_ORDER_DETAIL,
  CATEGORY_PAGE_WITH_QUERY_PARAMS,
  CATEGORY_PAGE_WITH_SLUG_WITH_QUERY_PARAMS,
  BRAND_PAGE_WITH_QUERY_PARAMS,
  BRAND_PAGE_WITH_SLUG_WITH_QUERY_PARAMS,
  CATEGORY_PRODUCT_LISTINGS_WITH_PAGE,
  BRAND_PRODUCT_LISTINGS_WITH_PAGE,
  SKU_PAGE,
  BRAND_AND_CATEGORY_PAGE,
  CANCEL_PREFIX,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_REVIEWS_WITH_SLUG,
  REQUESTING,
  MY_ACCOUNT,
  STATIC_PAGE,
  SKU_PAGE_FILTER,
  PRODUCT_LISTINGS_WITHOUT_SLASH
} from "../src/lib/constants";
import Loadable from "react-loadable";

const Loader = () => {
  return (
    <div className={AppStyles.loadingIndicator}>
      <SecondaryLoader />
    </div>
  );
};

const MyAccountWrapper = Loadable({
  loader: () => import("./account/components/MyAccountWrapper"),
  loading() {
    return <Loader />;
  }
});

const BrandLandingPageContainer = Loadable({
  loader: () => import("./blp/containers/BrandLandingPageContainer"),
  loading() {
    return <Loader />;
  }
});

const BrandsLandingPageDefaultContainer = Loadable({
  loader: () => import("./blp/containers/BrandsLandingPageDefaultContainer"),
  loading() {
    return <Loader />;
  }
});

const ProductListingsContainer = Loadable({
  loader: () => import("./plp/containers/ProductListingsContainer"),
  loading(error) {
    return <Loader />;
  }
});

const CancelOrderContainer = Loadable({
  loader: () => import("./account/containers/CancelOrderContainer"),
  loading() {
    return <Loader />;
  }
});

const ReturnFlowContainer = Loadable({
  loader: () => import("./account/containers/ReturnFlowContainer.js"),
  loading() {
    return <Loader />;
  }
});

const OrderDetailsContainer = Loadable({
  loader: () => import("./account/containers/OrderDetailsContainer.js"),
  loading() {
    return <Loader />;
  }
});

const DisplayOrderSummaryContainer = Loadable({
  loader: () => import("./cart/containers/DisplayOrderSummaryContainer"),
  loading() {
    return <Loader />;
  }
});

const CheckoutAddressContainer = Loadable({
  loader: () => import("./cart/containers/CheckoutAddressContainer"),
  loading() {
    return <Loader />;
  }
});

const DeliveryModesContainer = Loadable({
  loader: () => import("./cart/containers/DeliveryModesContainer"),
  loading() {
    return <Loader />;
  }
});

const CategoriesPageContainer = Loadable({
  loader: () => import("./clp/containers/CategoriesPageContainer"),
  loading() {
    return <Loader />;
  }
});

const LoginContainer = Loadable({
  loader: () => import("./auth/containers/LoginContainer"),
  loading() {
    return <Loader />;
  }
});

const SignUpContainer = Loadable({
  loader: () => import("./auth/containers/SignUpContainer.js"),
  loading() {
    return <Loader />;
  }
});

const ProductDescriptionPageWrapperContainer = Loadable({
  loader: () =>
    import("./pdp/containers/ProductDescriptionPageWrapperContainer"),
  loading() {
    return <Loader />;
  }
});

const CheckOutContainer = Loadable({
  loader: () => import("./cart/containers/CheckOutContainer"),
  loading() {
    return <Loader />;
  }
});

const CartContainer = Loadable({
  loader: () => import("./cart/containers/CartContainer"),
  loading() {
    return <Loader />;
  }
});

const ProductReviewContainer = Loadable({
  loader: () => import("./pdp/containers/ProductReviewContainer"),
  loading() {
    return <Loader />;
  }
});

const ProductSellerContainer = Loadable({
  loader: () => import("./pdp/containers/ProductSellerContainer"),
  loading() {
    return <Loader />;
  }
});

class App extends Component {
  async componentDidMount() {
    let globalAccessToken = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let customerAccessToken = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let loggedInUserDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsForLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );

    let cartDetailsForAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);

    // Case 1. THe user is not logged in.
    if (!globalAccessToken && !this.props.cartLoading) {
      await this.props.getGlobalAccessToken();
      globalAccessToken = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    }

    if (!customerAccessToken && localStorage.getItem(REFRESH_TOKEN)) {
      await this.props.refreshToken(localStorage.getItem(REFRESH_TOKEN));
      customerAccessToken = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    }

    if (customerAccessToken) {
      if (!cartDetailsForLoggedInUser && !this.props.cartLoading) {
        this.props.generateCartIdForLoggedInUser();
      }
    }
    if (
      customerAccessToken &&
      cartDetailsForLoggedInUser &&
      loggedInUserDetails
    ) {
      if (
        this.props.location.pathname.indexOf(LOGIN_PATH) !== -1 ||
        this.props.location.pathname.indexOf(SIGN_UP_PATH) !== -1
      ) {
        if (this.props.redirectToAfterAuthUrl) {
          this.props.history.push(this.props.redirectToAfterAuthUrl);
          this.props.clearUrlToRedirectToAfterAuth();
        } else {
          this.props.history.push(`${HOME_ROUTER}`);
        }
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
        <SecondaryLoader />
      </div>
    );
  }

  render() {
    let className = AppStyles.base;
    const {
      globalAccessTokenStatus,
      customerAccessTokenStatus,
      refreshCustomerAccessTokenStatus,
      cartIdForLoggedInUserStatus,
      cartIdForAnonymousUserStatus
    } = this.props;

    if (
      globalAccessTokenStatus === REQUESTING ||
      customerAccessTokenStatus === REQUESTING ||
      refreshCustomerAccessTokenStatus === REQUESTING ||
      cartIdForLoggedInUserStatus === REQUESTING ||
      cartIdForAnonymousUserStatus === REQUESTING
    ) {
      return <HomeSkeleton />;
    }

    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }

    return (
      <React.Fragment>
        <div className={className}>
          <SecondaryLoaderContainer />

          <StoryWidget />
          <ModalContainer />
          <ErrorContainer />
          <ToastContainer />
        </div>
      </React.Fragment>
    );
  }
}

export default App;
