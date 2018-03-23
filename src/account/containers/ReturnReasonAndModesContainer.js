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
  console.log(state);
  return {
    returnRequest: state.profile.returnRequest
  };
};
const ReturnReasonAndModesContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ReturnReasonAndModes);
export default ReturnReasonAndModesContainer;
