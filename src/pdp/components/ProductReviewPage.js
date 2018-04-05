import React, { Component } from "react";
import ReviewList from "./ReviewList";
import styles from "./ProductReviewPage.css";
import ProductDetailsCard from "./ProductDetailsCard";
import WriteReview from "./WriteReview";
import PropTypes from "prop-types";
import RatingHolder from "./RatingHolder";
import PdpFrame from "./PdpFrame";
import { Redirect } from "react-router-dom";
import {
  MOBILE_PDP_VIEW,
  PRODUCT_REVIEWS_PATH_SUFFIX,
  SUCCESS,
  LOGIN_PATH
} from "../../lib/constants";
import find from "lodash/find";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  ANONYMOUS_USER,
  REVIEW_SUBMIT_TOAST_TEXT
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
    if (!productReview.rating) {
      this.props.displayToast("Please give rating");
      return false;
    }
    if (!productReview.headline) {
      this.props.displayToast("Please enter title");
      return false;
    }
    if (!productReview.comment) {
      this.props.displayToast("Please enter comment");
      return false;
    } else {
      this.props.addProductReview(
        this.props.productDetails.productListingId,
        productReview
      );
      this.setState({ visible: false });
    }
  };
  onCancel() {
    this.setState({ visible: false });
  }
  renderReviewSection = () => {
    if (this.state.visible) {
      return (
        <WriteReview
          onSubmit={val => this.onSubmit(val)}
          onCancel={() => this.onCancel()}
        />
      );
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

  goBack = () => {
    const url = this.props.location.pathname.replace(
      PRODUCT_REVIEWS_PATH_SUFFIX,
      ""
    );
    this.props.history.replace(url);
  };

  componentWillReceiveProps(nextProps) {
    if (nextProps.addReviewStatus === SUCCESS) {
      this.setState({ visible: false });
    }
  }
  navigateToLogin() {
    return <Redirect to={LOGIN_PATH} />;
  }
  render() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!userDetails || !customerCookie) {
      return this.navigateToLogin();
    }
    if (this.props.productDetails) {
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
        <PdpFrame
          addProductToBag={() => this.addProductToBag()}
          addProductToWishList={() => this.addProductToWishList()}
          gotoPreviousPage={() => this.goBack()}
        >
          <div className={styles.base}>
            <div className={styles.productBackground}>
              <ProductDetailsCard
                productImage={mobileGalleryImages[0]}
                productName={this.props.productDetails.brandName}
                productMaterial={this.props.productDetails.productName}
                price={
                  this.props.productDetails.winningSellerPrice
                    .formattedValueNoDecimal
                }
                discountPrice={
                  this.props.productDetails.mrpPrice.formattedValueNoDecimal
                }
                averageRating={this.props.productDetails.averageRating}
                totalNoOfReviews={this.props.productDetails.productReviewsCount}
              />
              <RatingHolder ratingData={this.props.ratingData} />
            </div>
            <div className={styles.reviewHolder}>
              <div className={styles.reviewText} onClick={this.reviewSection}>
                {WRITE_REVIEW_TEXT}
              </div>
              {this.renderReviewSection()}
            </div>
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
