import { withRouter } from "react-router-dom";
import {
  getReturnRequest,
  returnProductDetails
} from "../actions/account.actions.js";
import { connect } from "react-redux";
import ReturnFlow from "../components/ReturnFlow";
import { displayToast } from "../../general/toast.actions.js";
const mapDispatchToProps = dispatch => {
  return {
    getReturnRequest: (orderCode, transactionId) => {
      dispatch(getReturnRequest(orderCode, transactionId));
    },
    displayToast: message => {
      dispatch(displayToast(message));
    },
    returnProductDetailsFunc: productDetails => {
      dispatch(returnProductDetails(productDetails));
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
