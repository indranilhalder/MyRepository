import { withRouter } from "react-router-dom";
import { newReturnInitial } from "../actions/account.actions.js";
import { connect } from "react-redux";
import SelfCourier from "../components/SelfCourier";

const mapDispatchToProps = dispatch => {
  return {
    newReturnInitial: returnDetails => {
      dispatch(newReturnInitial(returnDetails));
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
