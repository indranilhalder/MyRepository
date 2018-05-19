import { connect } from "react-redux";
import HollowHeader from "../components/HollowHeader.js";
import { setIsGoBackFromPDP } from "../../plp/actions/plp.actions.js";
const mapDispatchToProps = dispatch => {
  return {
    setIsGoBackFromPDP: () => {
      dispatch(setIsGoBackFromPDP());
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return ownProps;
};

const HollowHeaderContainer = connect(mapStateToProps, mapDispatchToProps)(
  HollowHeader
);

export default HollowHeaderContainer;
