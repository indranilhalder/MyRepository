import React from "react";
import InformationHeader from "./InformationHeader";
import MobileFooter from "./MobileFooter";
import SearchContainer from "../../search/SearchContainer";
import {
  MAIN_ROUTER,
  CATEGORIES_LANDING_PAGE,
  BRAND_PAGE,
  BRAND_PAGE_WITH_SLUG,
  BRAND_LANDING_PAGE,
  PRODUCT_CART_ROUTER,
  DEFAULT_BRANDS_LANDING_PAGE,
  CATEGORY_PAGE,
  CATEGORY_PAGE_WITH_SLUG,
  PROFILE_PAGE,
  PRODUCT_SELLER_ROUTER_SUFFIX,
  PRODUCT_CART_DELIVERY_MODES,
  PRODUCT_DELIVERY_ADDRESSES,
  PRODUCT_LISTINGS
} from "../../lib/constants";
import styles from "./CommonFrame.css";
const HOME = "home";
const CATEGORIES = "categories";
const BRANDS = "brands";
const PROFILE = "profile";
const BAG = "bag";
export default class extends React.Component {
  getPageName = () => {
    const url = this.props.location.pathname;
    if (url === "/") {
      return HOME;
    } else if (
      url.includes(CATEGORIES) ||
      url.includes(CATEGORY_PAGE) ||
      url.includes(CATEGORY_PAGE_WITH_SLUG)
    ) {
      return CATEGORIES;
    } else if (url.includes(BRANDS)) {
      return BRANDS;
    } else if (url.includes(PROFILE)) {
      return PROFILE;
    } else if (url.includes(BAG)) {
      return BAG;
    } else {
      return null;
    }
  };
  getIfUrlIsBaseUrl = () => {
    const url = this.props.location.pathname;
    // how do I know if something is base?
    // it goes to /, DEFAULT_BRANDS_LANDING_PAGE, CATEGORIES_LANDING_PAGE, PRODUCT_CART_ROUTER, PROFILE_PAGE
    //then return true;
  };

  getShouldRenderSearch = () => {
    // we know to render search again based on the url
    // check the designs for when.
    // if in trouble ask me about regex - you should probably use regex here to do this - you need to make sure the url is correct.
    ///categories
    console.log(CATEGORIES_LANDING_PAGE);
    console.log(this.props.location.pathname);
    const pathname = this.props.location.pathname;
    if (
      pathname === CATEGORIES_LANDING_PAGE ||
      pathname === DEFAULT_BRANDS_LANDING_PAGE ||
      pathname === MAIN_ROUTER
    ) {
      return true;
    } else {
      return false;
    }
  };

  getShouldRender = () => {
    // again look at the design and decide when to render this.
  };
  getHeaderText = () => {};
  render() {
    console.log(this.props);
    this.getShouldRenderSearch();
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          {this.getShouldRenderSearch && <SearchContainer />}
          {!this.getShouldRenderSearch && <InformationHeader />}
        </div>
        <div className={styles.content}>{this.props.children}</div>

        <div className={styles.footer}>
          <MobileFooter history={this.props.history} />
        </div>
      </div>
    );
  }
}
