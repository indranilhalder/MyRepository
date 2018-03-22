import ReturnReasonForm from "../components/ReturnReasonForm.js";
import { withRouter } from "react-router-dom";
import {
  getReturnRequest,
  returnProductDetails
} from "../actions/account.actions.js";
const mapDispatchToProps = dispatch => {
  return {
    getReturnRequest: (orderCode, transactionId) => {
      dispatch(getReturnRequest(orderCode, transactionId));
    }
  };
};
