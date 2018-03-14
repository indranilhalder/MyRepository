import { connect } from "react-redux";
import {
  getProductDescription,
  addProductToCart,
  getProductSizeGuide,
  addProductToWishList,
  getMsdRequest,
  getPdpEmi,
  getEmiTerms,
  getProductPinCode
} from "../actions/pdp.actions";
import {
  showModal,
  EMI_MODAL,
  ADDRESS,
  SIZE_GUIDE
} from "../../general/modal.actions.js";
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
    },
    showSizeGuide: () => {
      dispatch(showModal(SIZE_GUIDE));
    },
    getPdpEmi: (token, cartValue) => {
      dispatch(getPdpEmi(token, cartValue));
    },
    getEmiTerms: (token, productValue) => {
      dispatch(getEmiTerms(token, productValue));
    },
    showEmiModal: () => {
      dispatch(showModal(EMI_MODAL));
    },
    showPincodeModal: productCode => {
      dispatch(showModal(ADDRESS, { productCode }));
    },
    getProductPinCode: (pinCode, productCode) => {
      dispatch(getProductPinCode(pinCode, productCode));
    }
  };
};

const mapStateToProps = state => {
  return {
    productDetails: state.productDescription.productDetails,
    msdItems: state.productDescription.msdItems,
    loading: state.productDescription.loading
  };
};

const ProductDescriptionPageWrapperContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductDescriptionPageWrapper)
);

export default ProductDescriptionPageWrapperContainer;
