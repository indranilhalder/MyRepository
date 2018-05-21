import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import { setHeaderText } from "../../general/header.actions";
import { displayToast } from "../../general/toast.actions";
import { homeFeed } from "../../home/actions/home.actions";

import StaticPage from "../components/StaticPage";
const mapDispatchToProps = dispatch => {
  return {
    getStaticPage: pageId => {
      dispatch(homeFeed(pageId));
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
    data: state.home.homeFeed,
    loading: state.home.loading,
    feedType: state.home.feedType,
    status: state.home.status
  };
};

const StaticPageContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(StaticPage)
);

export default StaticPageContainer;
