import { connect } from "react-redux";
import { getWishList } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import SaveListDetails from "../components/SaveListDetails";
import { setHeaderText } from "../../general/header.actions";
import {
  removeProductFromWishList,
  addProductToCart
} from "../../pdp/actions/pdp.actions";
import { SUCCESS } from "../../lib/constants";
const mapDispatchToProps = dispatch => {
  return {
    getWishList: () => {
      dispatch(getWishList());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    addProductToCart: (userId, cartId, accessToken, productDetails) => {
      dispatch(addProductToCart(userId, cartId, accessToken, productDetails));
    },
    removeProductFromWishList: productDetails => {
      dispatch(removeProductFromWishList(productDetails)).then(response => {
        if (response.status === SUCCESS) {
          return dispatch(getWishList());
        } else {
          return response;
        }
      });
    }
  };
};
const mapStateToProps = state => {
  return {
    wishList: state.profile.wishlist,
    loading: state.profile.loadingForWishlist
  };
};

const SaveListContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SaveListDetails)
);

export default SaveListContainer;
