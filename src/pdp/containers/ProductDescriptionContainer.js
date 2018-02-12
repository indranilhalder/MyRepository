import { connect } from "react-redux";
import {
  getProductDescription,
  getProductPinCode,
  addProductToWishList,
  removeProductFromWishList,
  addProductToBag,
  getProductSizeGuide,
  getPdpEmi
} from "../actions/pdp.actions";
import ProductDescriptionPage from "../components/ProductDescriptionPage";
import { withRouter } from "react-router-dom";
import {
  showModal,
  PRODUCT_COUPONS,
  SIZE_GUIDE
} from "../../general/modal.actions";

const mapDispatchToProps = dispatch => {
  return {
    getProductDescription: () => {
      dispatch(getProductDescription());
    },
    getProductPinCode: productDetails => {
      dispatch(getProductPinCode(productDetails));
    },
    addProductToWishList: productDetails => {
      dispatch(addProductToWishList(productDetails));
    },
    removeProductFromWishList: productDetails => {
      dispatch(removeProductFromWishList(productDetails));
    },
    addProductToBag: productDetails => {
      dispatch(addProductToBag(productDetails));
    },
    showCouponModal: data => {
      dispatch(showModal(PRODUCT_COUPONS, data));
    },
    getProductSizeGuide: () => {
      dispatch(getProductSizeGuide());
    },
    getPdpEmi: () => {
      dispatch(getPdpEmi());
    },
    showSizeGuide: () => {
      dispatch(showModal(SIZE_GUIDE));
    }
  };
};

const mapStateToProps = state => {
  return {
    loading: state.productDescription.loading,
    productDetails: state.productDescription.productDetails
  };
};

const ProductDescriptionContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductDescriptionPage)
);

export default ProductDescriptionContainer;
