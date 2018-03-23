import React from "react";
import PdpFrame from "./PdpFrame";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import JewelleryDetailsAndLink from "./JewelleryDetailsAndLink";
import { Image } from "xelpmoc-core";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ColourSelector from "./ColourSelector";
import SizeSelector from "./SizeSelector";
import PriceBreakUp from "./PriceBreakUp";
import OfferCard from "./OfferCard";
import PdpLink from "./PdpLink";

import ProductDetails from "./ProductDetails";
import JewelleryClassification from "./JewelleryClassification";
import RatingAndTextLink from "./RatingAndTextLink";
import AllDescription from "./AllDescription";
import PdpPincode from "./PdpPincode";
import Overlay from "./Overlay";
import DeliveryInformation from "../../general/components/DeliveryInformations.js";
import JewelleryCertification from "./JewelleryCertification.js";
import { HashLink as Link } from "react-router-hash-link";
import styles from "./ProductDescriptionPage.css";
import * as Cookie from "../../lib/Cookie";
import PDPRecommendedSections from "./PDPRecommendedSections.js";
import {
  PRODUCT_SELLER_ROUTER_SUFFIX,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  ANONYMOUS_USER,
  PRODUCT_CART_ROUTER,
  PRODUCT_REVIEWS_PATH_SUFFIX,
  YES,
  NO,
  PRODUCT_DESCRIPTION_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
} from "../../lib/constants";

const DELIVERY_TEXT = "Delivery Options For";
const PIN_CODE = "110011";
const PRODUCT_QUANTITY = "1";
export default class PdpJewellery extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showPriceBreakUp: false
    };
  }
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
    this.props.history.push({
      pathname: PRODUCT_CART_ROUTER,
      state: {
        ProductCode: this.props.productDetails.productListingId,
        pinCode: PIN_CODE
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
      if (
        cartDetailsLoggedInUser !== undefined &&
        customerCookie !== undefined
      ) {
        this.props.addProductToCart(
          JSON.parse(userDetails).userName,
          JSON.parse(cartDetailsLoggedInUser).code,
          JSON.parse(customerCookie).access_token,
          productDetails
        );
      }
    } else if (cartDetailsAnonymous) {
      this.props.addProductToCart(
        ANONYMOUS_USER,
        JSON.parse(cartDetailsAnonymous).guid,
        JSON.parse(globalCookie).access_token,
        productDetails
      );
    }
  };

  goToReviewPage = () => {
    const url = `${this.props.location.pathname}${PRODUCT_REVIEWS_PATH_SUFFIX}`;
    this.props.history.push(url);
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
  showPriceBreakUp() {
    this.setState({ showPriceBreakUp: true });
  }
  showEmiModal = () => {
    const cartValue = this.props.productDetails.winningSellerMOP.substr(1);
    const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);

    const globalAccessToken = JSON.parse(globalCookie).access_token;
    this.props.getPdpEmi(globalAccessToken, cartValue);
    this.props.getEmiTerms(globalAccessToken, cartValue);
    this.props.showEmiModal();
  };
  renderDeliveryOptions(productData) {
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
            label={PIN_CODE}
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
    let otherSellersText;

    if (productData.otherSellers && productData.otherSellers.length > 0) {
      otherSellersText = (
        <span>
          Sold by{" "}
          <span className={styles.winningSellerText}>
            {" "}
            {productData.winningSellerName}
          </span>{" "}
          and {productData.otherSellers.length} other sellers;
        </span>
      );
    }

    if (productData) {
      return (
        <PdpFrame
          goToCart={() => this.goToCart()}
          gotoPreviousPage={() => this.gotoPreviousPage()}
          addProductToBag={() => this.addToCart()}
          addProductToWishList={() => this.addToWishList()}
          showPincodeModal={() => this.showPincodeModal()}
        >
          <ProductGalleryMobile paddingBottom="114">
            {mobileGalleryImages.map((val, idx) => {
              return (
                <Image image={val} key={idx} color="#ffffff" fit="contain" />
              );
            })}
          </ProductGalleryMobile>
          <div className={styles.content}>
            {/* <ProductDetailsMainCard
              productName={productData.brandName}
              productDescription={productData.productName}
              price={productData.mrp}
              discountPrice={productData.winningSellerMOP}
              averageRating={productData.averageRating}
            /> */}
            <JewelleryDetailsAndLink
              productName={productData.brandName}
              productDescription={productData.productName}
              price={productData.mrp}
              discountPrice={productData.winningSellerMOP}
              discount={productData.discount}
              hasPriceBreakUp={productData.priceBreakUpDetailsMap}
              history={this.props.history}
              showPriceBreakUp={() => {
                this.showPriceBreakUp();
              }}
            />
          </div>
          {productData.isEMIEligible === "Y" && (
            <div className={styles.separator}>
              <div className={styles.info}>
                Emi available on this product.
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
                headerText="Select a variant"
              />
            </React.Fragment>
          )}
          {productData.certificationMapFrJwlry && (
            <JewelleryCertification
              certifications={productData.certificationMapFrJwlry}
            />
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

          {productData.otherSellers && (
            <div className={styles.separator}>
              <PdpLink onClick={this.goToSellerPage}>
                <div className={styles.sellers}>{otherSellersText}</div>
              </PdpLink>
            </div>
          )}

          <div className={styles.separator}>
            <RatingAndTextLink
              onClick={this.goToReviewPage}
              averageRating={productData.averageRating}
              numberOfReview={productData.numberOfReviews}
            />
          </div>

          <div className={styles.details} id="priceBreakup">
            {productData.priceBreakUpDetailsMap && (
              <PriceBreakUp
                data={productData.priceBreakUpDetailsMap}
                isOpen={this.state.showPriceBreakUp}
              />
            )}
            {productData.fineJewelleryClassificationList && (
              <JewelleryClassification
                data={productData.fineJewelleryClassificationList}
              />
            )}
          </div>
          {productData.APlusContent && (
            <AllDescription
              productContent={productData.APlusContent.productContent}
            />
          )}
          <PDPRecommendedSections
            msdItems={this.props.msdItems}
            productData={productData}
          />
        </PdpFrame>
      );
    } else {
      return null;
    }
  }
}
