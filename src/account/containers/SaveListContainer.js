import { connect } from "react-redux";
import { getWishList } from "../actions/account.actions";
import {
  getWishListItems,
  removeProductFromWishList
} from "../../wishlist/actions/wishlist.actions";
import { withRouter } from "react-router-dom";
import SaveListDetails from "../components/SaveListDetails";
import { setHeaderText } from "../../general/header.actions";
import { displayToast } from "../../general/toast.actions";
import { addProductToCart } from "../../pdp/actions/pdp.actions";
import { SUCCESS } from "../../lib/constants";
const REMOVED_SAVELIST = "Removed Successfully";
const mapDispatchToProps = dispatch => {
  return {
    getWishList: () => {
      dispatch(getWishListItems());
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
          dispatch(displayToast(REMOVED_SAVELIST));
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
    wishList: state.wishlistItems.wishlistItems
  };
};

const SaveListContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SaveListDetails)
);

export default SaveListContainer;
