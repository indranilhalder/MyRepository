import { connect } from "react-redux";
import { getComponentData } from "../actions/home.actions";
import { withRouter } from "react-router-dom";
import FeedComponent from "../components/FeedComponent";
const mapDispatchToProps = dispatch => {
  return {
    getComponentData: (fetchUrl, positionInFeed) => {
      dispatch(getComponentData(positionInFeed, fetchUrl));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  const positionInFeed = ownProps.positionInFeed;
  const feedComponentData = state.home.homeFeed[ownProps.positionInFeed];
  return {
    feedComponentData: feedComponentData,
    positionInFeed
  };
};

const FeedComponentContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(FeedComponent)
);

export default FeedComponentContainer;
