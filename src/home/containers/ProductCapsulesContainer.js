import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import { getProductCapsules } from "../actions/home.actions.js";
import ProductCapsules from "../components/ProductCapsules";

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getProductCapsules: positionInFeed =>
      dispatch(getProductCapsules(positionInFeed)),
    setClickedElementId: id => {
      ownProps.setClickedElementId(id);
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    productCapsulesLoading: state.feed.productCapsulesLoading,
    feedComponentData: state.feed.homeFeed[ownProps.positionInFeed],
    positionInFeed: ownProps.positionInFeed,
    id: ownProps.id
  };
};

const ProductCapsulesContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductCapsules)
);

export default ProductCapsulesContainer;
