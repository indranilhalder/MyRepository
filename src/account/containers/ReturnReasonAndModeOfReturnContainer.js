import { withRouter } from "react-router-dom";
import {
  getReturnRequest,
  returnProductDetails
} from "../actions/account.actions.js";
import { connect } from "react-redux";
import ReturnReasonAndModeOfReturn from "../components/ReturnReasonAndModeOfReturn";

const mapDispatchToProps = dispatch => {
  return {
    getReturnRequest: (orderCode, transactionId) => {
      dispatch(getReturnRequest(orderCode, transactionId));
    },

    returnProductDetails: () => {
      dispatch(returnProductDetails());
    }
  };
};

const mapStateToProps = state => {
  return {
    returnProductDetails: state.account
  };
};

const ReturnReasonAndModeOfReturnContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnReasonAndModeOfReturn)
);
export default ReturnReasonAndModeOfReturnContainer;
