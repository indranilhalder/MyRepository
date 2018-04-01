import React from "react";
import PdpFrame from "./PdpFrame";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import { Image } from "xelpmoc-core";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ColourSelector from "./ColourSelector";
import SizeQuantitySelect from "./SizeQuantitySelect";
import OfferCard from "./OfferCard";
import PdpLink from "./PdpLink";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import ProductDetails from "./ProductDetails";
import ProductFeatures from "./ProductFeatures";
import RatingAndTextLink from "./RatingAndTextLink";
import PdpDeliveryModes from "./PdpDeliveryModes";
import AllDescription from "./AllDescription";
import PdpPincode from "./PdpPincode";
import Overlay from "./Overlay";
import DeliveryInformation from "../../general/components/DeliveryInformations.js";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER,
  PRODUCT_SELLER_ROUTER_SUFFIX,
  PRODUCT_CART_ROUTER,
  PRODUCT_REVIEWS_PATH_SUFFIX,
  PRODUCT_DESCRIPTION_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE,
  NO,
  DEFAULT_PIN_CODE_LOCAL_STORAGE
} from "../../lib/constants";

import styles from "./ProductDescriptionPage.css";
import PDPRecommendedSectionsContainer from "../containers/PDPRecommendedSectionsContainer.js";

const PRODUCT_QUANTITY = "1";
const DELIVERY_TEXT = "Delivery Options For";
export default class PdpApparel extends React.Component {
  visitBrand() {
    if (this.props.visitBrandStore) {
      this.props.visitBrandStore();
    }
  }
  gotoPreviousPage = () => {
    this.props.history.goBack();
  };
  goToBuyingGuide = () => {
    if (this.props.goToBuyingGuide) {
      this.props.goToBuyingGuide();
    }
  };
  goToSellerPage = () => {
    let expressionRuleFirst = "/p-(.*)/(.*)";
    let expressionRuleSecond = "/p-(.*)";
    let productId;
    if (this.props.location.pathname.match(expressionRuleFirst)) {
      productId = this.props.location.pathname.match(expressionRuleFirst)[1];
    } else {
      productId = this.props.location.pathname.match(expressionRuleSecond)[1];
    }
    this.props.history.push(`/p-${productId}${PRODUCT_SELLER_ROUTER_SUFFIX}`);
  };
  goToCart = () => {
    const defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

    this.props.history.push({
      pathname: PRODUCT_CART_ROUTER,
      state: {
        ProductCode: this.props.productDetails.productListingId,
        pinCode: defaultPinCode
      }
    });
  };

  addToCart = () => {
    let productDetails = {};
    productDetails.code = this.props.productDetails.productListingId;
    productDetails.quantity = PRODUCT_QUANTITY;
    productDetails.ussId = this.props.productDetails.winningUssID;
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);

