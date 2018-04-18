import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import { setHeaderText } from "../../general/header.actions";
import { getStaticPage } from "../actions/staticPage.actions";
import { displayToast } from "../../general/toast.actions";
import StaticPage from "../components/StaticPage";
const mapDispatchToProps = dispatch => {
  return {
    getStaticPage: pageId => {
      dispatch(getStaticPage(pageId));
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
    data: state.staticPage.data,
    loading: state.staticPage.loading
  };
};

const StaticPageContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(StaticPage)
);

export default StaticPageContainer;
