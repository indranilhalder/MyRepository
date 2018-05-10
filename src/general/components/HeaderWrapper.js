import React from "react";
import InformationHeader from "./InformationHeader.js";
import SearchContainer from "../../search/SearchContainer.js";
import HollowHeader from "./HollowHeader.js";
import StickyHeader from "./StickyHeader.js";
import { withRouter } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import styles from "./HeaderWrapper.css";
import queryString from "query-string";
import throttle from "lodash/throttle";
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
  CHECKOUT_ROUTER,
  CHECKOUT_ROUTER_THANKYOU,
  APP_VIEW
} from "../../../src/lib/constants";
import { SIGN_UP } from "../../auth/actions/user.actions";

const PRODUCT_CODE_REGEX = /p-(.*)/;
class HeaderWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      stickyHeader: false
    };
  }
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
  redirectToHome = () => {
    if (this.props.history) {
      this.props.history.push(HOME_ROUTER);
    }
  };
  handleScroll = () => {
    return throttle(() => {
      if (window.pageYOffset < 30 && this.state.stickyHeader) {
        this.setState({ stickyHeader: false });
      } else if (window.pageYOffset > 30 && !this.state.stickyHeader) {
        this.setState({ stickyHeader: true });
      }
    }, 50);
  };

  componentDidMount() {
    window.scroll(0, 0);
    this.throttledScroll = this.handleScroll();
    window.addEventListener("scroll", this.throttledScroll);
  }

  componentWillUnmount() {
    window.removeEventListener("scroll", this.throttledScroll);
  }

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
    if (hasAppView && !Cookie.getCookie(APP_VIEW)) {
      Cookie.createCookie(APP_VIEW, true);
    }
    const url = this.props.location.pathname;
    let shouldRenderSearch = false;

    let productCode = null;
    if (PRODUCT_CODE_REGEX.test(url)) {
      productCode = PRODUCT_CODE_REGEX.exec(url);
      shouldRenderSearch = true;
    }

    let isGoBack = true;
    let isCross = false;
    let isLogo = false;
    let shouldRenderHeader = true;
    let companyLogoInPdp = false;
    if (url === PRODUCT_CART_ROUTER) {
      shouldRenderSearch = false;
    }
    if (
      url === DEFAULT_BRANDS_LANDING_PAGE &&
      url === CATEGORIES_LANDING_PAGE &&
      url === MY_ACCOUNT_PAGE
    ) {
      isLogo = false;
    }

    if (
      this.props.match.path.includes("/") &&
      url !== PRODUCT_CART_ROUTER &&
      url !== DEFAULT_BRANDS_LANDING_PAGE &&
      url !== CATEGORIES_LANDING_PAGE &&
      url !== MY_ACCOUNT_PAGE
    ) {
      isGoBack = true;
      shouldRenderSearch = true;
      isLogo = true;
    }
    if (this.props.location.pathname.includes("/my-account/")) {
      isLogo = false;
      shouldRenderSearch = false;
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
    if (url === HOME_ROUTER) {
      isLogo = true;
    }
    if (this.props.history.length === 0) {
      isGoBack = false;
    }
    if (this.props.history.length <= 2) {
      companyLogoInPdp = true;
    }
    if (url === LOGIN_PATH || url === SIGN_UP_PATH) {
      shouldRenderHeader = false;
    }
    if (this.props.location.pathname.includes(CHECKOUT_ROUTER_THANKYOU)) {
      isGoBack = false;
      isCross = true;
      shouldRenderSearch = false;
    }
    if (url === CHECKOUT_ROUTER) {
      isGoBack = false;
      isCross = true;
      shouldRenderSearch = false;
    }

    if (hasAppView === "true" || Cookie.getCookie(APP_VIEW)) {
      shouldRenderHeader = false;
    }
    let headerToRender = (
      <InformationHeader
        goBack={this.onBackClick}
        text={this.props.headerText}
        hasBackButton={isGoBack}
        hasCrossButton={isCross}
      />
    );
    if (productCode) {
      headerToRender = this.state.stickyHeader ? (
        <StickyHeader
          goBack={this.onBackClick}
          redirectToHome={this.redirectToHome}
          goToCart={this.goToCart}
          goToWishList={this.goToWishList}
          text={this.props.headerText}
          isShowCompanyLogo={companyLogoInPdp}
          bagCount={this.props.bagCount}
        />
      ) : (
        <HollowHeader
          goBack={this.onBackClick}
          redirectToHome={this.redirectToHome}
          goToCart={this.goToCart}
          goToWishList={this.goToWishList}
          isShowCompanyLogo={companyLogoInPdp}
          bagCount={this.props.bagCount}
        />
      );
    } else if (shouldRenderSearch) {
      headerToRender = (
        <SearchContainer
          text={this.props.headerText}
          canGoBack={this.onBackClick}
          hasBackButton={isGoBack}
          isLogo={isLogo}
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