    if (userDetails) {
      if (cartDetailsLoggedInUser && customerCookie) {
        this.props.addProductToCart(
          JSON.parse(userDetails).userName,
          JSON.parse(cartDetailsLoggedInUser).code,
          JSON.parse(customerCookie).access_token,
          productDetails
        );
      }
    } else {
      if (cartDetailsAnonymous && globalCookie) {
        this.props.addProductToCart(
          ANONYMOUS_USER,
          JSON.parse(cartDetailsAnonymous).guid,
          JSON.parse(globalCookie).access_token,
          productDetails
        );
      }
    }
  };

  goToReviewPage = () => {
    const url = `${this.props.location.pathname}${PRODUCT_REVIEWS_PATH_SUFFIX}`;
    this.props.history.push(url);
  };

  addToWishList = () => {
    let productDetails = {};
    productDetails.code = this.props.productDetails.productListingId;
    productDetails.ussId = this.props.productDetails.winningUssID;

    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

    if (userDetails) {
      this.props.addProductToWishList(
        JSON.parse(userDetails).userName,
        JSON.parse(customerCookie).access_token,
        productDetails
      );
    } else {
      this.props.addProductToWishList(
        ANONYMOUS_USER,
        JSON.parse(globalCookie).access_token,
        productDetails
      );
    }
  };
  showPincodeModal() {
    if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
      this.props.showPincodeModal(this.props.match.params[0]);
    } else if (
      this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
    ) {
      this.props.showPincodeModal(this.props.match.params[1]);
    }
  }

  handleQuantitySelect(val) {
    if (this.props.onQuantitySelect) {
      this.props.onQuantitySelect();
    }
  }

  render() {
    const productData = this.props.productDetails;
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
        <PdpFrame
          goToCart={() => this.goToCart()}
          gotoPreviousPage={() => this.gotoPreviousPage()}
          addProductToBag={() => this.addToCart()}
          productListingId={productData.productListingId}
          ussId={productData.winningUssID}
        >
          <ProductGalleryMobile>
            {mobileGalleryImages.map((val, idx) => {
              return <Image image={val} key={idx} />;
            })}
          </ProductGalleryMobile>
          <div className={styles.whiteBackground}>
            <div className={styles.content}>
              <ProductDetailsMainCard
                productName={productData.brandName}
                productDescription={productData.productName}
                price={productData.mrpPrice.formattedValueNoDecimal}
                discountPrice={
                  productData.winningSellerPrice.formattedValueNoDecimal
                }
                averageRating={productData.averageRating}
                onClick={this.goToReviewPage}
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

            {productData.potentialPromotions && (
              <OfferCard
                endTime={productData.potentialPromotions.endDate}
                startDate={productData.potentialPromotions.startDate}
                heading={productData.potentialPromotions.title}
                description={productData.potentialPromotions.description}
                onClick={this.goToCouponPage}
              />
            )}
            {productData.variantOptions && (
              <React.Fragment>
                <SizeQuantitySelect
                  history={this.props.history}
                  showSizeGuide={
                    productData.showSizeGuide ? this.props.showSizeGuide : null
                  }
                  sizes={productData.variantOptions.map(value => {
                    return value.sizelink;
                  })}
                  maxQuantity={productData.maxQuantityAllowed}
                  onQuantitySelect={val => this.props.handleQuantitySelect(val)}
                />
                {this.props.customiseMessage && (
                  <div className={styles.customisation}>
                    <div className={styles.customiseText}>
                      Customisation available -{this.props.customiseMessage}
                    </div>
                    <div className={styles.customisationButton}>
                      <UnderLinedButton
                        label="Checkout our buying guide"
                        onClick={() => this.goToBuyingGuide()}
                        color="#ff1744"
                      />
                    </div>
                  </div>
                )}
                <ColourSelector
                  noBackground={true}
                  data={productData.variantOptions.map(value => {
                    return value.colorlink;
                  })}
                  history={this.props.history}
                  updateColour={val => {}}
                  getProductSpecification={this.props.getProductSpecification}
                />
              </React.Fragment>
            )}
          </div>
          {this.props.productDetails.isServiceableToPincode &&
          this.props.productDetails.isServiceableToPincode.pinCode ? (
            <PdpPincode
              hasPincode={true}
              pincode={this.props.productDetails.isServiceableToPincode.pinCode}
              onClick={() => this.showPincodeModal()}
            />
          ) : (
            <PdpPincode onClick={() => this.showPincodeModal()} />
          )}
          {this.props.productDetails.isServiceableToPincode &&
          this.props.productDetails.isServiceableToPincode.status === NO ? (
            <Overlay labelText="Not serviceable in you pincode,
  please try another pincode">
              <PdpDeliveryModes
                eligibleDeliveryModes={productData.eligibleDeliveryModes}
                deliveryModesATP={productData.deliveryModesATP}
              />
            </Overlay>
          ) : (
            <PdpDeliveryModes
              eligibleDeliveryModes={productData.eligibleDeliveryModes}
              deliveryModesATP={productData.deliveryModesATP}
            />
          )}

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
          <div className={styles.separator}>
            {productData.averageRating && (
              <RatingAndTextLink
                onClick={this.goToReviewPage}
                averageRating={productData.averageRating}
                numberOfReview={productData.numberOfReviews}
              />
            )}
          </div>
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
          <PDPRecommendedSectionsContainer />
        </PdpFrame>
      );
    } else {
      return null;
    }
  }
}
