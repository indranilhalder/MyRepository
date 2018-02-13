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
  render() {
    if (this.props.productDetails) {
      return (
        <PdpFrame
          addProductToBag={this.props.addProductToBag}
          addProductToWishList={this.props.addProductToWishList}
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
