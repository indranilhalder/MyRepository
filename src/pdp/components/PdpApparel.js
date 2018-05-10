import React from "react";
import PdpFrame from "./PdpFrame";
import find from "lodash.find";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import Image from "../../xelpmoc-core/Image";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ColourSelector from "./ColourSelector";
import SizeSelector from "./SizeSelector";
import OfferCard from "./OfferCard";
import OtherSellersLink from "./OtherSellersLink";
import PdpPaymentInfo from "./PdpPaymentInfo";
import ProductDetails from "./ProductDetails";
import ProductFeature from "./ProductFeature";
import RatingAndTextLink from "./RatingAndTextLink";
import AllDescription from "./AllDescription";
import PdpPincode from "./PdpPincode";
import PdpDeliveryModes from "./PdpDeliveryModes";
import Overlay from "./Overlay";
import Accordion from "../../general/components/Accordion.js";
import PDPRecommendedSectionsContainer from "../containers/PDPRecommendedSectionsContainer.js";
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
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  COLLECT
} from "../../lib/constants";

import styles from "./ProductDescriptionPage.css";

const PRODUCT_QUANTITY = "1";
const IMAGE = "Image";
export default class PdpApparel extends React.Component {
  visitBrand() {
    if (this.props.visitBrandStore) {
      this.props.visitBrandStore();
    }
  }
  gotoPreviousPage = () => {
    this.props.history.goBack();
  };
  goToSellerPage(count) {
    if (count !== 0) {
      let expressionRuleFirst = "/p-(.*)/(.*)";
      let expressionRuleSecond = "/p-(.*)";
      let productId;
      if (this.props.location.pathname.match(expressionRuleFirst)) {
        productId = this.props.location.pathname.match(expressionRuleFirst)[1];
      } else {
        productId = this.props.location.pathname.match(expressionRuleSecond)[1];
      }
      this.props.history.push(`/p-${productId}${PRODUCT_SELLER_ROUTER_SUFFIX}`);
    }
  }
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
    if (!this.props.productDetails.winningSellerPrice) {
      this.props.displayToast("Product is not saleable");
    } else {
      if (
        this.props.productDetails.allOOStock ||
        (this.props.productDetails.winningSellerAvailableStock === "0" &&
          this.checkIfSizeSelected())
      ) {
        this.props.displayToast("Product is out of stock");
      } else {
        if (this.checkIfSizeSelected() || this.checkIfSizeDoesNotExist()) {
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
      }
    }
  };

