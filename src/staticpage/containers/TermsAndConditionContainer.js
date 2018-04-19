import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import TermsAndConditionPayment from "../components/TermsAndConditionPayment.js";
import { getTermsAndConditionDetails } from "../actions/staticPage.actions";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = dispatch => {
  return {
    getTermsAndConditionDetails: () => {
      dispatch(getTermsAndConditionDetails());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};

const mapStateToProps = state => {
  return {
    termsAndCondition: state.staticPage.termsAndCondition,
    termsAndConditionStatus: state.staticPage.termsAndConditionStatus,
    termsAndConditionLoading: state.staticPage.termsAndConditionLoading
  };
};

const TermsAndConditionContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(TermsAndConditionPayment)
);

export default TermsAndConditionContainer;
