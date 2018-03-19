import { connect } from "react-redux";
import { getWishList } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import SaveListDetails from "../components/SaveListDetails";
import {
  removeProductFromWishList,
  addProductToCart
} from "../../pdp/actions/pdp.actions";

const mapDispatchToProps = dispatch => {
  return {
    getWishList: () => {
      dispatch(getWishList());
    },
    addProductToCart: (userId, cartId, accessToken, productDetails) => {
      dispatch(addProductToCart(userId, cartId, accessToken, productDetails));
    },
    removeProductFromWishList: productDetails => {
      dispatch(removeProductFromWishList(productDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    profile: state.profile
  };
};

const SaveListContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SaveListDetails)
);

export default SaveListContainer;
