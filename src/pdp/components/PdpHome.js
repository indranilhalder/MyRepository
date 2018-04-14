import React from "react";
import PdpFrame from "./PdpFrame";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import { Image } from "xelpmoc-core";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ColourSelector from "./ColourSelector";
import SizeQuantitySelect from "./SizeQuantitySelect";
import OfferCard from "./OfferCard";
import PdpLink from "./PdpLink";
import ProductFeature from "./ProductFeature";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import ProductDetails from "./ProductDetails";
import ProductFeatures from "./ProductFeatures";
import RatingAndTextLink from "./RatingAndTextLink";
import PdpDeliveryModes from "./PdpDeliveryModes";
import AllDescription from "./AllDescription";
import PdpPincode from "./PdpPincode";
import Overlay from "./Overlay";
import Accordion from "../../general/components/Accordion.js";
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
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import styles from "./ProductDescriptionPage.css";
import PDPRecommendedSectionsContainer from "../containers/PDPRecommendedSectionsContainer.js";

export default class PdpApparel extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      productQuantity: "1"
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
  goToBuyingGuide = buyingGuideUrl => {
    if (buyingGuideUrl) {
      const urlSuffix = buyingGuideUrl.replace(TATA_CLIQ_ROOT, "$1");
      this.props.history.push(urlSuffix);
    }
  };
  updateQuantity = quantity => {
    this.setState({ productQuantity: quantity });
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
    productDetails.quantity = this.state.productQuantity;
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
    let validSellersCount = 0;
    if (
      productData.otherSellers &&
      productData.otherSellers.filter(val => {
        return val.availableStock !== "0";
      }).length > 0
    ) {
      validSellersCount = productData.otherSellers.filter(val => {
        return val.availableStock !== "0" && val.availableStock !== "-1";
      }).length;
    }
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
                brandUrl={productData.brandURL}
                history={this.props.history}
                price={price}
                discountPrice={discountPrice}
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
            <OfferCard
              showDetails={this.props.showOfferDetails}
              potentialPromotions={productData.potentialPromotions}
              secondaryPromotions={productData.productOfferMsg}
            />
            {productData.variantOptions && (
              <React.Fragment>
                <SizeQuantitySelect
                  history={this.props.history}
                  showSizeGuide={
                    productData.showSizeGuide ? this.props.showSizeGuide : null
                  }
                  data={productData.variantOptions}
                  maxQuantity={productData.maxQuantityAllowed}
                  updateQuantity={this.updateQuantity}
                  onQuantitySelect={val => this.props.handleQuantitySelect(val)}
                />

                <div className={styles.customisation}>
                  <div className={styles.customiseText}>
                    Customisation available - Contact seller for Free
                    Monogramming
                  </div>
                  {productData.buyingGuideUrl && (
                    <div className={styles.customisationButton}>
                      <UnderLinedButton
                        label="Checkout our buying guide"
                        onClick={() =>
                          this.goToBuyingGuide(productData.buyingGuideUrl)
                        }
                        color="#ff1744"
                      />
                    </div>
                  )}
                </div>

                <ColourSelector
                  noBackground={true}
                  productId={productData.productListingId}
                  data={productData.variantOptions}
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
          {productData.winningSellerName && (
            <div className={styles.separator}>
              <PdpLink
                onClick={() => this.goToSellerPage(validSellersCount)}
                noLink={validSellersCount === 0}
              >
                <div className={styles.sellers}>
                  Sold by{" "}
                  <span className={styles.winningSellerText}>
                    {productData.winningSellerName}
                  </span>
                  {validSellersCount !== 0 && (
                    <React.Fragment>
                      {" "}
                      and {validSellersCount} other seller(s)
                    </React.Fragment>
                  )}
                </div>
              </PdpLink>
            </div>
          )}

          {productData.classifications && (
            <div className={styles.details}>
              <ProductFeatures features={productData.classifications} />
            </div>
          )}
          <div className={styles.details}>
            <Accordion text="Product description" headerFontSize={16}>
              {productData.classificationList &&
                productData.classificationList.map(value => {
                  return (
                    <div>
                      <div className={styles.header}>{value.key}</div>
                      {value.value.classificationList &&
                        value.value.classificationList.map(val => {
                          return (
                            <div>
                              <div className={styles.contentTextForHome}>
                                {val.key} : {val.value}
                              </div>
                            </div>
                          );
                        })}
                      {value.value.classificationValues &&
                        value.value.classificationValues.map(val => {
                          return (
                            <div>
                              <div className={styles.contentTextForHome}>
                                {val}
                              </div>
                            </div>
                          );
                        })}
                      <div className={styles.blankSeparator} />
                    </div>
                  );
                })}
            </Accordion>
            {productData.styleNote && (
              <ProductFeature
                heading="Style Note"
                content={productData.styleNote}
              />
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
          <div className={styles.blankSeparator} />
          <PDPRecommendedSectionsContainer />
        </PdpFrame>
      );
    } else {
      return null;
    }
  }
}
