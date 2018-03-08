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
  return {
    productDetails: state.productDescription.productDetails,
    msdItems: state.productDescription.msdItems,
    sizeGuide: state.productDescription.sizeGuide,
    loading: state.productDescription.loading
  };
};

const ProductDescriptionPageWrapperContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductDescriptionPageWrapper)
);

export default ProductDescriptionPageWrapperContainer;
