import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import ProductDetails from "./ProductDetails";
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


  renderAddressModal = () => {
    if (this.props.showAddress) {
      this.props.showAddress(this.props.productDetails);
    }
  }

  goToCouponPage = () => {
    this.props.showCouponModal(this.props.productDetails);
  };

  render() {
    if (this.props.productDetails) {
      const productData = this.props.productDetails;
      const mobileGalleryImages = productData.galleryImagesList.filter(val => {
        return val.imageType === MOBILE_PDP_VIEW;
      })[0].galleryImages;
      return (
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
              productName={productData.productName}
              productDescription={productData.productDescription}
              price={productData.mrpPrice.formattedValue}
              discountPrice={productData.discountedPrice.formattedValue}
              averageRating={productData.averageRating}
            />
          </div>
          {productData.variantOptions &&
            productData.variantOptions.showColor && (
              <div>
                <SizeSelector
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
      );
    } else {
      return this.renderLoader();
    }
  }
}

export default ProductDescriptionPage;
