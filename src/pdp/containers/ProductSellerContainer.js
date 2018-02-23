import { connect } from "react-redux";
import ProductSellerPage from "../components/ProductSellerPage";
import { withRouter } from "react-router-dom";
import { addProductToWishList, addProductToCart } from "../actions/pdp.actions";
const mapDispatchToProps = dispatch => {
  return {
    addProductToCart: productDetails => {
      dispatch(addProductToCart(productDetails));
    },
    addProductToWishList: productDetails => {
      dispatch(addProductToWishList(productDetails));
    }
  };
};
const mapStateToProps = state => {
  return {
    productDetails: state.productDescription.productDetails
  };
};

const ProductSellerContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductSellerPage)
);

export default ProductSellerContainer;
