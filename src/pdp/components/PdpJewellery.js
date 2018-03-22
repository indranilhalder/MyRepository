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
import Logo from "../../general/components/Logo.js";
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

const data = {
  type: "productDetailMobileWsData",
  allOOStock: false,
  brandInfo: "Fine Jewellery Brand",
  brandName: "Tanishq",
  certificationMapFrJwlry: ["AGL"],
  deliveryModesATP: [
    {
      key: "home-delivery",
      value: "Delivered in 5-8 business days."
    },
    {
      key: "express-delivery",
      value: "Delivered in 1-2 business days."
    }
  ],
  discount: "21",
  eligibleDeliveryModes: [
    {
      code: "home-delivery",
      displayCost: "₹10.00",
      name: "Home Delivery"
    },
    {
      code: "express-delivery",
      displayCost: "₹20.00",
      name: "Express Delivery"
    }
  ],
  fineJewelleryClassificationList: [
    {
      key: "Product Details",
      value: {
        classificationListJwlry: [
          {
            key: "PRODUCT CODE",
            value: {
              classificationListValueJwlry: ["abc579798A"]
            }
          },
          {
            key: "Set Type",
            value: {
              classificationListValueJwlry: ["Earring , Pendant & Ring "]
            }
          },
          {
            key: "Set Items Count",
            value: {
              classificationListValueJwlry: ["3 "]
            }
          },
          {
            key: "Bangle Type",
            value: {
              classificationListValueJwlry: ["Round "]
            }
          },
          {
            key: "Chain & Necklace Design Type",
            value: {
              classificationListValueJwlry: ["Mangalsutras "]
            }
          },
          {
            key: "Pendant Type",
            value: {
              classificationListValueJwlry: ["Religious "]
            }
          },
          {
            key: "Chain & Necklace Length Type",
            value: {
              classificationListValueJwlry: ["Choker "]
            }
          },
          {
            key: "Earring Type",
            value: {
              classificationListValueJwlry: ["Stud "]
            }
          },
          {
            key: "Type",
            value: {
              classificationListValueJwlry: ["Hathpanja "]
            }
          },
          {
            key: "Purity",
            value: {
              classificationListValueJwlry: ["800 Silver ", "10k (417) "]
            }
          },
          {
            key: "Metal",
            value: {
              classificationListValueJwlry: ["Silver "]
            }
          },
          {
            key: "Metal Color",
            value: {
              classificationListValueJwlry: ["White "]
            }
          },
          {
            key: "Design Type",
            value: {
              classificationListValueJwlry: ["Filigree "]
            }
          },
          {
            key: "Occasion",
            value: {
              classificationListValueJwlry: ["Wedding "]
            }
          },
          {
            key: "Certification",
            value: {
              classificationListValueJwlry: ["AGL "]
            }
          },
          {
            key: "Gross Weight",
            value: {
              classificationListValueJwlry: ["121 "]
            }
          }
        ]
      }
    },
    {
      key: "Solitaire Details",
      value: {
        classificationListJwlry: [
          {
            key: "Shape",
            value: {
              classificationListValueJwlry: ["Round "]
            }
          },
          {
            key: "Color",
            value: {
              classificationListValueJwlry: ["GH "]
            }
          },
          {
            key: "Count",
            value: {
              classificationListValueJwlry: ["1 "]
            }
          },
          {
            key: "Clarity",
            value: {
              classificationListValueJwlry: ["SI "]
            }
          },
          {
            key: "Setting",
            value: {
              classificationListValueJwlry: ["Prong "]
            }
          },
          {
            key: "Weight",
            value: {
              classificationListValueJwlry: ["6 "]
            }
          },
          {
            key: "Cut",
            value: {
              classificationListValueJwlry: ["EX "]
            }
          }
        ]
      }
    },
    {
      key: "Diamond Details",
      value: {
        classificationListJwlry: [
          {
            key: "Total Count",
            value: {
              classificationListValueJwlry: ["1 "]
            }
          },
          {
            key: "Clarity",
            value: {
              classificationListValueJwlry: ["SI "]
            }
          },
          {
            key: "Weight",
            value: {
              classificationListValueJwlry: ["6 "]
            }
          },
          {
            key: "Cut",
            value: {
              classificationListValueJwlry: ["EX "]
            }
          },
          {
            key: "Total Weight",
            value: {
              classificationListValueJwlry: ["101 "]
            }
          },
          {
            key: "Shape",
            value: {
              classificationListValueJwlry: ["Round "]
            }
          },
          {
            key: "Color",
            value: {
              classificationListValueJwlry: ["GH "]
            }
          },
          {
            key: "Setting",
            value: {
              classificationListValueJwlry: ["Prong "]
            }
          },
          {
            key: "Count",
            value: {
              classificationListValueJwlry: ["1 "]
            }
          }
        ]
      }
    },
    {
      key: "Abalone Details",
      value: {
        classificationListJwlry: [
          {
            key: "Colour",
            value: {
              classificationListValueJwlry: ["Beige"]
            }
          },
          {
            key: "Setting",
            value: {
              classificationListValueJwlry: ["Prong"]
            }
          },
          {
            key: "Shape",
            value: {
              classificationListValueJwlry: ["Round"]
            }
          },
          {
            key: "Count",
            value: {
              classificationListValueJwlry: ["3"]
            }
          }
        ]
      }
    }
  ],
  fulfillmentType: "sship",
  galleryImagesList: [
    {
      galleryImages: [
        {
          key: "product",
          value:
            "//pcmqa2.tataunistore.com/images/437Wx649H/MP000000000132739_437Wx649H_20170809175615.jpeg"
        },
        {
          key: "thumbnail",
          value:
            "//pcmqa2.tataunistore.com/images/97Wx144H/MP000000000132739_97Wx144H_20170809175615.jpeg"
        },
        {
          key: "searchPage",
          value:
            "//pcmqa2.tataunistore.com/images/252Wx374H/MP000000000132739_252Wx374H_20170809175615.jpeg"
        },
        {
          key: "mobilePdpView",
          value:
            "//pcmqa2.tataunistore.com/images/450Wx545H/MP000000000132739_450Wx545H_20170809175615.jpeg"
        },
        {
          key: "superZoom",
          value:
            "//pcmqa2.tataunistore.com/images/1348Wx2000H/MP000000000132739_1348Wx2000H_20170809175615.jpeg"
        },
        {
          key: "cartIcon",
          value:
            "//pcmqa2.tataunistore.com/images/97Wx144H/MP000000000132739_97Wx144H_20170809175615.jpeg"
        },
        {
          key: "zoom",
          value:
            "//pcmqa2.tataunistore.com/images/437Wx649H/MP000000000132739_437Wx649H_20170809175615.jpeg"
        },
        {
          key: "cartPage",
          value:
            "//pcmqa2.tataunistore.com/images/113Wx168H/MP000000000132739_113Wx168H_20170809175615.jpeg"
        }
      ],
      mediaType: "Image"
    },
    {
      galleryImages: [
        {
          key: "product",
          value:
            "//pcmqa2.tataunistore.com/images/437Wx649H/MP000000000132739_437Wx649H_20170809175616.jpeg"
        },
        {
          key: "thumbnail",
          value:
            "//pcmqa2.tataunistore.com/images/97Wx144H/MP000000000132739_97Wx144H_20170809175616.jpeg"
        },
        {
          key: "searchPage",
          value:
            "//pcmqa2.tataunistore.com/images/252Wx374H/MP000000000132739_252Wx374H_20170809175616.jpeg"
        },
        {
          key: "mobilePdpView",
          value:
            "//pcmqa2.tataunistore.com/images/450Wx545H/MP000000000132739_450Wx545H_20170809175616.jpeg"
        },
        {
          key: "superZoom",
          value:
            "//pcmqa2.tataunistore.com/images/1348Wx2000H/MP000000000132739_1348Wx2000H_20170809175616.jpeg"
        },
        {
          key: "cartIcon",
          value:
            "//pcmqa2.tataunistore.com/images/97Wx144H/MP000000000132739_97Wx144H_20170809175616.jpeg"
        },
        {
          key: "zoom",
          value:
            "//pcmqa2.tataunistore.com/images/437Wx649H/MP000000000132739_437Wx649H_20170809175616.jpeg"
        },
        {
          key: "cartPage",
          value:
            "//pcmqa2.tataunistore.com/images/113Wx168H/MP000000000132739_113Wx168H_20170809175616.jpeg"
        }
      ],
      mediaType: "Image"
    }
  ],
  isCOD: "Y",
  isEMIEligible: "Y",
  isOnlineExclusive: "N",
  isProductNew: "N",
  isSizeOrLength: "Size",
  knowMore: [
    {},
    {
      knowMoreItem: "Original 100% authentic certified jewelry from brands."
    },
    {
      knowMoreItem:
        "Shipped in tamper-proof boxes with security seal. Complete details will be mentioned in the order shipment e-mail."
    },
    {
      knowMoreItem: "Shipment includes brand's actual packaging."
    },
    {
      knowMoreItem: "Transit insured shipment."
    },
    {
      knowMoreItem:
        "Shipped directly by brands with all applicable brand provided service benefits."
    },
    {
      knowMoreItem:
        "An order, once placed, can be cancelled until the seller processes it."
    },
    {
      knowMoreItem:
        "Please check our Precious Jewellery related FAQ’s & Buying guide _here _ ( /fine-jewellery-faq )"
    },
    {
      knowMoreItem:
        "For any other queries, do reach out to CliQ Care at 90291 08282 or hello@tatacliq.com."
    }
  ],
  knowMoreEmail: "hello@tatacliq.com",
  knowMorePhoneNo: "1800 208 8282",
  maxQuantityAllowed: "10",
  mrp: "₹10867.00",
  numberOfReviews: 0,
  priceBreakUpDetailsMap: [
    {
      name: "DIAMOND",
      price: {
        currencyIso: "INR",
        doubleValue: 3500,
        formattedValue: "₹3500.00",
        priceType: "BUY",
        value: 3500
      },
      weightRateList: ["6Ct @ ₹0.00/Ct"]
    },
    {
      name: "GEMSTONE",
      price: {
        currencyIso: "INR",
        doubleValue: 5000,
        formattedValue: "₹5000.00",
        priceType: "BUY",
        value: 5000
      }
    },
    {
      name: "MAKING CHARGE",
      price: {
        currencyIso: "INR",
        doubleValue: 10,
        formattedValue: "₹10.00",
        priceType: "BUY",
        value: 10
      }
    },
    {
      name: "WASTAGE TAX",
      price: {
        currencyIso: "INR",
        doubleValue: 10,
        formattedValue: "₹10.00",
        priceType: "BUY",
        value: 10
      }
    }
  ],
  productCategory: "Pendants & Sets",
  productCategoryId: "MSH2016109",
  productDescription: "Trendy Jewellery Sets for Kids",
  productListingId: "MP000000000132739",
  productName: "NeedyBee Diamond Jewel Set",
  returnAndPolicies: [
    {
      refundReturnItem:
        "This product can be returned within 7 day(s) of delivery,subject to the Return Policy."
    },
    {
      refundReturnItem:
        "Complete your festive look with gold and diamond jewellery"
    },
    {
      refundReturnItem: "Browse through the trendiest Fashion Jewellery"
    }
  ],
  rootCategory: "FineJewellery",
  sellerAssociationstatus: "N",
  sharedText:
    "Wow!Check out this amazing find http://e2e.tataunistore.com/needybee-diamond-jewel-set/p-mp000000000132739 . Like or  comment to tell me what you think, or share for warm fuzzies.",
  showPriceBrkUpPDP: "Yes",
  styleNote: "Trendy Jewellery Sets for Kids",
  variantOptions: [
    {
      colorlink: {
        colorurl: "/needybee-diamond-jewel-set/p-mp000000000132739"
      },
      sizelink: {
        isAvailable: true,
        size: "NO SIZE",
        url: "/needybee-diamond-jewel-set/p-mp000000000132739"
      }
    }
  ],
  winningSellerAvailableStock: "1221",
  winningSellerMOP: "₹8480.00",
  winningSellerName: "Jewsell",
  winningUssID: "273568abc579798A"
};
const DELIVERY_TEXT = "Delivery Options For";
const PIN_CODE = "110011";
const PRODUCT_QUANTITY = "1";
export default class PdpJewellery extends React.Component {
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
    console.log(data.priceBreakUpDetailsMap);
    // const productData = this.props.productDetails;
    const productData = data;
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
          <ProductGalleryMobile isElectronics={true}>
            {mobileGalleryImages.map((val, idx) => {
              return (
                <Image image={val} key={idx} color="#f5f5f5" fit="contain" />
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
              showPriceBreakUp={productData.priceBreakUpDetailsMap}
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

          {productData.fineJewelleryClassificationList && (
            <div className={styles.details}>
              <PriceBreakUp data={productData.priceBreakUpDetailsMap} />
              <JewelleryClassification
                data={productData.fineJewelleryClassificationList}
              />
            </div>
          )}
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
