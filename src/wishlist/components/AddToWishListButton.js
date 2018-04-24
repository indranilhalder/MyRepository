import React from "react";
import PropTypes from "prop-types";
import Icon from "../../xelpmoc-core/Icon";
import downloadIcon from "../../general/components/img/download.png";
import * as Cookie from "../../lib/Cookie";
import FooterButton from "../../general/components/FooterButton.js";
import saveIcon from "../../general/components/img/download.png";
import styles from "./AddToWishListButton.css";
import downloadIconWhite from "../../general/components/img/downloadWhite.png";
import {
  LOGIN_PATH,
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN
} from "../../lib/constants.js";
export const WISHLIST_FOOTER_BUTTON_TYPE = "wishlistFooter";
export const WISHLIST_FOOTER_ICON_TYPE = "wishlistIcon";
export const WISHLIST_BUTTON_TEXT_TYPE = "wishlistText";
export default class AddToWishListButton extends React.Component {
  onClick(e) {
    e.stopPropagation();
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!userDetails || !customerCookie) {
      const url = this.props.location.pathname;
      this.props.setUrlToRedirectToAfterAuth(url);
      this.props.history.push(LOGIN_PATH);
    } else {
      const { productListingId, winningUssID, wishlistItems } = this.props;
      const indexOfProduct = wishlistItems.findIndex(item => {
        return (
          item.productcode === productListingId || item.USSID === winningUssID
        );
      });
      if (indexOfProduct < 0) {
        this.props.addProductToWishList({
          productListingId,
          winningUssID
        }); // adding product to wishlist
      } else {
        this.props.displayToast(); // product is a already in wish list show toast
      }
    }
  }
  render() {
    if (this.props.type === WISHLIST_FOOTER_BUTTON_TYPE) {
      return (
        <FooterButton
          borderColor="#ececec"
          icon={saveIcon}
          label="Save"
          onClick={e => this.onClick(e)}
        />
      );
    }

    if (this.props.type === WISHLIST_BUTTON_TEXT_TYPE) {
      return (
        <div className={styles.saveButton} onClick={e => this.onClick(e)}>
          <div className={styles.iconHolder}>
            <Icon image={saveIcon} size={24} />
          </div>
          <div className={styles.saveLabel}>Save</div>
        </div>
      );
    }

    return (
      <div onClick={e => this.onClick(e)}>
        <Icon
          image={this.props.isWhite ? downloadIconWhite : downloadIcon}
          size={this.props.size}
        />
      </div>
    );
  }
}
AddToWishListButton.propTypes = {
  productListingId: PropTypes.string.isRequired,
  winningUssID: PropTypes.string.isRequired,
  wishlistItems: PropTypes.arrayOf(
    PropTypes.shape({
      productListingId: PropTypes.string,
      winningUssID: PropTypes.string
    })
  ),
  addProductToWishList: PropTypes.func,
  size: PropTypes.number
};
AddToWishListButton.defaultProps = {
  size: 20,
  addProductToWishList: () => {},
  type: WISHLIST_FOOTER_ICON_TYPE
};
