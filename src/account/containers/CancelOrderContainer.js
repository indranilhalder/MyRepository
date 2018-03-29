import { connect } from "react-redux";
import {
  cancelProductDetails,
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
    cancelProductDetails: CancelProductDetails => {
      dispatch(cancelProductDetails(CancelProductDetails));
    },
    cancelProduct: CancelProductDetails => {
      try {
        dispatch(cancelProduct(CancelProductDetails)).then(response => {
          if (response.status === SUCCESS) {
            ownProps.history.push(`${MY_ACCOUNT}${MY_ACCOUNT_ORDERS_PAGE}`);
          } else {
            throw new Error("Failed");
          }
        });
      } catch (e) {
        // showToast Here
        console.log(e.message);
      }
    }
  };
};
const mapStateToProps = state => {
  return {
    cancelProductDetailsObj: state.profile.cancelProductDetails
  };
};

const CancelOrderContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CancelOrder)
);

export default CancelOrderContainer;
