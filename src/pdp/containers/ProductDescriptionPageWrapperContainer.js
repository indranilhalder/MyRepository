import { connect } from "react-redux";
import {
  getProductDescription,
  addProductToCart,
  getProductSizeGuide,
  addProductToWishList,
  getMsdRequest,
  getPdpEmi,
  getEmiTerms
} from "../actions/pdp.actions";
import { checkPinCodeServiceAvailability } from "../../cart/actions/cart.actions";
import { showModal, EMI_MODAL, ADDRESS } from "../../general/modal.actions.js";
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
    getPdpEmi: (token, cartValue) => {
      dispatch(getPdpEmi(token, cartValue));
    },
    getEmiTerms: (token, productValue) => {
      dispatch(getEmiTerms(token, productValue));
    },
    showEmiModal: () => {
      dispatch(showModal(EMI_MODAL));
    },
    showSizeGuide: () => {
      dispatch(showModal("SizeGuide"));
    },
    showPincodeModal: productCode => {
      dispatch(showModal(ADDRESS, { productCode }));
    },
    checkPinCodeServiceAvailability: (pincode, productCode) => {
      dispatch(checkPinCodeServiceAvailability(pincode, productCode));
    }
  };
};

const mapStateToProps = state => {
  return {
    pinCodeServiceAvailability: state.cart.pinCodeServiceAvailability,
    productDetails: state.productDescription.productDetails,
    msdItems: state.productDescription.msdItems,
    loading: state.productDescription.loading
  };
};

const ProductDescriptionPageWrapperContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductDescriptionPageWrapper)
);

export default ProductDescriptionPageWrapperContainer;
