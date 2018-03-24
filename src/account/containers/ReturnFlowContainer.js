import { withRouter } from "react-router-dom";
import {
  getReturnRequest,
  returnProductDetails
} from "../actions/account.actions.js";
import { connect } from "react-redux";
import ReturnFlow from "../components/ReturnFlow";

const mapDispatchToProps = dispatch => {
  return {
    getReturnRequest: (orderCode, transactionId) => {
      dispatch(getReturnRequest(orderCode, transactionId));
    },

    returnProductDetailsFunc: () => {
      dispatch(returnProductDetails());
    }
  };
};

const mapStateToProps = state => {
  return {
    returnRequest: state.profile.returnRequest,
    returnProductDetails: state.profile.returnProductDetails
  };
};

const ReturnFlowContainer = connect(mapStateToProps, mapDispatchToProps)(
  ReturnFlow
);
export default ReturnFlowContainer;
