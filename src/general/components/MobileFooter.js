import React from "react";
import styles from "./MobileFooter.css";
import MobileFooterItem from "./MobileFooterItem";
import PropTypes from "prop-types";
import homeIcon from "./img/home_icon.png";
import categoriesIcon from "./img/categories_icon.png";
import brandsIcon from "./img/brands_icon.png";
import userIcon from "./img/myCliq_icon.png";
import myBagIcon from "./img/myBag_icon.png";
import homeIconRed from "./img/home_icon_red.png";
import categoriesIconRed from "./img/categories_icon_red.png";
import brandsIconRed from "./img/brands_icon_red.png";
import userIconRed from "./img/myCliq_icon_red.png";
import myBagIconRed from "./img/myBag_icon_red.png";
import {
  HOME_ROUTER,
  PRODUCT_CART_ROUTER,
  BRANDS_LANDING_PAGE
} from "../../../src/lib/constants";

export default class MobileFooter extends React.Component {
  handleSelect(val) {
    if (this.props.history) {
      this.props.history.push(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <MobileFooterItem
          activeIcon={homeIconRed}
          basicIcon={homeIcon}
          value="home"
          text="Home"
          selected={this.props.selected}
          onSelect={() => this.handleSelect(HOME_ROUTER)}
        />
        <MobileFooterItem
          activeIcon={categoriesIconRed}
          basicIcon={categoriesIcon}
          value="categories"
          text="Categories"
          selected={this.props.selected}
          onSelect={() => this.handleSelect(HOME_ROUTER)}
        />
        <MobileFooterItem
          activeIcon={brandsIconRed}
          basicIcon={brandsIcon}
          value="brands"
          text="Brands"
          selected={this.props.selected}
          onSelect={() => this.handleSelect(BRANDS_LANDING_PAGE)}
        />
        <MobileFooterItem
          activeIcon={userIconRed}
          basicIcon={userIcon}
          value="profile"
          text="My Cliq"
          selected={this.props.selected}
          onSelect={() => this.handleSelect(HOME_ROUTER)}
        />
        <MobileFooterItem
          activeIcon={myBagIconRed}
          basicIcon={myBagIcon}
          value="bag"
          text="My Bag"
          selected={this.props.selected}
          onSelect={val => this.handleSelect(PRODUCT_CART_ROUTER)}
        />
      </div>
    );
  }
}

MobileFooter.propTypes = {
  selected: PropTypes.oneOf(["home", "categories", "brands", "profile", "bag"])
};
MobileFooter.defaultProps = {
  selected: "home"
};
