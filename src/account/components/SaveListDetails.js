import React from "react";
import SearchHeader from "../../search/components/SearchHeader";
import SaveListCard from "../../blp/components/SaveListCard";
import styles from "./SaveListDetails.css";
import PropTypes from "prop-types";
import moment from "moment";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER
} from "../../lib/constants";

import * as Cookie from "../../lib/Cookie";
const dateFormat = "MMMM DD YYYY";
const PRODUCT_QUANTITY = "1";

export default class SaveListDetails extends React.Component {
  componentDidMount() {
    this.props.getWishList();
  }
  addToBagItem(ussid, productcode) {
    let productDetails = {};
    productDetails.ussId = ussid;
    productDetails.code = productcode;
    productDetails.quantity = PRODUCT_QUANTITY;
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
    if (userDetails) {
      if (cartDetailsLoggedInUser && customerCookie) {
        this.props.addProductToCart(
          JSON.parse(userDetails).userName,
          JSON.parse(cartDetailsLoggedInUser).code,
          JSON.parse(customerCookie).access_token,
          productDetails
        );
      }
    } else {
      if (cartDetailsAnonymous && globalCookie) {
        this.props.addProductToCart(
          ANONYMOUS_USER,
          JSON.parse(cartDetailsAnonymous).guid,
          JSON.parse(globalCookie).access_token,
          productDetails
        );
      }
    }
  }
  removeItem(ussid) {
    let productDetails = {};
    productDetails.USSID = ussid;
    if (this.props.removeProductFromWishList) {
      this.props.removeProductFromWishList(productDetails);
    }
  }
  render() {
    const wishList = this.props.profile.wishlist;
    return (
      <div className={styles.base}>
        {wishList &&
          wishList.products.map((val, i) => {
            return (
              <div className={styles.listCardHolder} key={i}>
                <SaveListCard
                  productName={val.productBrand}
                  productMaterial={val.productName}
                  price={val.mrp.value}
                  date={moment(val.date).format(dateFormat)}
                  day=""
                  offer=""
                  offerPrice={val.mop.value}
                  image={val.imageURL}
                  addToBagItem={() =>
                    this.addToBagItem(val.USSID, val.productcode)
                  }
                  removeItem={val => this.removeItem(val.USSID)}
                />
              </div>
            );
          })}
      </div>
    );
  }
}
SaveListDetails.propTypes = {
  text: PropTypes.string,
  profile: PropTypes.objectOf(
    PropTypes.shape({
      wishlist: PropTypes.arrayOf(
        PropTypes.shape({
          products: PropTypes.arrayOf(
            PropTypes.shape({
              productBrand: PropTypes.string,
              productName: PropTypes.string,
              imageURL: PropTypes.string,
              date: PropTypes.string
            })
          )
        })
      )
    })
  ),
  addProductToWishList: PropTypes.func,
  removeProductFromWishList: PropTypes.func
};
SaveListDetails.defaultProps = {
  text: "Saved List"
};