  goToReviewPage = () => {
    const url = `${
      this.props.location.pathname
    }/${PRODUCT_REVIEWS_PATH_SUFFIX}`;
    this.props.history.push(url);
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
  showSizeSelector = () => {
    if (
      this.props.showSizeSelector &&
      this.props.productDetails &&
      this.props.productDetails.variantOptions
    ) {
      this.props.showSizeSelector({
        sizeSelected: this.checkIfSizeSelected(),
        productId: this.props.productDetails.productListingId,
        showSizeGuide: this.props.showSizeGuide,
        hasSizeGuide: this.props.productDetails.showSizeGuide,
        data: this.props.productDetails.variantOptions
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
  checkIfSizeDoesNotExist = () => {
    return this.props.productDetails.variantOptions
      ? this.props.productDetails.variantOptions.filter(val => {
          return val.sizelink.isAvailable;
        }).length === 0
        ? true
        : false
      : true;
  };
  handleShowPiqPage = () => {
    const eligibleForCNC = find(
      this.props.productDetails &&
        this.props.productDetails.eligibleDeliveryModes,
      deliveryMode => {
        return deliveryMode.code === COLLECT;
      }
    );
    if (eligibleForCNC && this.props.getAllStoresForCliqAndPiq) {
      this.props.showPdpPiqPage();
      this.props.getAllStoresForCliqAndPiq();
    }
  };
  render() {
    const productData = this.props.productDetails;
    const mobileGalleryImages = productData.galleryImagesList
      ? productData.galleryImagesList
          .filter(val => {
            return val.mediaType === IMAGE;
          })
          .map(galleryImageList => {
            return galleryImageList.galleryImages.filter(galleryImages => {
              return galleryImages.key === "product";
            });
          })
          .map(image => {
            return image[0].value;
          })
      : [];

    if (productData) {
      let price = "";
      let discountPrice = "";
      if (productData.mrpPrice) {
        price = productData.mrpPrice.formattedValueNoDecimal;
      }
      if (productData.winningSellerPrice) {
        discountPrice = productData.winningSellerPrice.formattedValueNoDecimal;
      }

      return (
        <PdpFrame
          goToCart={() => this.goToCart()}
          gotoPreviousPage={() => this.gotoPreviousPage()}
          addProductToBag={() => this.addToCart()}
          productListingId={productData.productListingId}
          outOfStock={
            productData.allOOStock ||
            !productData.winningSellerPrice ||
            (productData.winningSellerAvailableStock === "0" &&
              this.checkIfSizeSelected())
          }
          ussId={productData.winningUssID}
        >
          <div className={styles.gallery}>
            <ProductGalleryMobile>
              {mobileGalleryImages.map((val, idx) => {
                return <Image image={val} key={idx} />;
              })}
            </ProductGalleryMobile>
            {(productData.allOOStock ||
              (productData.winningSellerAvailableStock === "0" &&
                this.checkIfSizeSelected())) && (
              <div className={styles.flag}>Out of stock</div>
            )}
            {!productData.winningSellerPrice && (
              <div className={styles.flag}>Not Saleable</div>
            )}
          </div>
          <div className={styles.content}>
            <ProductDetailsMainCard
              brandName={productData.brandName}
              productName={productData.productName}
              brandUrl={productData.brandURL}
              history={this.props.history}
              price={price}
              discountPrice={discountPrice}
              averageRating={productData.averageRating}
              onClick={this.goToReviewPage}
              discount={productData.discount}
            />
          </div>
          <PdpPaymentInfo
            hasEmi={productData.isEMIEligible}
            hasCod={productData.isCOD}
            showEmiModal={this.showEmiModal}
          />
          <OfferCard
            showDetails={this.props.showOfferDetails}
            potentialPromotions={productData.potentialPromotions}
            secondaryPromotions={productData.productOfferMsg}
          />
          {productData.variantOptions && (
            <React.Fragment>
              <SizeSelector
                history={this.props.history}
                sizeSelected={this.checkIfSizeSelected()}
                productId={productData.productListingId}
                hasSizeGuide={productData.showSizeGuide}
                showSizeGuide={this.props.showSizeGuide}
                data={productData.variantOptions}
              />
              <ColourSelector
                data={productData.variantOptions}
                productId={productData.productListingId}
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
            <Overlay
              labelText="Not serviceable in you pincode,
  please try another pincode"
            >
              <PdpDeliveryModes
                eligibleDeliveryModes={productData.eligibleDeliveryModes}
                deliveryModesATP={productData.deliveryModesATP}
              />
            </Overlay>
          ) : (
            <PdpDeliveryModes
              onPiq={this.handleShowPiqPage}
              eligibleDeliveryModes={productData.eligibleDeliveryModes}
              deliveryModesATP={productData.deliveryModesATP}
            />
          )}
          <div className={styles.separator}>
            <OtherSellersLink
              otherSellers={productData.otherSellers}
              winningSeller={productData.winningSellerName}
            />
          </div>
          <div className={styles.details}>
            {productData.details && (
              <Accordion
                text="Product Description"
                headerFontSize={16}
                isOpen={true}
              >
                <div className={styles.accordionContent} itemProp="description">
                  {productData.productDescription}
                </div>
              </Accordion>
            )}
            {productData.details && (
              <ProductDetails data={productData.details} />
            )}
            {productData.knowMore && (
              <Accordion text="Know More" headerFontSize={16}>
                {productData.knowMore &&
                  productData.knowMore.map(val => {
                    return (
                      <div className={styles.list}>{val.knowMoreItem}</div>
                    );
                  })}
              </Accordion>
            )}
            {productData.brandInfo && (
              <ProductFeature
                heading="Brand Info"
                content={productData.brandInfo}
              />
            )}
          </div>
          <div className={styles.separator}>
            <RatingAndTextLink
              onClick={this.goToReviewPage}
              averageRating={productData.averageRating}
              numberOfReview={productData.numberOfReviews}
            />
          </div>
          {productData.APlusContent && (
            <AllDescription
              productContent={productData.APlusContent.productContent}
            />
          )}
          <div className={styles.details} />
          <div className={styles.blankSeparator} />
          <PDPRecommendedSectionsContainer />
        </PdpFrame>
      );
    } else {
      return null;
    }
  }
}
