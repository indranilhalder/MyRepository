import React, { Component } from "react";
import ReviewList from "./ReviewList";
import styles from "./ProductReviewPage.css";
import ProductDetailsCard from "./ProductDetailsCard";
import WriteReview from "./WriteReview";
import PropTypes from "prop-types";
import RatingHolder from "./RatingHolder";
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
        <div className={styles.base}>
          <div className={styles.productBackground}>
            <ProductDetailsCard
              productImage={this.props.productDetails.galleryImagesList}
              productName={this.props.productDetails.productName}
              productMaterial={
                this.props.productDetails.classificationList[0].value
                  .classificationListJwlry[5].value
                  .classificationListValueJwlry[0]
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
