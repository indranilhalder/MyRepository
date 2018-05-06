import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import ReturnToStore from "../components/ReturnToStore";
import { quickDropStore, newReturnInitial } from "../actions/account.actions";
const mapDispatchToProps = dispatch => {
  return {
    quickDropStore: (pincode, ussId) => {
      dispatch(quickDropStore(pincode, ussId));
    },
    newReturnInitial: (productObjToBeReturn, product) => {
      dispatch(newReturnInitial(productObjToBeReturn, product));
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
