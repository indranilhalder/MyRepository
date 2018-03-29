import { connect } from "react-redux";
import {
  cancelProductDetails,
  cancelProduct
} from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import CancelOrder from "../components/CancelOrder";
const mapDispatchToProps = dispatch => {
  return {
    cancelProductDetails: CancelProductDetails => {
      dispatch(cancelProductDetails(CancelProductDetails));
    },
    cancelProduct: CancelProductDetails => {
      dispatch(cancelProduct(CancelProductDetails));
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
