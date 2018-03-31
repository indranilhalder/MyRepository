import { connect } from "react-redux";
import { displayToast } from "../../general/toast.actions";
import WishlistIcon from "../components/WishlistIcon";
import { addProductToWishList } from "../actions/wishlist.actions";
import { SUCCESS } from "../../lib/constants";

const toastMessageOnSuccessAddToWishlist = "Added";
const toastMessageOnFailureAddToWishlist = "Failed";
const toastMessageOnAlreadyInWishlist = "Already in wishlist";

const mapDispatchToProps = dispatch => {
  return {
    addProductToWishList: async productObj => {
      const wishlistResponse = await dispatch(addProductToWishList(productObj));
      if (wishlistResponse.status === SUCCESS) {
        dispatch(displayToast(toastMessageOnSuccessAddToWishlist));
      } else {
        dispatch(displayToast(toastMessageOnFailureAddToWishlist));
      }
    },
    displayToast: () => {
      dispatch(displayToast(toastMessageOnAlreadyInWishlist));
    }
  };
};
// here mandatory props is product ojb.
const mapStateToProps = (state, ownProps) => {
  return {
    productListingId: ownProps.productListingId,
    winningUssID: ownProps.winningUssID,
    wishlistItems: state.wishlistItems.wishlistItems
  };
};
const WishlistContainer = connect(mapStateToProps, mapDispatchToProps)(
  WishlistIcon
);
export default WishlistContainer;
