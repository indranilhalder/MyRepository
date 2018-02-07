import { connect } from "react-redux";
import {
  getProductDescription,
  getProductPinCode,
  addProductToWishList,
  removeProductFromWishList,
  addProductToBag
} from "../actions/pdp.actions";
import ProductDescriptionPage from "../components/ProductDescriptionPage";
import { withRouter } from "react-router-dom";

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
    }
  };
};

const mapStateToProps = state => {
  return {
    loading: state.productDescription.loading,
    productDescription: state.productDescription.productDescription
  };
};

const ProductDescriptionContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductDescriptionPage)
);

export default ProductDescriptionContainer;
