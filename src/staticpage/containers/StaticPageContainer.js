import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import { setHeaderText } from "../../general/header.actions";
import { displayToast } from "../../general/toast.actions";
import { getFeed } from "../../home/actions/home.actions";

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
    data: state.home.homeFeed,
    loading: state.home.loading
  };
};

const StaticPageContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(StaticPage)
);

export default StaticPageContainer;
