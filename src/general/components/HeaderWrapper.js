import React from "react";
import InformationHeader from "./InformationHeader.js";
import SearchContainer from "../../search/SearchContainer.js";
import HollowHeader from "./HollowHeader.js";
import { withRouter } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import styles from "./HeaderWrapper.css";
import queryString from "query-string";
import {
  HOME_ROUTER,
  PRODUCT_CART_ROUTER,
  DEFAULT_BRANDS_LANDING_PAGE,
  CATEGORIES_LANDING_PAGE,
  LOGIN_PATH,
  SIGN_UP_PATH,
  PRODUCT_LISTINGS,
  SAVE_LIST_PAGE,
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  MY_ACCOUNT_PAGE,
  JUS_PAY_CHARGED,
  JUS_PAY_PENDING,
  JUS_PAY_AUTHENTICATION_FAILED,
  CHECKOUT_ROUTER
} from "../../../src/lib/constants";
import { SIGN_UP } from "../../auth/actions/user.actions";

const PRODUCT_CODE_REGEX = /p-(.*)/;
class HeaderWrapper extends React.Component {
  onBackClick = () => {
    if (this.props.isPlpFilterOpen) {
      this.props.hideFilter();
      return;
    }

    const parsedQueryString = queryString.parse(this.props.location.search);
    const value = parsedQueryString.status;

    if (
      value === JUS_PAY_CHARGED ||
      value === JUS_PAY_PENDING ||
      value === JUS_PAY_AUTHENTICATION_FAILED
    ) {
      window.history.go(-3);
    } else {
      this.props.history.goBack();
    }
  };
  goToCart = () => {
    if (this.props.history) {
      this.props.history.push(PRODUCT_CART_ROUTER);
    }
  };

  goToWishList = () => {
    if (this.props.history) {
      const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
      const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
      if (!userDetails || !customerCookie) {
        this.props.history.push(LOGIN_PATH);
      } else {
        this.props.history.push(`${MY_ACCOUNT_PAGE}${SAVE_LIST_PAGE}`);
      }
    }
  };

  render() {
    const searchQuery = queryString.parse(this.props.history.location.search);
    const hasAppView = searchQuery.appview;
    const url = this.props.location.pathname;
    let shouldRenderSearch = false;

    let productCode = null;
    if (PRODUCT_CODE_REGEX.test(url)) {
      productCode = PRODUCT_CODE_REGEX.exec(url);
      shouldRenderSearch = true;
    }

    let isGoBack = true;
    let isCross = false;
    let shouldRenderHeader = true;
    if (url === PRODUCT_CART_ROUTER) {
      shouldRenderSearch = false;
    }
    if (this.props.match.path.includes("/") && url !== PRODUCT_CART_ROUTER) {
      isGoBack = true;
      shouldRenderSearch = true;
    }

    if (
      url === HOME_ROUTER ||
      url === CATEGORIES_LANDING_PAGE ||
      url === DEFAULT_BRANDS_LANDING_PAGE ||
      url === PRODUCT_LISTINGS
    ) {
      isGoBack = false;
      shouldRenderSearch = true;
    }

    if (this.props.history.length === 0) {
      isGoBack = false;
    }

    if (url === LOGIN_PATH || url === SIGN_UP_PATH) {
      shouldRenderHeader = false;
    }
    if (url === CHECKOUT_ROUTER) {
      isGoBack = false;
      isCross = true;
    }
    if (hasAppView === "true") {
      shouldRenderHeader = false;
    }
    let headerToRender = (
      <InformationHeader
        goBack={this.onBackClick}
        text={this.props.headerText}
        hasBackButton={isGoBack}
      />
    );
    if (productCode) {
      headerToRender = (
        <HollowHeader
          goBack={this.onBackClick}
          goToCart={this.goToCart}
          goToWishList={this.goToWishList}
        />
      );
    } else if (shouldRenderSearch) {
      headerToRender = (
        <SearchContainer
          text={this.props.headerText}
          canGoBack={this.onBackClick}
          hasBackButton={isGoBack}
          hasCrossButton={isCross}
        />
      );
    }
    return (
      shouldRenderHeader && (
        <React.Fragment>
          {!productCode && <div className={styles.hiddenHeader} />}
          <div className={!productCode ? styles.base : styles.absoluteHeader}>
            {headerToRender}
          </div>
        </React.Fragment>
      )
    );
  }
}

export default withRouter(HeaderWrapper);
