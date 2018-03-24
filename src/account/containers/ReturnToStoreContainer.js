import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import ReturnToStore from "../components/ReturnToStore";
import {
  quickDropStore,
  returnInitialForQuickDrop
} from "../actions/account.actions";
const mapDispatchToProps = dispatch => {
  return {
    quickDropStore: (pincode, ussId) => {
      dispatch(quickDropStore(pincode, ussId));
    },
    returnInitialForQuickDrop: productObj => {
      dispatch(returnInitialForQuickDrop(productObj));
    }
  };
};

const mapStateToProps = state => {
  return {
    returnRequest: state.profile.returnRequest,
    returnProductDetails: state.profile.returnProductDetails
  };
};

const ReturnToStoreContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnToStore)
);
export default ReturnToStoreContainer;
