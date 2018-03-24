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

    returnProductDetails: () => {
      dispatch(returnProductDetails());
    }
  };
};

const mapStateToProps = state => {
  return {
    returnProductDetails: state.profile
  };
};

const ReturnFlowContainer = connect(mapStateToProps, mapDispatchToProps)(
  ReturnFlow
);
export default ReturnFlowContainer;
