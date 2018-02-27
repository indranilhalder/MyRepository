import React from "react";
import styles from "./MobileFooter.css";
import MobileFooterItem from "./MobileFooterItem";
import withMultiSelect from "../../higherOrderComponents/withMultiSelect";
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
const MobileFooterBase = class MobileFooterBase extends React.Component {
  render() {
    return <div className={styles.base}>{this.props.children}</div>;
  }
};
const MobileFooterWithSelect = withMultiSelect(MobileFooterBase);

export default class MobileFooter extends React.Component {
  render() {
    return (
      <MobileFooterWithSelect limit={1} selected={["home"]}>
        <MobileFooterItem
          activeIcon={homeIconRed}
          basicIcon={homeIcon}
          value="home"
          text="Home"
        />
        <MobileFooterItem
          activeIcon={categoriesIconRed}
          basicIcon={categoriesIcon}
          value="categories"
          text="Categories"
        />
        <MobileFooterItem
          activeIcon={brandsIconRed}
          basicIcon={brandsIcon}
          value="brands"
          text="Brands"
        />
        <MobileFooterItem
          activeIcon={userIconRed}
          basicIcon={userIcon}
          value="profile"
          text="My Cliq"
        />
        <MobileFooterItem
          activeIcon={myBagIconRed}
          basicIcon={myBagIcon}
          value="bag"
          text="My Bag"
        />
      </MobileFooterWithSelect>
    );
  }
}
