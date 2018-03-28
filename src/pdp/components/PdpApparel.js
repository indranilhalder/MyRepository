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
import PDPRecommendedSections from "./PDPRecommendedSections.js";

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
    if (this.checkIfSizeSelected()) {
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
    } else {
      this.showSizeSelector();
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
      this.props.showPincodeModal(this.props.match.params[1]);
    } else if (
      this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
    ) {
      this.props.showPincodeModal(this.props.match.params[2]);
    }
  }
  showSizeSelector = () => {
    if (this.props.showSizeSelector && this.props.productDetails) {
      this.props.showSizeSelector({
        sizeSelected: this.checkIfSizeSelected(),
        productId: this.props.productDetails.productListingId,
        showSizeGuide: this.props.showSizeGuide,
        data: this.props.productDetails.variantOptions.map(value => {
          return value.sizelink;
        })
      });
    }
  };
  handleShowSizeguide() {
    if (this.props.getProductSizeGuide) {
      this.props.getProductSizeGuide();
    }
  }
  checkIfSizeSelected = () => {
    if (this.props.location.state && this.props.location.state.isSizeSelected) {
      return true;
    } else {
      return false;
    }
  };
  renderDeliveryOptions(productData) {
    const defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

    return (
      productData.eligibleDeliveryModes &&
      productData.eligibleDeliveryModes.map((val, idx) => {
        return (
          <DeliveryInformation
            key={idx}
            header={val.name}
            placedTime={val.timeline}
            type={val.code}
            onClick={() => this.renderAddressModal()}
            deliveryOptions={DELIVERY_TEXT}
            label={defaultPinCode}
            showCliqAndPiqButton={false}
          />
        );
      })
    );
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
          addProductToWishList={() => this.addToWishList()}
        >
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
              <SizeSelector
                history={this.props.history}
                sizeSelected={this.checkIfSizeSelected()}
                productId={productData.productListingId}
                showSizeGuide={this.props.showSizeGuide}
                data={productData.variantOptions.map(value => {
                  return value.sizelink;
                })}
              />
              <ColourSelector
                data={productData.variantOptions.map(value => {
                  return value.colorlink;
                })}
                history={this.props.history}
                updateColour={val => {}}
                getProductSpecification={this.props.getProductSpecification}
              />
            </React.Fragment>
          )}
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
              {this.renderDeliveryOptions(productData)}
            </Overlay>
          ) : (
            this.renderDeliveryOptions(productData)
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
            <RatingAndTextLink
              onClick={this.goToReviewPage}
              averageRating={productData.averageRating}
              numberOfReview={productData.numberOfReviews}
            />
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
          <PDPRecommendedSections
            msdItems={this.props.msdItems}
            aboutTheBrand={this.props.aboutTheBrand}
            productData={productData}
          />
        </PdpFrame>
      );
    } else {
      return null;
    }
  }
}
