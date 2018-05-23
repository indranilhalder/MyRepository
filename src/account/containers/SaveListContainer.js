import { connect } from "react-redux";
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
import { SUCCESS_FOR_ADDING_TO_BAG } from "../../lib/constants.js";
const REMOVED_SAVELIST = "Removed Successfully";

const mapDispatchToProps = dispatch => {
  return {
    getWishList: () => {
      dispatch(getWishListItems(true)); // true is passing for set data layer on my account save list tab
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    addProductToCart: (userId, cartId, accessToken, productDetails) => {
      dispatch(
        addProductToCart(userId, cartId, accessToken, productDetails)
      ).then(result => {
        if (result.status === SUCCESS) {
          dispatch(removeProductFromWishList(productDetails)).then(response => {
            if (response.status === SUCCESS) {
              dispatch(displayToast(SUCCESS_FOR_ADDING_TO_BAG));
            }
          });
        }
      });
    },
    removeProductFromWishList: productDetails => {
      dispatch(removeProductFromWishList(productDetails)).then(response => {
        if (response.status === SUCCESS) {
          dispatch(displayToast(REMOVED_SAVELIST));
        }
      });
    }
  };
};
const mapStateToProps = state => {
  return {
    wishList: state.wishlistItems.wishlistItems,
    loading: state.wishlistItems.loading,
    count: state.wishlistItems.count
  };
};

const SaveListContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SaveListDetails)
);

export default SaveListContainer;
