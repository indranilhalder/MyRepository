import { connect } from "react-redux";
import {
  getProductDescription,
  addProductToCart,
  getProductSizeGuide,
  addProductToWishList,
  getMsdRequest
} from "../actions/pdp.actions";
import ProductDescriptionPageWrapper from "../components/ProductDescriptionPageWrapper";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    getProductDescription: productCode => {
      dispatch(getProductDescription(productCode));
    },
    addProductToCart: (userId, cartId, accessToken, productDetails) => {
      dispatch(addProductToCart(userId, cartId, accessToken, productDetails));
    },
    getProductSizeGuide: productCode => {
      dispatch(getProductSizeGuide(productCode));
    },
    addProductToWishList: (userId, accessToken, productDetails) => {
      dispatch(addProductToWishList(userId, accessToken, productDetails));
    },
    getMsdRequest: productCode => {
      dispatch(getMsdRequest(productCode));
    }
  };
};

const mapStateToProps = state => {
  const productDetails = {
    type: "productDetailMobileWsData",
    allOOStock: false,
    brandInfo:
      "Abridging fine ethnic fashion and contemporary silhouettes, Utsa By Westside will easily become the go to brand for any fashion connoisseur. The vibrant palette along with chic ethnic designs are curated for you to dress up with, here at Tata CLiQ.",
    brandName: "Utsa",
    deliveryModesATP: [
      {
        key: "home-delivery",
        value: "Delivered in 3-6 business days."
      },
      {
        key: "express-delivery",
        value: "Delivered in 1-2 business days."
      }
    ],
    details: [
      {
        key: "Fit",
        value: "Regular fit"
      },
      {
        key: "Pattern",
        value: "Printed"
      },
      {
        key: "Color",
        value: "Black"
      },
      {
        key: "Model fit",
        value: "Model is 5'8\"/173 cm and is wearing a small"
      },
      {
        key: "Wash Care",
        value: "Hand wash in running water"
      },
      {
        key: "Sleeve",
        value: "3/4th sleeve"
      },
      {
        key: "Fabric",
        value: "Cotton"
      }
    ],
    discount: "0",
    eligibleDeliveryModes: [
      {
        code: "home-delivery",
        displayCost: "₹0.00",
        name: "Home Delivery"
      },
      {
        code: "express-delivery",
        displayCost: "₹0.00",
        name: "Express Delivery"
      },
      {
        code: "click-and-collect",
        displayCost: "₹0.00",
        name: "Click and Collect"
      }
    ],
    fulfillmentType: "tship",
    galleryImagesList: [
      {
        galleryImages: [
          {
            key: "product",
            value:
              "//img.tatacliq.com/images/i3/437Wx649H/MP000000002316862_437Wx649H_20180124140949.jpeg"
          },
          {
            key: "thumbnail",
            value:
              "//img.tatacliq.com/images/i3/97Wx144H/MP000000002316862_97Wx144H_20180124140949.jpeg"
          },
          {
            key: "searchPage",
            value:
              "//img.tatacliq.com/images/i3/252Wx374H/MP000000002316862_252Wx374H_20180124140949.jpeg"
          },
          {
            key: "mobilePdpView",
            value:
              "//img.tatacliq.com/images/i3/450Wx545H/MP000000002316862_450Wx545H_20180124140949.jpeg"
          },
          {
            key: "superZoom",
            value:
              "//img.tatacliq.com/images/i3/1348Wx2000H/MP000000002316862_1348Wx2000H_20180124140949.jpeg"
          },
          {
            key: "cartIcon",
            value:
              "//img.tatacliq.com/images/i3/97Wx144H/MP000000002316862_97Wx144H_20180124140949.jpeg"
          },
          {
            key: "zoom",
            value:
              "//img.tatacliq.com/images/i3/437Wx649H/MP000000002316862_437Wx649H_20180124140949.jpeg"
          },
          {
            key: "cartPage",
            value:
              "//img.tatacliq.com/images/i3/113Wx168H/MP000000002316862_113Wx168H_20180124140949.jpeg"
          }
        ],
        mediaType: "Image"
      },
      {
        galleryImages: [
          {
            key: "product",
            value:
              "//img.tatacliq.com/images/i3/437Wx649H/MP000000002316862_437Wx649H_20180124140942.jpeg"
          },
          {
            key: "thumbnail",
            value:
              "//img.tatacliq.com/images/i3/97Wx144H/MP000000002316862_97Wx144H_20180124140942.jpeg"
          },
          {
            key: "searchPage",
            value:
              "//img.tatacliq.com/images/i3/252Wx374H/MP000000002316862_252Wx374H_20180124140942.jpeg"
          },
          {
            key: "mobilePdpView",
            value:
              "//img.tatacliq.com/images/i3/450Wx545H/MP000000002316862_450Wx545H_20180124140942.jpeg"
          },
          {
            key: "superZoom",
            value:
              "//img.tatacliq.com/images/i3/1348Wx2000H/MP000000002316862_1348Wx2000H_20180124140942.jpeg"
          },
          {
            key: "cartIcon",
            value:
              "//img.tatacliq.com/images/i3/97Wx144H/MP000000002316862_97Wx144H_20180124140942.jpeg"
          },
          {
            key: "zoom",
            value:
              "//img.tatacliq.com/images/i3/437Wx649H/MP000000002316862_437Wx649H_20180124140942.jpeg"
          },
          {
            key: "cartPage",
            value:
              "//img.tatacliq.com/images/i3/113Wx168H/MP000000002316862_113Wx168H_20180124140942.jpeg"
          }
        ],
        mediaType: "Image"
      },
      {
        galleryImages: [
          {
            key: "product",
            value:
              "//img.tatacliq.com/images/i3/437Wx649H/MP000000002316862_437Wx649H_20180124140946.jpeg"
          },
          {
            key: "thumbnail",
            value:
              "//img.tatacliq.com/images/i3/97Wx144H/MP000000002316862_97Wx144H_20180124140946.jpeg"
          },
          {
            key: "searchPage",
            value:
              "//img.tatacliq.com/images/i3/252Wx374H/MP000000002316862_252Wx374H_20180124140946.jpeg"
          },
          {
            key: "mobilePdpView",
            value:
              "//img.tatacliq.com/images/i3/450Wx545H/MP000000002316862_450Wx545H_20180124140946.jpeg"
          },
          {
            key: "superZoom",
            value:
              "//img.tatacliq.com/images/i3/1348Wx2000H/MP000000002316862_1348Wx2000H_20180124140946.jpeg"
          },
          {
            key: "cartIcon",
            value:
              "//img.tatacliq.com/images/i3/97Wx144H/MP000000002316862_97Wx144H_20180124140946.jpeg"
          },
          {
            key: "zoom",
            value:
              "//img.tatacliq.com/images/i3/437Wx649H/MP000000002316862_437Wx649H_20180124140946.jpeg"
          },
          {
            key: "cartPage",
            value:
              "//img.tatacliq.com/images/i3/113Wx168H/MP000000002316862_113Wx168H_20180124140946.jpeg"
          }
        ],
        mediaType: "Image"
      },
      {
        galleryImages: [
          {
            key: "product",
            value:
              "//img.tatacliq.com/images/i3/437Wx649H/MP000000002316862_437Wx649H_20180124140939.jpeg"
          },
          {
            key: "thumbnail",
            value:
              "//img.tatacliq.com/images/i3/97Wx144H/MP000000002316862_97Wx144H_20180124140939.jpeg"
          },
          {
            key: "searchPage",
            value:
              "//img.tatacliq.com/images/i3/252Wx374H/MP000000002316862_252Wx374H_20180124140939.jpeg"
          },
          {
            key: "mobilePdpView",
            value:
              "//img.tatacliq.com/images/i3/450Wx545H/MP000000002316862_450Wx545H_20180124140939.jpeg"
          },
          {
            key: "superZoom",
            value:
              "//img.tatacliq.com/images/i3/1348Wx2000H/MP000000002316862_1348Wx2000H_20180124140939.jpeg"
          },
          {
            key: "cartIcon",
            value:
              "//img.tatacliq.com/images/i3/97Wx144H/MP000000002316862_97Wx144H_20180124140939.jpeg"
          },
          {
            key: "zoom",
            value:
              "//img.tatacliq.com/images/i3/437Wx649H/MP000000002316862_437Wx649H_20180124140939.jpeg"
          },
          {
            key: "cartPage",
            value:
              "//img.tatacliq.com/images/i3/113Wx168H/MP000000002316862_113Wx168H_20180124140939.jpeg"
          }
        ],
        mediaType: "Image"
      }
    ],
    isCOD: "Y",
    isEMIEligible: "N",
    isOnlineExclusive: "N",
    isProductNew: "N",
    knowMore: [
      {
        knowMoreItem:
          "An order, once placed, can be cancelled until the seller processes it."
      },
      {
        knowMoreItem:
          "This product can be returned within 30 day(s) of delivery,subject to the Return Policy."
      },
      {
        knowMoreItem:
          "For any other queries, do reach out to CliQ Care at 90291 08282 or hello@tatacliq.com"
      }
    ],
    knowMoreEmail: "hello@tatacliq.com",
    knowMorePhoneNo: "90291 08282",
    maxQuantityAllowed: "5",
    mrp: "₹1099.00",
    productCategory: "Kurtis & Kurtas",
    productCategoryId: "MSH1012100",
    productDescription:
      "Add some character into your ethnic line-up with this black kurta from Utsa. Cut in an A-line design that features slitted side pockets, tab sleeves and a button-fastened front, this self-printed style comes rendered in a breathable fabric to promote all-day comfort.",
    productListingId: "MP000000002318352",
    productName: "Utsa by Westside Black Kurta",
    rootCategory: "Clothing",
    sellerAssociationstatus: "N",
    sharedText:
      "Wow!Check out this amazing find http://www.tatacliq.com/utsa-by-westside-black-kurta/p-mp000000002318352 . Like or  comment to tell me what you think, or share for warm fuzzies.",
    styleNote:
      "Add some character into your ethnic line-up with this black kurta from Utsa. Cut in an A-line design that features slitted side pockets, tab sleeves and a button-fastened front, this self-printed style comes rendered in a breathable fabric to promote all-day comfort.",
    variantOptions: [
      {
        colorlink: {
          color: "Black",
          colorHexCode: "#000000",
          colorurl: "/utsa-by-westside-black-kurta/p-mp000000002316862"
        },
        sizelink: {
          isAvailable: true,
          size: "XS",
          url: "/utsa-by-westside-black-kurta/p-mp000000002316862"
        }
      },
      {
        colorlink: {
          color: "Black",
          colorHexCode: "#000000",
          colorurl: "/utsa-by-westside-black-kurta/p-mp000000002318341"
        },
        sizelink: {
          isAvailable: true,
          size: "S",
          url: "/utsa-by-westside-black-kurta/p-mp000000002318341"
        }
      },
      {
        colorlink: {
          color: "Black",
          colorHexCode: "#000000",
          colorurl: "/utsa-by-westside-black-kurta/p-mp000000002318343"
        },
        sizelink: {
          isAvailable: true,
          size: "M",
          url: "/utsa-by-westside-black-kurta/p-mp000000002318343"
        }
      },
      {
        colorlink: {
          color: "Black",
          colorHexCode: "#000000",
          colorurl: "/utsa-by-westside-black-kurta/p-mp000000002318346"
        },
        sizelink: {
          isAvailable: true,
          size: "L",
          url: "/utsa-by-westside-black-kurta/p-mp000000002318346"
        }
      },
      {
        colorlink: {
          color: "Black",
          colorHexCode: "#000000",
          colorurl: "/utsa-by-westside-black-kurta/p-mp000000002318352"
        },
        sizelink: {
          isAvailable: true,
          size: "XL",
          url: "/utsa-by-westside-black-kurta/p-mp000000002318352"
        }
      },
      {
        colorlink: {
          color: "Black",
          colorHexCode: "#000000",
          colorurl: "/utsa-by-westside-black-kurta/p-mp000000002318354"
        },
        sizelink: {
          isAvailable: true,
          size: "XXL",
          url: "/utsa-by-westside-black-kurta/p-mp000000002318354"
        }
      }
    ],
    winningSellerAvailableStock: "29",
    winningSellerMOP: "₹1099.00",
    winningSellerName: "Westside",
    winningUssID: "100001300712393005"
  };
  return {
    productDetails,
    msdItems: state.productDescription.msdItems,
    sizeGuide: state.productDescription.sizeGuide
  };
};

const ProductDescriptionPageWrapperContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductDescriptionPageWrapper)
);

export default ProductDescriptionPageWrapperContainer;
