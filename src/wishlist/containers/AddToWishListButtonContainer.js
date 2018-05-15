import { connect } from "react-redux";
import { displayToast } from "../../general/toast.actions";
import AddToWishListButton from "../components/AddToWishListButton";
import { addProductToWishList } from "../actions/wishlist.actions";
import { SUCCESS } from "../../lib/constants";
import { withRouter } from "react-router-dom";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";
import { removeItemFromCartLoggedIn } from "../../cart/actions/cart.actions";
import { DEFAULT_PIN_CODE_LOCAL_STORAGE } from "../../lib/constants";
const toastMessageOnSuccessAddToWishlist = "Added";

const toastMessageOnAlreadyInWishlist = "Already in wishlist";

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    addProductToWishList: async productObj => {
      const wishlistResponse = await dispatch(
        addProductToWishList(productObj, ownProps.setDataLayerType)
      );
      if (wishlistResponse.status === SUCCESS) {
        if (ownProps.index >= 0) {
          dispatch(
            removeItemFromCartLoggedIn(
              ownProps.index,
              localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
            )
          );
        }
        dispatch(displayToast(toastMessageOnSuccessAddToWishlist));
      }
    },
    displayToast: () => {
      dispatch(displayToast(toastMessageOnAlreadyInWishlist));
    },

    setUrlToRedirectToAfterAuth: url => {
      dispatch(setUrlToRedirectToAfterAuth(url));
    }
  };
};
// here mandatory props is product ojb.
const mapStateToProps = (state, ownProps) => {
  return {
    productListingId: ownProps.productListingId,
    winningUssID: ownProps.winningUssID,
    isWhite: ownProps.isWhite,
    wishlistItems: state.wishlistItems.wishlistItems,
    type: ownProps.type,
    index: ownProps.index
  };
};
const AddToWishListButtonContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AddToWishListButton)
);
export default AddToWishListButtonContainer;
