import { withRouter } from "react-router-dom";
import { newReturnInitial } from "../actions/account.actions.js";
import { connect } from "react-redux";
import SelfCourier from "../components/SelfCourier";
import {
  SUCCESS,
  MY_ACCOUNT,
  MY_ACCOUNT_ORDERS_PAGE
} from "../../lib/constants.js";
import { displayToast } from "../../general/toast.actions.js";
const RETURN_SUCCESS_MESSAGE = "Return has been initiated";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    newReturnInitial: async returnDetails => {
      const returnInitiate = await dispatch(newReturnInitial(returnDetails));
      if (returnInitiate.status === SUCCESS) {
        dispatch(displayToast(RETURN_SUCCESS_MESSAGE));
        ownProps.history.push(`${MY_ACCOUNT}${MY_ACCOUNT_ORDERS_PAGE}`);
      }
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    returnRequest: state.profile.returnRequest,
    returnProductDetails: state.profile.returnProductDetails,
    ...ownProps
  };
};

const SelfCourierContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SelfCourier)
);
export default SelfCourierContainer;
