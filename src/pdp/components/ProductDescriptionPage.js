import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import ProductDetails from "./ProductDetails";
import PdpFrame from "./PdpFrame";
import OfferCard from "./OfferCard";
import ColourSelector from "./ColourSelector";
import SizeSelector from "./SizeSelector";
import { Image } from "xelpmoc-core";
import RatingAndTextLink from "./RatingAndTextLink";
import HollowHeader from "./HollowHeader.js";
import PdpLink from "./PdpLink";
import styles from "./ProductDescriptionPage.css";
import DeliveryInformation from "../../general/components/DeliveryInformations.js";

import {
  PRODUCT_REVIEW_ROUTER,
  MOBILE_PDP_VIEW,
  PRODUCT_SELLER_ROUTER
} from "../../lib/constants";
const DELIVERY_TEXT = "Delivery Options For";
const PIN_CODE = "110011";
class ProductDescriptionPage extends Component {
  componentWillMount() {
    this.props.getProductDescription();
    this.props.getProductSizeGuide();
    this.props.getPdpEmi();
  }
  onSave() {
    if (this.props.onSave) {
      this.props.onSave();
    }
  }
  onAddToBag() {
    if (this.props.onAddToBag) {
      this.props.onAddToBag();
    }
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
  goToSellerPage = () => {
    this.props.history.push(PRODUCT_SELLER_ROUTER);
  };
  showEmiModal = () => {
    if (this.props.showEmiPlans) {
      this.props.showEmiPlans();
    }
  };

  renderAddressModal = () => {
    if (this.props.showAddress) {
      this.props.showAddress();
    }
  };

  goToCouponPage = () => {
    this.props.showCouponModal(this.props.productDetails);
  };

  addProductToBag = () => {
    if (this.props.addProductToBag) {
      let productDetails = {};
      productDetails.listingId = this.props.productDetails.productListingId;
      this.props.addProductToBag(productDetails);
    }
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
      const productData = this.props.productDetails;
      const mobileGalleryImages = productData.galleryImagesList.filter(val => {
        return val.imageType === MOBILE_PDP_VIEW;
      })[0].galleryImages;
      return (
        <PdpFrame
          addProductToBag={() => this.addProductToBag()}
          addProductToWishList={() => this.addProductToWishList()}
        >
          <div className={styles.base}>
            <div className={styles.pageHeader}>
              <HollowHeader
                addProductToBag={this.props.addProductToBag}
                addProductToWishList={this.props.addProductToWishList}
                history={this.props.history}
              />
            </div>
            <ProductGalleryMobile>
              {mobileGalleryImages.map(val => {
                return <Image image={val.value} />;
              })}
            </ProductGalleryMobile>
            <div className={styles.content}>
              <ProductDetailsMainCard
                productName={productData.productBrandInfo.brandName}
                productDescription={productData.productName}
                price={productData.mrpPrice.formattedValue}
                discountPrice={productData.discountedPrice.formattedValue}
                averageRating={productData.averageRating}
              />
            </div>
            {productData.emiInfo && (
              <div className={styles.info}>
                {productData.emiInfo.emiText}
                <span className={styles.link} onClick={this.showEmiModal}>
                  View Plans
                </span>
              </div>
            )}

            {productData.variantOptions &&
              productData.variantOptions.showColor && (
                <div>
                  <SizeSelector
                    showSizeGuide={this.props.showSizeGuide}
                    data={productData.variantOptions.colorlink
                      .filter(option => {
                        return option.selected;
                      })
                      .map(value => {
                        return value.sizelink;
                      })}
                  />
                  <ColourSelector
                    data={productData.variantOptions.colorlink}
                    selected={productData.variantOptions.colorlink
                      .filter(option => {
                        return option.selected;
                      })
                      .map(value => {
                        return value.color;
                      })}
                    updateColour={val => {}}
                  />
                </div>
              )}
            <OfferCard
              endTime={productData.productOfferPromotion[0].validTill.date}
              heading={productData.productOfferPromotion[0].promotionTitle}
              description={productData.productOfferPromotion[0].promotionDetail}
              onClick={this.goToCouponPage}
            />
            <DeliveryInformation
              header={productData.eligibleDeliveryModes[0].name}
              placedTime={productData.eligibleDeliveryModes[0].timeline}
              onClick={() => this.renderAddressModal()}
              deliveryOptions={DELIVERY_TEXT}
              label={PIN_CODE}
            />
            <div className={styles.separator}>
              <RatingAndTextLink
                onClick={this.goToReviewPage}
                averageRating={productData.averageRating}
                numberOfReview={productData.productReviewsCount}
              />
            </div>
            <div className={styles.details}>
              <ProductDetails data={productData.productDetails} />
            </div>
            <div className={styles.separator}>
              <PdpLink onClick={this.goToSellerPage}>
                <div
                  className={styles.sellers}
                  dangerouslySetInnerHTML={{
                    __html: productData.otherSellersText
                  }}
                />
              </PdpLink>
            </div>
          </div>
        </PdpFrame>
      );
    } else {
      return this.renderLoader();
    }
  }
}

export default ProductDescriptionPage;
