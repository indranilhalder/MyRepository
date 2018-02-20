import React, { Component } from "react";
import styles from "./ProductSellerPage.css";
import ProductDetailsCard from "./ProductDetailsCard";
import SellerWithMultiSelect from "./SellerWithMultiSelect";
import SellerCard from "./SellerCard";
import PdpFrame from "./PdpFrame";
import HollowHeader from "./HollowHeader.js";
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
  ANONYMOUS_USER
} from "../../lib/constants";

class ProductSellerPage extends Component {
  addProductToBag = () => {
    let productDetails = {};
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let cartDetailsForAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    if (userDetails) {
      productDetails.userId = JSON.parse(userDetails).customerInfo.mobileNumber;
      productDetails.accessToken = JSON.parse(customerCookie).access_token;
      productDetails.cartId = JSON.parse(cartDetailsLoggedInUser).code;
    } else {
      productDetails.userId = ANONYMOUS_USER;
      productDetails.accessToken = JSON.parse(globalCookie).access_token;
      productDetails.cartId = JSON.parse(cartDetailsForAnonymous).guid;
    }
    this.props.addProductToCart(productDetails);
  };
  addProductToWishList = () => {
    if (this.props.addProductToWishList) {
      let productDetails = {};
      productDetails.listingId = this.props.productDetails.productListingId;
      this.props.addProductToWishList(productDetails);
    }
  };

  render() {
    return (
      <PdpFrame
        addProductToBag={() => this.addProductToBag()}
        addProductToWishList={() => this.addProductToWishList()}
      >
        <div className={styles.base}>
          <HollowHeader
            addProductToBag={this.props.addProductToBag}
            addProductToWishList={this.props.addProductToWishList}
            history={this.props.history}
          />
          <ProductDetailsCard
            productImage={
              this.props.productDetails.galleryImagesList.filter(val => {
                return val.imageType === MOBILE_PDP_VIEW;
              })[0].galleryImages[0].value
            }
            productName={this.props.productDetails.productName}
            productMaterial={
              this.props.productDetails.classificationList[0].value
                .classificationListValue[5].value.classificationListValue[0]
            }
            price={this.props.productDetails.mrpPrice.formattedValue}
            discountPrice={
              this.props.productDetails.discountedPrice.formattedValue
            }
            averageRating={this.props.productDetails.averageRating}
            totalNoOfReviews={this.props.productDetails.productReviewsCount}
          />
          <div>
            {this.props.productDetails.otherSellers && (
              <SellerWithMultiSelect limit={1}>
                {this.props.productDetails.otherSellers.map((value, index) => {
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
    );
  }
}

export default ProductSellerPage;
