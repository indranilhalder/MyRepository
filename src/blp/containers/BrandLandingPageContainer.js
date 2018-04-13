import Feed from "../../home/components/Feed";
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
const BrandLandingPageContainer = connect(mapStateToProps, mapDispatchToProps)(
  Feed
);
export default BrandLandingPageContainer;
