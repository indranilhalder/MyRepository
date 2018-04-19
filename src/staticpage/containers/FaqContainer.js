import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import FaqPage from "../components/FaqPage.js";
import { getFaqDetails } from "../actions/staticPage.actions";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = dispatch => {
  return {
    getFaqDetails: () => {
      dispatch(getFaqDetails());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};

const mapStateToProps = state => {
  return {
    faq: state.staticPage.faq,
    faqStatus: state.staticPage.faqStatus,
    faqLoading: state.staticPage.faqLoading
  };
};

const FaqContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(FaqPage)
);

export default FaqContainer;
