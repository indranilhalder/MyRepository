import { connect } from "react-redux";
import { cancelProduct, cancelOrder } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import CancelOrder from "../components/CancelOrder";
const mapDispatchToProps = dispatch => {
  return {
    cancelProduct: CancelProductDetails => {
      dispatch(cancelProduct(CancelProductDetails));
    },
    cancelOrder: CancelProductDetails => {
      dispatch(cancelOrder(CancelProductDetails));
    }
  };
};
const mapStateToProps = state => {
  return {
    cancelProductDetails: state.profile.cancelProductDetails
  };
};

const SaveListContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CancelOrder)
);

export default SaveListContainer;
