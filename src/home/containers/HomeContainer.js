import { connect } from "react-redux";
import { getFeed } from "../actions/home.actions";
import { getCartId } from "../../cart/actions/cart.actions";
import { getWishListItems } from "../../wishlist/actions/wishlist.actions";
import Feed from "../components/Feed";
import { setHeaderText } from "../../general/header.actions";
import { withRouter } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import { LOGGED_IN_USER_DETAILS } from "../../lib/constants";
const mapDispatchToProps = dispatch => {
  return {
    homeFeed: () => {
      dispatch(getFeed());
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
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

  if (userDetails) {
    userDetails = JSON.parse(userDetails);
    if (userDetails.firstName && userDetails.firstName !== "undefined") {
      headerMessage = `Welcome ${userDetails.firstName}`;
    } else {
      headerMessage = `Welcome`;
    }
  }
  return {
    homeFeedData: state.feed.homeFeed,
    isHomeFeedPage: true,
    loading: state.feed.loading,
    type: state.cart.type,
    headerMessage,
    loginFromMyBag: state.cart.loginFromMyBag
  };
};

const HomeContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Feed)
);

export default HomeContainer;
