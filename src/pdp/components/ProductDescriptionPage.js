import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import ProductDetails from "./ProductDetails";
import { Image } from "xelpmoc-core";
import styles from "./ProductDescriptionPage.css";
import { PRODUCT_REVIEW_ROUTER, MOBILE_PDP_VIEW } from "../../lib/constants";
class ProductDescriptionPage extends Component {
  componentWillMount() {
    this.props.getProductDescription();
  }

  renderLoader() {
    return (
      <div className={styles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  }
  goToReviewPage = () => {
    this.props.history.push(PRODUCT_REVIEW_ROUTER);
  };
  render() {
    if (this.props.productDetails) {
      const productData = this.props.productDetails;
      const mobileGalleryImages = productData.galleryImagesList.filter(val => {
        return val.imageType === MOBILE_PDP_VIEW;
      })[0].galleryImages;

      return (
        <div className={styles.base}>
          <ProductGalleryMobile>
            {mobileGalleryImages.map(val => {
              return <Image image={val.value} />;
            })}
          </ProductGalleryMobile>
          <div className={styles.content}>
            <ProductDetailsMainCard
              productName={productData.productName}
              productDescription={productData.productDescription}
              price={productData.mrpPrice.formattedValue}
              discountPrice={productData.discountedPrice.formattedValue}
              averageRating={productData.averageRating}
            />
          </div>
          <div className={styles.details}>
            <ProductDetails data={productData.productDetails} />
          </div>
          <div onClick={this.goToReviewPage}>Go to Review</div>
        </div>
      );
    } else {
      return this.renderLoader();
    }
  }
}

export default ProductDescriptionPage;
