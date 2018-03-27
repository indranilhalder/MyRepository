import React, { Component } from "react";
import styles from "./ProductSellerPage.css";
import ProductDetailsCard from "./ProductDetailsCard";
import SellerWithMultiSelect from "./SellerWithMultiSelect";
import SellerCard from "./SellerCard";
import PdpFrame from "./PdpFrame";
import * as Cookie from "../../lib/Cookie";

import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN
} from "../../lib/constants";
import {
  MOBILE_PDP_VIEW,
  PRICE_TEXT,
  OFFER_AVAILABLE,
  DELIVERY_INFORMATION_TEXT,
  DELIVERY_RATES,
  CASH_TEXT,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  ANONYMOUS_USER,
  PRODUCT_SELLER_ROUTER_SUFFIX,
  PRODUCT_OTHER_SELLER_ROUTER,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
} from "../../lib/constants";
const PRODUCT_QUANTITY = "1";
class ProductSellerPage extends Component {
  gotoPreviousPage = () => {
    const url = this.props.location.pathname.replace(
      PRODUCT_SELLER_ROUTER_SUFFIX,
      ""
    );
    this.props.history.replace(url);
  };

  addToCart = () => {
    let productDetails = {};
    productDetails.code = this.props.productListingId;
    productDetails.ussId = productDetails.quantity = PRODUCT_QUANTITY;
    productDetails.ussId = this.props.productDetails.winningUssID;
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
    if (userDetails) {
      this.props.addProductToCart(
        JSON.parse(userDetails).userName,
        JSON.parse(cartDetailsLoggedInUser).code,
        JSON.parse(customerCookie).access_token,
        productDetails
      );
    } else {
      this.props.addProductToCart(
        ANONYMOUS_USER,
        JSON.parse(cartDetailsAnonymous).guid,
        JSON.parse(globalCookie).access_token,
        productDetails
      );
    }
  };

  addToWishList = () => {
    let productDetails = {};
    productDetails.code = this.props.productDetails.productListingId;
    productDetails.ussId = this.props.productDetails.winningUssID;

    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

    if (userDetails) {
      this.props.addProductToWishList(
        JSON.parse(userDetails).userName,
        JSON.parse(customerCookie).access_token,
        productDetails
      );
    } else {
      this.props.addProductToWishList(
        ANONYMOUS_USER,
        JSON.parse(globalCookie).access_token,
        productDetails
      );
    }
  };

  componentDidMount() {
    if (this.props.match.path === PRODUCT_OTHER_SELLER_ROUTER) {
      this.props.getProductDescription(this.props.match.params[1]);
    } else {
      //need to show error page
    }
  }

  render() {
    const mobileGalleryImages =
      this.props.productDetails &&
      this.props.productDetails.galleryImagesList
        .map(galleryImageList => {
          return galleryImageList.galleryImages.filter(galleryImages => {
            return galleryImages.key === "product";
          });
        })
        .map(image => {
          return image[0].value;
        });

    return (
      mobileGalleryImages && (
        <PdpFrame
          addProductToBag={() => this.addToCart()}
          addProductToWishList={() => this.addToWishList()}
          gotoPreviousPage={() => this.gotoPreviousPage()}
        >
          <div className={styles.base}>
            <ProductDetailsCard
              productImage={mobileGalleryImages[0]}
              productName={this.props.productDetails.productName}
              price={this.props.productDetails.mrp}
              discountPrice={this.props.productDetails.winningSellerMOP}
              averageRating={this.props.productDetails.averageRating}
              totalNoOfReviews={this.props.productDetails.productReviewsCount}
            />
            <div>
              {this.props.productDetails.otherSellers && (
                <SellerWithMultiSelect limit={1}>
                  {this.props.productDetails.otherSellers
                    .filter(val => {
                      return val.availableStock !== "0";
                    })
                    .map((value, index) => {
                      return (
                        <SellerCard
                          heading={value.sellerName}
                          priceTitle={PRICE_TEXT}
                          discountPrice={value.sellerMOP}
                          price={value.sellerMRP}
                          offerText={OFFER_AVAILABLE}
                          deliveryText={DELIVERY_INFORMATION_TEXT}
                          shippingText={value.deliveryModesATP[0].value}
                          cashText={CASH_TEXT}
                          policyText={DELIVERY_RATES}
                          key={index}
                          value={value}
                        />
                      );
                    })}
                </SellerWithMultiSelect>
              )}
            </div>
          </div>
        </PdpFrame>
      )
    );
  }
}

export default ProductSellerPage;
