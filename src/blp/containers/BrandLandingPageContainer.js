import Feed from "../../home/components/Feed";
import { connect } from "react-redux";
import { setHeaderText } from "../../general/header.actions";
import { SECONDARY_FEED_TYPE } from "../../lib/constants";
const mapDispatchToProps = dispatch => {
  return {
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};
const mapStateToProps = state => {
  return {
    homeFeedData: state.feed.secondaryFeed,
    loading: state.feed.loading,
    isOnBrandLandingPage: true,
    feedType: SECONDARY_FEED_TYPE
  };
};
const BrandLandingPageContainer = connect(mapStateToProps, mapDispatchToProps)(
  Feed
);
export default BrandLandingPageContainer;
