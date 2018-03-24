import { connect } from "react-redux";
import ReturnReasonAndModes from "../components/ReturnReasonAndModes";
const mapDispatchToProps = dispatch => {
  return {
    addReason: reason => {
      // dispatch(setReason(reason))
    }
  };
};
const mapStateToProps = state => {
  return {
    returnRequest: state.profile.returnRequest,
    returnProductDetails: state.profile.returnProductDetails
  };
};
const ReturnReasonAndModesContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ReturnReasonAndModes);
export default ReturnReasonAndModesContainer;
