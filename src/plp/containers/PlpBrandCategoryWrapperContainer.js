import { connect } from "react-redux";
import { getFeed } from "../../home/actions/home.actions";
import PlpBrandCategoryWrapper from "../components/PlpBrandCategoryWrapper";

const mapDispatchToProps = dispatch => {
  return {
    getFeed: brandIdOrCategoryId => {
      dispatch(getFeed(brandIdOrCategoryId));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    homeFeedData: state.feed
  };
};

const PlpBrandCategoryWrapperContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(PlpBrandCategoryWrapper);

export default PlpBrandCategoryWrapperContainer;
