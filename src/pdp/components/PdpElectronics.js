import React from "react";
import PdpFrame from "./PdpFrame";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import Image from "../../xelpmoc-core/Image";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ColourSelector from "./ColourSelector";
import SizeSelector from "./SizeSelector";
import OfferCard from "./OfferCard";
import PdpLink from "./PdpLink";
import ProductDetails from "./ProductDetails";
import ProductFeatures from "./ProductFeatures";
import ProductFeature from "./ProductFeature";
import RatingAndTextLink from "./RatingAndTextLink";
import OtherSellersLink from "./OtherSellersLink";
import AllDescription from "./AllDescription";
import PdpPincode from "./PdpPincode";
import Overlay from "./Overlay";
import PdpDeliveryModes from "./PdpDeliveryModes";
import PdpPaymentInfo from "./PdpPaymentInfo";
import JewelleryDetailsAndLink from "./JewelleryDetailsAndLink";
import Accordion from "../../general/components/Accordion.js";
import Logo from "../../general/components/Logo.js";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import ProductModule from "../../general/components/ProductModule.js";
import Button from "../../general/components/Button.js";
import styles from "./ProductDescriptionPage.css";
import * as Cookie from "../../lib/Cookie";
import PDPRecommendedSectionsContainer from "../containers/PDPRecommendedSectionsContainer.js";

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
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE,
  DEFAULT_PIN_CODE_LOCAL_STORAGE
} from "../../lib/constants";

const DELIVERY_TEXT = "Delivery Options For";
const PRODUCT_QUANTITY = "1";
export default class PdpElectronics extends React.Component {
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
    if (!this.props.productDetails.winningSellerPrice) {
      this.props.displayToast("Product is not saleable");
    } else {
      if (
        this.props.productDetails.allOOStock ||
        this.props.productDetails.winningSellerAvailableStock === "0"
      ) {
        this.props.displayToast("Product is out of stock");
      } else {
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

  showEmiModal = () => {
    const cartValue = this.props.productDetails.winningSellerPrice.value;
    const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);

    const globalAccessToken = JSON.parse(globalCookie).access_token;
    this.props.getPdpEmi(globalAccessToken, cartValue);
    this.props.getEmiTerms(globalAccessToken, cartValue);
    this.props.showEmiModal();
  };

  render() {
    const productData = this.props.productDetails;
    const mobileGalleryImages = productData.galleryImagesList
      ? productData.galleryImagesList
          .map(galleryImageList => {
            return galleryImageList.galleryImages.filter(galleryImages => {
              return galleryImages.key === "product";
            });
          })
          .map(image => {
            return image[0] && image[0].value;
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
          ussId={productData.winningUssID}
          showPincodeModal={() => this.showPincodeModal()}
          outOfStock={
            productData.allOOStock ||
            !productData.winningSellerPrice ||
            productData.winningSellerAvailableStock === "0"
          }
        >
          <div className={styles.gallery}>
            <ProductGalleryMobile
              paddingBottom={
                productData.rootCategory === "Watches" ? "114" : "89.4"
              }
            >
              {mobileGalleryImages.map((val, idx) => {
                return (
                  <Image
                    image={val}
                    key={idx}
                    color={
                      productData.rootCategory === "Watches"
                        ? "#ffffff"
                        : "#f5f5f5"
                    }
                    fit="contain"
                  />
                );
              })}
            </ProductGalleryMobile>
            {(productData.allOOStock ||
              productData.winningSellerAvailableStock === "0") && (
              <div className={styles.flag}>Out of stock</div>
            )}
            {!productData.winningSellerPrice && (
              <div className={styles.flag}>Not Saleable</div>
            )}
          </div>
          <div className={styles.content}>
            {productData.rootCategory !== "Watches" && (
              <ProductDetailsMainCard
                productName={productData.brandName}
                productDescription={productData.productName}
                brandUrl={productData.brandURL}
                history={this.props.history}
                price={price}
                discountPrice={discountPrice}
                averageRating={productData.averageRating}
                onClick={this.goToReviewPage}
                discount={productData.discount}
              />
            )}
            {productData.rootCategory === "Watches" && (
              <JewelleryDetailsAndLink
                productName={productData.brandName}
                productDescription={productData.productName}
                brandUrl={productData.brandURL}
                history={this.props.history}
                price={discountPrice}
                discountPrice={price}
                averageRating={productData.averageRating}
                discount={productData.discount}
              />
            )}
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
              getAllStoresForCliqAndPiq={this.props.getAllStoresForCliqAndPiq}
              eligibleDeliveryModes={productData.eligibleDeliveryModes}
              deliveryModesATP={productData.deliveryModesATP}
            />
          )}
          <div className={styles.separator}>
            <OtherSellersLink
              onClick={this.goToSellerPage}
              otherSellers={productData.otherSellers}
              winningSeller={productData.winningSellerName}
            />
          </div>
          {productData.rootCategory !== "Watches" && (
            <div className={styles.details}>
              {productData.details && (
                <Accordion
                  text="Product Description"
                  headerFontSize={16}
                  isOpen={true}
                >
                  <div className={styles.accordionContent}>
                    {productData.productDescription}
                    <div style={{ marginTop: 10 }}>
                      {productData.details &&
                        productData.details.map(val => {
                          return <div className={styles.list}>{val.value}</div>;
                        })}
                    </div>
                  </div>
                </Accordion>
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
          )}
          {productData.rootCategory === "Watches" && (
            <div className={styles.details}>
              {productData.productDescription && (
                <Accordion
                  text="Product Description"
                  headerFontSize={16}
                  isOpen={true}
                >
                  <div className={styles.accordionContent}>
                    {productData.productDescription}
                  </div>
                </Accordion>
              )}
              {productData.classifications && (
                <Accordion
                  text="Features & Functions"
                  headerFontSize={16}
                  isOpen={false}
                >
                  {productData.classifications.map(val => {
                    if (val.specifications) {
                      return val.specifications.map(value => {
                        return (
                          <div style={{ paddingBottom: 10 }}>
                            <div className={styles.sideHeader}>{value.key}</div>
                            <div className={styles.sideContent}>
                              {value.value}
                            </div>
                          </div>
                        );
                      });
                    } else {
                      return null;
                    }
                  })}
                </Accordion>
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
          )}
          <div className={styles.separator}>
            <RatingAndTextLink
              onClick={this.goToReviewPage}
              averageRating={productData.averageRating}
              numberOfReview={productData.numberOfReviews}
            />
          </div>
          {productData.rootCategory !== "Watches" && (
            <div className={styles.details}>
              {productData.classifications && (
                <ProductFeatures features={productData.classifications} />
              )}
            </div>
          )}
          {productData.rootCategory === "Watches" && (
            <div className={styles.details}>
              {productData.details && (
                <ProductDetails data={productData.details} />
              )}
              {productData.warranty &&
                productData.warranty.length > 0 && (
                  <ProductFeature
                    heading="Warranty"
                    content={productData.warranty[0]}
                  />
                )}
            </div>
          )}
          {productData.APlusContent && (
            <AllDescription
              templateName={productData.APlusContent.temlateName}
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
