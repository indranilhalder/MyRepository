import { connect } from "react-redux";
import {
  getDetailsOfCancelledProduct,
  cancelProduct
} from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import CancelOrder from "../components/CancelOrder";
import {
  SUCCESS,
  MY_ACCOUNT,
  MY_ACCOUNT_ORDERS_PAGE
} from "../../lib/constants";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getDetailsOfCancelledProduct: cancelProductDetails => {
      dispatch(getDetailsOfCancelledProduct(cancelProductDetails));
    },
    cancelProduct: async CancelProductDetails => {
      const cancelOrderDetails = await dispatch(
        cancelProduct(CancelProductDetails)
      );
      if (cancelOrderDetails.status === SUCCESS) {
        ownProps.history.push(`${MY_ACCOUNT}${MY_ACCOUNT_ORDERS_PAGE}`);
      } else {
        // show toast
      }
    }
  };
};
const mapStateToProps = state => {
  return {
    cancelProductDetails: state.profile.cancelProductDetails,
    loadingForCancelProductDetails: state.profile.loadingForCancelProductDetails
  };
};

const CancelOrderContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CancelOrder)
);

export default CancelOrderContainer;
