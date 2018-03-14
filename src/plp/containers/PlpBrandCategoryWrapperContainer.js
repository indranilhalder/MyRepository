import { connect } from "react-redux";
import { homeFeed } from "../../home/actions/home.actions";
import PlpBrandCategoryWrapper from "../components/PlpBrandCategoryWrapper";

const mapDispatchToProps = dispatch => {
  return {
    homeFeed: brandIdOrCategoryId => {
      dispatch(homeFeed(brandIdOrCategoryId));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    homeFeedData: state.home
  };
};

const PlpBrandCategoryWrapperContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(PlpBrandCategoryWrapper);

export default PlpBrandCategoryWrapperContainer;
