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
    getProductPinCode: () => {
      dispatch(getProductPinCode());
    },
    addProductToWishList: () => {
      dispatch(addProductToWishList());
    },
    removeProductFromWishList: () => {
      dispatch(removeProductFromWishList());
    },
    addProductToBag: () => {
      dispatch(addProductToBag());
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
