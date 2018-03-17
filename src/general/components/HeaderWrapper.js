import React from "react";
import InformationHeader from "./InformationHeader.js";
import HollowHeader from "./HollowHeader.js";
import { withRouter } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import styles from "./HeaderWrapper.css";

import {
  HOME_ROUTER,
  PRODUCT_CART_ROUTER,
  DEFAULT_BRANDS_LANDING_PAGE,
  CATEGORIES_LANDING_PAGE,
  PRODUCT_CART_DELIVERY_MODES,
  LOGIN_PATH,
  SIGN_UP_PATH
} from "../../../src/lib/constants";
import { SIGN_UP } from "../../auth/actions/user.actions";

const PRODUCT_CODE_REGEX = /p-(.*)/;
class HeaderWrapper extends React.Component {
  onBackClick = () => {
    console.log("ON BACK CLICK");
    this.props.history.goBack();
  };
  render() {
    console.log("HEADER IS RENDERED");
    const url = this.props.location.pathname;

    let productCode = null;
    if (PRODUCT_CODE_REGEX.test(url)) {
      productCode = PRODUCT_CODE_REGEX.exec(url);
    }

    let canGoBack = true;
    let shouldRenderHeader = true;
    let headerText = "";

    if (
      url === HOME_ROUTER ||
      url === CATEGORIES_LANDING_PAGE ||
      url === DEFAULT_BRANDS_LANDING_PAGE
    ) {
      canGoBack = false;
    }

    if (this.props.history.length === 0) {
      canGoBack = false;
    }

    if (url === HOME_ROUTER) {
      headerText = "Home";
    }

    if (url === CATEGORIES_LANDING_PAGE) {
      headerText = "Categories";
    }

    if (url === DEFAULT_BRANDS_LANDING_PAGE) {
      headerText = "Brands";
    }

    if (url === LOGIN_PATH || url === SIGN_UP_PATH) {
      shouldRenderHeader = false;
    }

    // if we are on home, category landing, brand landing, we cannot go back.

    /*
    When can we not go back?
    If we are on home, category landing, brand landing, then we cannot go back.
    If we are not on any of those and we have history stack, then we can go back.

    What if we are on view sellers or view reviews?
      If there is no back and we are on a



*/

    return shouldRenderHeader ? (
      <div className={styles.base}>
        {" "}
        {productCode ? (
          <HollowHeader goBack={this.onBackClick} />
        ) : (
          <InformationHeader
            goBack={this.onBackClick}
            text={headerText}
            hasBackButton={canGoBack}
          />
        )}{" "}
      </div>
    ) : null;
  }
}

export default withRouter(HeaderWrapper);
