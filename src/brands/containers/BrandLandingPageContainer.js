import Feed from "../../home/components/Feed";
import { homeFeed } from "../../home/actions/home.actions";
import { connect } from "react-redux";
import { GET_FEED_DATA_FOR_BLP } from "../../lib/constants";
const mapDispatchToProps = dispatch => {
  return {
    homeFeed: () => {
      dispatch(homeFeed(GET_FEED_DATA_FOR_BLP));
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
