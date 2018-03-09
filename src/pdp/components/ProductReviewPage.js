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
import find from "lodash/find";
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

class ProductReviewPage extends Component {
  state = {
    visible: false
  };

  componentDidMount() {
    if (!this.props.productDetails) {
      this.props.getProductDescription(this.props.match.params[0]);
    }
    this.props.getProductReviews(this.props.match.params[0]);
  }

  reviewSection = () => {
    if (this.state.visible === false) {
      this.setState({ visible: true });
    } else {
      this.setState({ visible: false });
    }
  };

  onSubmit = productReview => {
    this.props.addProductReview(
      this.props.productDetails.productListingId,
      productReview
    );
  };
  renderReviewSection = () => {
    if (this.state.visible) {
      return <WriteReview onSubmit={this.onSubmit} />;
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
      console.log("IN PRODUCT REVIEW PAGE");
      console.log(this.props.productDetails);
      console.log(this.props.reviews);
      console.log(this.props.ratingData);
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
                  find(
                    this.props.productDetails.galleryImagesList[0]
                      .galleryImages,
                    galleryImage => {
                      return galleryImage.key === MOBILE_PDP_VIEW;
                    }
                  ).value
                }
                productName={this.props.productDetails.productName}
                productMaterial={this.props.productDetails.productDescription}
                price={this.props.productDetails.mrp}
                discountPrice={this.props.productDetails.winningSellerMOP}
                averageRating={this.props.productDetails.averageRating}
                totalNoOfReviews={this.props.productDetails.numberOfReviews}
              />

              <RatingHolder ratingData={this.props.ratingData} />
            </div>
            <div className={styles.reviewText} onClick={this.reviewSection}>
              {WRITE_REVIEW_TEXT}
            </div>
            {this.renderReviewSection()}
            {this.props.reviews && (
              <ReviewList reviewList={this.props.reviews.reviews} />
            )}
          </div>
        </PdpFrame>
      );
    } else {
      return <div />;
    }
  }
}

ProductReviewPage.propTypes = {
  label: PropTypes.string,
  ratingData: PropTypes.array,
  reviewList: PropTypes.array
};

export default ProductReviewPage;
