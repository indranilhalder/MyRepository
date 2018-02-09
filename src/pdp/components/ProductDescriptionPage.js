import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
import ProductGalleryMobile from "./ProductGalleryMobile";
import { Image } from "xelpmoc-core";
import styles from "./ProductDescriptionPage.css";
import { PRODUCT_REVIEW_ROUTER } from "../../lib/constants";
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
        return val.imageType === "mobilePdpView";
      })[0].galleryImages;

      return (
        <div className={styles.base}>
          <ProductGalleryMobile>
            {mobileGalleryImages.map(val => {
              return <Image image={val.value} />;
            })}
          </ProductGalleryMobile>
          <div onClick={this.goToReviewPage}>Go to Review</div>
        </div>
      );
    } else {
      return this.renderLoader();
    }
  }
}

export default ProductDescriptionPage;
