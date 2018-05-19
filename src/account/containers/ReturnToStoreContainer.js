import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import ReturnToStore from "../components/ReturnToStore";
import { quickDropStore, newReturnInitial } from "../actions/account.actions";
import { displayToast } from "../../general/toast.actions.js";
import {
  SUCCESS,
  MY_ACCOUNT,
  MY_ACCOUNT_ORDERS_PAGE,
  ERROR,
  FAILURE
} from "../../lib/constants.js";
const RETURN_SUCCESS_MESSAGE = "Return has been initiated";

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    quickDropStore: (pincode, ussId) => {
      dispatch(quickDropStore(pincode, ussId)).then(result => {
        if (result.status === FAILURE) {
          dispatch(displayToast(result.error));
        }
      });
    },
    newReturnInitial: (productObjToBeReturn, product) => {
      dispatch(newReturnInitial(productObjToBeReturn, product)).then(res => {
        if (res.status === SUCCESS) {
          dispatch(displayToast(RETURN_SUCCESS_MESSAGE));
          if (ownProps.isCOD) {
            ownProps.history.go(-5);
          } else {
            ownProps.history.go(-4);
          }
        }
      });
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    ...ownProps,
    orderDetails: state.profile.fetchOrderDetails
  };
};

const ReturnToStoreContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnToStore)
);
export default ReturnToStoreContainer;
