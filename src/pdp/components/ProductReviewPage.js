import React, { Component } from "react";
import ReviewList from "./ReviewList";
import styles from "./ProductReviewPage.css";
import ProductDetailsCard from "./ProductDetailsCard";
import WriteReview from "./WriteReview";
import PropTypes from "prop-types";
import RatingHolder from "./RatingHolder";
import PdpFrame from "./PdpFrame";
import HollowHeader from "./HollowHeader";
import { MOBILE_PDP_VIEW } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  ANONYMOUS_USER
} from "../../lib/constants";
const WRITE_REVIEW_TEXT = "Write Review";

class ProductDescriptionPage extends Component {
  state = {
    visible: false
  };

  reviewSection = () => {
    if (this.state.visible === false) {
      this.setState({ visible: true });
    } else {
      this.setState({ visible: false });
    }
  };
  renderReviewSection = () => {
    if (this.state.visible) {
      return <WriteReview />;
    }
  };
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
    if (this.props.productDetails) {
      return (
        <PdpFrame
          addProductToBag={() => this.addProductToBag()}
          addProductToWishList={() => this.addProductToWishList()}
        >
          <div className={styles.base}>
            <div className={styles.productBackground}>
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

              <RatingHolder ratingData={this.props.ratingData} />
            </div>
            <div className={styles.reviewText} onClick={this.reviewSection}>
              {WRITE_REVIEW_TEXT}
            </div>
            {this.renderReviewSection()}
            <ReviewList reviewList={this.props.reviewList} />
          </div>
        </PdpFrame>
      );
    } else {
      return <div />;
    }
  }
}

ProductDescriptionPage.propTypes = {
  label: PropTypes.string,
  ratingData: PropTypes.array,
  reviewList: PropTypes.array
};

export default ProductDescriptionPage;
