import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import { setHeaderText } from "../../general/header.actions";
import { displayToast } from "../../general/toast.actions";
import { getFeed } from "../../home/actions/home.actions";
import { SECONDARY_FEED_TYPE } from "../../lib/constants";

import StaticPage from "../components/StaticPage";
const mapDispatchToProps = dispatch => {
  return {
    getStaticPage: pageId => {
      dispatch(getFeed(pageId));
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    displayToast: text => {
      dispatch(displayToast(text));
    }
  };
};

const mapStateToProps = state => {
  return {
    data: state.feed.secondaryFeed,
    loading: state.feed.loading,
    feedType: SECONDARY_FEED_TYPE,
    status: state.feed.secondaryFeedStatus
  };
};

const StaticPageContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(StaticPage)
);

export default StaticPageContainer;
