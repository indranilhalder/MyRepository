import Feed from "../../home/components/Feed";
import { homeFeed } from "../../home/actions/home.actions";
import { connect } from "react-redux";

const mapStateToProps = state => {
  return {
    homeFeedData: state.home.homeFeed,
    loading: state.home.loading,
    isOnBrandLandingPage: true
  };
};
const BrandLandingPageContainer = connect(mapStateToProps)(Feed);
export default BrandLandingPageContainer;
