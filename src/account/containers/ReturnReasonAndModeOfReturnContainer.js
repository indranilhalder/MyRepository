import { withRouter } from "react-router-dom";
import {
  getReturnRequest,
  returnProductDetails
} from "../actions/account.actions.js";
import { connect } from "react-redux";

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
    returnProductDetails: state.account.returnProductDetails,
    returnRequest: state.account.returnRequest
  };
};

const ReturnReasonAndModeOfReturnContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)
);
export default ReturnReasonAndModeOfReturnContainer;
