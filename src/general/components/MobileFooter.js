import React from "react";
import styles from "./MobileFooter.css";
import MobileFooterItem from "./MobileFooterItem";
import PropTypes from "prop-types";
import { withRouter } from "react-router-dom";
import {
  HOME_ROUTER,
  PRODUCT_CART_ROUTER,
  DEFAULT_BRANDS_LANDING_PAGE,
  CATEGORIES_LANDING_PAGE,
  PRODUCT_CART_DELIVERY_MODES,
  MY_ACCOUNT_PAGE,
  MY_ACCOUNT_ADDRESS_EDIT_PAGE,
  EDIT_ADDRESS_BOOK,
  CDN_URL_ROOT,
  CART_BAG_DETAILS
} from "../../../src/lib/constants";
import { CATEGORY_REGEX } from "../../plp/components/PlpBrandCategoryWrapper";
const HOME = "home";
const CATEGORIES = "categories";
const BRANDS = "brands";
const PROFILE = "profile";
const BAG = "bag";
const userIcon = `${CDN_URL_ROOT}myCliq_icon.png`;
const homeIcon = `${CDN_URL_ROOT}home_icon.png`;
const brandsIcon = `${CDN_URL_ROOT}brands_icon.png`;
const myBagIcon = `${CDN_URL_ROOT}myBag_icon.png`;
const categoriesIcon = `${CDN_URL_ROOT}categories_icon.png`;
const brandsIconRed = `${CDN_URL_ROOT}brands_icon_red.png`;
const userIconRed = `${CDN_URL_ROOT}myCliq_icon_red.png`;
const categoriesIconRed = `${CDN_URL_ROOT}categories_icon_red.png`;
const homeIconRed = `${CDN_URL_ROOT}home_icon_red.png`;
const myBagIconRed = `${CDN_URL_ROOT}myBag_icon_red.png`;
class MobileFooter extends React.Component {
  handleSelect(val) {
    if (this.props.history) {
      this.props.history.push(val);
    }
  }

  render() {
    const pathName = this.props.location.pathname;
    let selected = null;
    if (pathName === HOME_ROUTER) {
      selected = HOME;
    }
    if (pathName === CATEGORIES_LANDING_PAGE) {
      selected = CATEGORIES;
    }

    if (pathName === DEFAULT_BRANDS_LANDING_PAGE) {
      selected = BRANDS;
    }
    if (pathName === EDIT_ADDRESS_BOOK) {
      selected = PROFILE;
    }
    // if (pathName === PRODUCT_CART_ROUTER) {
    //   selected = BAG;
    // }

    if (pathName === MY_ACCOUNT_PAGE) {
      selected = PROFILE;
    }

    if (selected === null) {
      return null;
    }

    return (
      <div className={styles.base}>
        <MobileFooterItem
          activeIcon={homeIconRed}
          basicIcon={homeIcon}
          value={HOME}
          text="Home"
          selected={selected}
          onSelect={() => this.handleSelect(HOME_ROUTER)}
        />
        <MobileFooterItem
          activeIcon={categoriesIconRed}
          basicIcon={categoriesIcon}
          value={CATEGORIES}
          text="Categories"
          selected={selected}
          onSelect={() => this.handleSelect(CATEGORIES_LANDING_PAGE)}
        />
        <MobileFooterItem
          activeIcon={brandsIconRed}
          basicIcon={brandsIcon}
          value={BRANDS}
          text="Brands"
          selected={selected}
          onSelect={() => this.handleSelect(DEFAULT_BRANDS_LANDING_PAGE)}
        />
        <MobileFooterItem
          activeIcon={userIconRed}
          basicIcon={userIcon}
          value={PROFILE}
          text="My Account"
          selected={selected}
          onSelect={() => this.handleSelect(MY_ACCOUNT_PAGE)}
        />
        <MobileFooterItem
          activeIcon={myBagIconRed}
          basicIcon={myBagIcon}
          bagCount={
            localStorage.getItem(CART_BAG_DETAILS) &&
            JSON.parse(localStorage.getItem(CART_BAG_DETAILS)) &&
            JSON.parse(localStorage.getItem(CART_BAG_DETAILS)).length
          }
          value={BAG}
          text="My Bag"
          selected={selected}
          onSelect={val => this.handleSelect(PRODUCT_CART_ROUTER)}
        />
      </div>
    );
  }
}

export default withRouter(MobileFooter);

MobileFooter.propTypes = {
  selected: PropTypes.oneOf([HOME, CATEGORIES, BRANDS, PROFILE, BAG])
};
MobileFooter.defaultProps = {
  selected: HOME
};
