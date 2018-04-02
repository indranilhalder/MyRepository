import { connect } from "react-redux";
import { homeFeed } from "../actions/home.actions";
import { getCartId } from "../../cart/actions/cart.actions";
import { getWishListItems } from "../../wishlist/actions/wishlist.actions";
import Feed from "../components/Feed";
import { setHeaderText } from "../../general/header.actions";
import { withRouter } from "react-router-dom";
const mapDispatchToProps = dispatch => {
  return {
    homeFeed: () => {
      dispatch(homeFeed());
    },
    getCartId: () => {
      dispatch(getCartId());
    },
    getWishListItems: () => {
      dispatch(getWishListItems());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};

const mapStateToProps = state => {
  let headerMessage = "Welcome Guest";
  if (state.user.isLoggedIn) {
    headerMessage = `Welcome ${state.user.user.firstName}`;
  }
  return {
    homeFeedData: state.home.homeFeed,
    isHomeFeedPage: true,
    loading: state.home.loading,
    type: state.cart.type,
    headerMessage
  };
};

const HomeContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Feed)
);

export default HomeContainer;
