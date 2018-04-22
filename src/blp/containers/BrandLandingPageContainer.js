import Feed from "../../home/components/Feed";
import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = dispatch => {
  return {
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};
const mapStateToProps = state => {
  return {
    homeFeedData: state.home.homeFeed,
    loading: state.home.loading,
    isOnBrandLandingPage: true
  };
};
const BrandLandingPageContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Feed)
);
export default BrandLandingPageContainer;
