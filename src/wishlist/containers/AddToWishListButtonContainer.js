import { connect } from "react-redux";
import { displayToast } from "../../general/toast.actions";
import AddToWishListButton from "../components/AddToWishListButton";
import { addProductToWishList } from "../actions/wishlist.actions";
import { SUCCESS } from "../../lib/constants";
import { withRouter } from "react-router-dom";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";

const toastMessageOnSuccessAddToWishlist = "Added";

const toastMessageOnAlreadyInWishlist = "Already in wishlist";

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    addProductToWishList: async productObj => {
      const wishlistResponse = await dispatch(
        addProductToWishList(productObj, ownProps.setDataLayerType)
      );
      if (wishlistResponse.status === SUCCESS) {
        dispatch(displayToast(toastMessageOnSuccessAddToWishlist));
      }
    },
    displayToast: () => {
      console.log("TOAST MESSAGE ALREADY DISPLAYED");
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
    type: ownProps.type
  };
};
const AddToWishListButtonContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AddToWishListButton)
);
export default AddToWishListButtonContainer;
