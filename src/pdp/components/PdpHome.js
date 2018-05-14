import React from "react";
import PdpFrame from "./PdpFrame";
import find from "lodash.find";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import Image from "../../xelpmoc-core/Image";
import ProductGalleryMobile from "./ProductGalleryMobile";
import UnderLinedButton from "../../general/components/UnderLinedButton";

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
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  COLLECT
} from "../../lib/constants";

import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import styles from "./ProductDescriptionPage.css";
import LoadableVisibility from "react-loadable-visibility/react-loadable";

const ProductFeatures = LoadableVisibility({
  loader: () => import("./ProductFeatures"),
  loading: () => <div />,
  delay: 400
});

const ProductFeature = LoadableVisibility({
  loader: () => import("./ProductFeature"),
  loading: () => <div />,
  delay: 400
});

const SizeQuantitySelect = LoadableVisibility({
  loader: () => import("./SizeQuantitySelect"),
  loading: () => <div />,
  delay: 400
});

const Overlay = LoadableVisibility({
  loader: () => import("./Overlay"),
  loading: () => <div />,
  delay: 400
});

const PdpPincode = LoadableVisibility({
  loader: () => import("./PdpPincode"),
  loading: () => <div />,
  delay: 1000
});

const AllDescription = LoadableVisibility({
  loader: () => import("./AllDescription"),
  loading: () => <div />,
  delay: 400
});

const RatingAndTextLink = LoadableVisibility({
  loader: () => import("./RatingAndTextLink"),
  loading: () => <div />,
  delay: 400
});

const PdpPaymentInfo = LoadableVisibility({
  loader: () => import("./PdpPaymentInfo"),
  loading: () => <div />,
  delay: 400
});

const OtherSellersLink = LoadableVisibility({
  loader: () => import("./OtherSellersLink"),
  loading: () => <div />,
  delay: 400
});

const OfferCard = LoadableVisibility({
  loader: () => import("./OfferCard"),
  loading: () => <div />,
  delay: 400
});

const ColourSelector = LoadableVisibility({
  loader: () => import("./ColourSelector"),
  loading: () => <div />,
  delay: 400
});

const PdpDeliveryModes = LoadableVisibility({
  loader: () => import("./PdpDeliveryModes"),
  loading: () => <div />,
  delay: 400
});

const PDPRecommendedSectionsContainer = LoadableVisibility({
  loader: () => import("../containers/PDPRecommendedSectionsContainer"),
  loading: () => {
    return <div />;
  },
  delay: 400
});

const VIDEO = "Video";
const IMAGE = "Image";
export default class PdpApparel extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      productQuantityOption: "Quantity",
      sizeError: false,
      quantityError: false
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
    this.setState({ productQuantityOption: quantity, quantityError: false });
  };
  updateSize = () => {
    this.setState({ sizeError: false });
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
  showEmiModal = () => {
    const cartValue = this.props.productDetails.winningSellerPrice.value;
    const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    const globalAccessToken = JSON.parse(globalCookie).access_token;
    this.props.getPdpEmi(globalAccessToken, cartValue);
    this.props.getEmiTerms(globalAccessToken, cartValue);
    this.props.showEmiModal();
  };
  addToCart = () => {
    let productDetails = {};
    productDetails.code = this.props.productDetails.productListingId;
    productDetails.quantity = this.state.productQuantityOption.value;
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
        if (!this.checkIfSizeSelected()) {
          this.props.displayToast("Please select a size to continue");
          this.setState({ sizeError: true });
        } else if (
          !this.checkIfQuantitySelected() ||
          this.state.productQuantityOption === "Quantity"
        ) {
          this.props.displayToast("Please select a quantity to continue");
          this.setState({ quantityError: true });
        } else {
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
  checkIfSizeSelected = () => {
    if (this.props.location.state && this.props.location.state.isSizeSelected) {
      return true;
    } else {
      return false;
    }
  };
  checkIfQuantitySelected = () => {
    if (
      this.props.location.state &&
      this.props.location.state.isQuantitySelected
    ) {
      return true;
    } else {
      return false;
    }
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
            (this.props.productDetails.winningSellerAvailableStock === "0" &&
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
          <div className={styles.whiteBackground}>
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
              showEmiModal={() => this.showEmiModal()}
            />
            <OfferCard
              showDetails={this.props.showOfferDetails}
              potentialPromotions={productData.potentialPromotions}
              secondaryPromotions={productData.productOfferMsg}
            />
            {productData.variantOptions && (
              <React.Fragment>
                <SizeQuantitySelect
                  history={this.props.history}
                  sizeError={this.state.sizeError}
                  quantityError={this.state.quantityError}
                  showSizeGuide={
                    productData.showSizeGuide ? this.props.showSizeGuide : null
                  }
                  data={productData.variantOptions}
                  maxQuantity={productData.maxQuantityAllowed}
                  updateQuantity={this.updateQuantity}
                  updateSize={this.updateSize}
                  checkIfSizeSelected={this.checkIfSizeSelected}
                  checkIfQuantitySelected={this.checkIfQuantitySelected}
                  productQuantity={this.state.productQuantityOption}
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
          {productData.classifications && (
            <div className={styles.details}>
              <ProductFeatures features={productData.classifications} />
            </div>
          )}
          <div className={styles.details}>
            {productData.productDescription && (
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
            <Accordion text="Overview" headerFontSize={16}>
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
              templateName={productData.APlusContent.temlateName}
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
