import { connect } from "react-redux";
import { homeFeed } from "../actions/home.actions";
import { getCartId } from "../../cart/actions/cart.actions";
import Feed from "../components/Feed";

import { withRouter } from "react-router-dom";
const mapDispatchToProps = dispatch => {
  return {
    homeFeed: () => {
      dispatch(homeFeed());
    },
    getCartId: () => {
      dispatch(getCartId());
    }
  };
};

const mapStateToProps = state => {
  return {
    homeFeedData: state.home.homeFeed,
    loading: state.home.loading,
    type: state.cart.type
  };
};

const HomeContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Feed)
);

export default HomeContainer;
