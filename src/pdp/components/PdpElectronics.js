import React from "react";
import PdpFrame from "./PdpFrame";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import { Image } from "xelpmoc-core";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ColourSelector from "./ColourSelector";
import SizeSelector from "./SizeSelector";
import OfferCard from "./OfferCard";
import PdpLink from "./PdpLink";
import ProductDetails from "./ProductDetails";
import ProductFeatures from "./ProductFeatures";
import RatingAndTextLink from "./RatingAndTextLink";
import AllDescription from "./AllDescription";
import DeliveryInformation from "../../general/components/DeliveryInformations.js";
import Logo from "../../general/components/Logo.js";
import Button from "../../general/components/Button.js";
import styles from "./ProductDescriptionPage.css";

const DELIVERY_TEXT = "Delivery Options For";
const PIN_CODE = "110011";
export default class PdpElectronics extends React.Component {
  visitBrand() {
    if (this.props.visitBrandStore) {
      this.props.visitBrandStore();
    }
  }
  render() {
    console.log(this.props);
    const productData = this.props;
    const mobileGalleryImages = productData.galleryImagesList
      .map(galleryImageList => {
        return galleryImageList.galleryImages.filter(galleryImages => {
          return galleryImages.key === "product";
        });
      })
      .map(image => {
        return image[0].value;
      });
    if (productData) {
      return (
        <PdpFrame>
          <ProductGalleryMobile>
            {mobileGalleryImages.map((val, idx) => {
              return <Image image={val} key={idx} />;
            })}
          </ProductGalleryMobile>
          <div className={styles.content}>
            <ProductDetailsMainCard
              productName={productData.brandName}
              productDescription={productData.productName}
              price={productData.mrp}
              discountPrice={productData.winningSellerMOP}
              averageRating={productData.averageRating}
            />
          </div>
          {productData.emiInfo && (
            <div className={styles.separator}>
              <div className={styles.info}>
                {productData.emiInfo.emiText}
                <span className={styles.link} onClick={this.showEmiModal}>
                  View Plans
                </span>
              </div>
            </div>
          )}
          {productData.productOfferPromotion && (
            <OfferCard
              endTime={productData.productOfferPromotion[0].validTill.date}
              heading={productData.productOfferPromotion[0].promotionTitle}
              description={productData.productOfferPromotion[0].promotionDetail}
              onClick={this.goToCouponPage}
            />
          )}
          {productData.variantOptions && (
            <React.Fragment>
              <ColourSelector
                data={productData.variantOptions.map(value => {
                  return value.colorlink;
                })}
                history={this.props.history}
                updateColour={val => {}}
                getProductSpecification={this.props.getProductSpecification}
              />
              <SizeSelector
                showSizeGuide={this.props.showSizeGuide}
                data={productData.variantOptions.map(value => {
                  return value.sizelink;
                })}
              />
            </React.Fragment>
          )}
          {productData.eligibleDeliveryModes &&
            productData.eligibleDeliveryModes.map((val, idx) => {
              return (
                <DeliveryInformation
                  key={idx}
                  header={val.name}
                  placedTime={val.timeline}
                  type={val.code}
                  onClick={() => this.renderAddressModal()}
                  deliveryOptions={DELIVERY_TEXT}
                  label={PIN_CODE}
                />
              );
            })}
          {productData.otherSellersText && (
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
          )}
          {productData.details && (
            <div className={styles.details}>
              <ProductDetails data={productData.details} />
            </div>
          )}
          {productData.numberOfReviews > 0 && (
            <div className={styles.separator}>
              <RatingAndTextLink
                onClick={this.goToReviewPage}
                averageRating={productData.averageRating}
                numberOfReview={productData.numberOfReviews}
              />
            </div>
          )}
          {productData.classifications && (
            <div className={styles.details}>
              <ProductFeatures features={productData.classifications} />
            </div>
          )}
          {productData.APlusContent && (
            <AllDescription
              productContent={productData.APlusContent.productContent}
            />
          )}
          <div className={styles.brandSection}>
            <div className={styles.brandHeader}>About the brand</div>
            <div className={styles.brandLogoSection}>
              {productData.brandLogoImage && (
                <div className={styles.brandLogoHolder}>
                  <Logo image={productData.brandLogoImage} />
                </div>
              )}
              <div className={styles.followButton}>
                <Button label="Follow" type="tertiary" />
              </div>
            </div>
            {productData.brandInfo && (
              <div className={styles.brandDescription}>
                {productData.brandInfo}
              </div>
            )}
            {/* Suggested  products part goes here  */}
            <div className={styles.visitBrandButton}>
              <Button
                type="secondary"
                label="Visit Brand Store"
                oncLick={() => this.visitBrand()}
              />
            </div>
          </div>
        </PdpFrame>
      );
    } else {
      return null;
    }
  }
}
