import { connect } from "react-redux";
import {
  getCliqCashDetails,
  redeemCliqVoucher
} from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import CliqAndCash from "../components/CliqAndCash.js";

const mapDispatchToProps = dispatch => {
  return {
    getCliqCashDetails: () => {
      dispatch(getCliqCashDetails());
    },
    redeemCliqVoucher: (cliqCahDetails) => {
      dispatch(redeemCliqVoucher(cliqCahDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    userAddress: state.profile.userAddress,
    removeAddressStatus: state.profile.removeAddressStatus
  };
};

const CliqCashContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CliqAndCash)
);

export default CliqCashContainer;
