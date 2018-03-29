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
    redeemCliqVoucher: cliqCashDetails => {
      dispatch(redeemCliqVoucher(cliqCashDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    cliqCashUserDetails: state.profile.cliqCashUserDetails,
    cliqCashVoucherDetailsStatus: state.profile.cliqCashVoucherDetailsStatus,
    cliqCashVoucherDetails: state.profile.cliqCashVoucherDetails,
    cliqCashVoucherDetailsError: state.profile.cliqCashVoucherDetailsError
  };
};

const CliqCashContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CliqAndCash)
);

export default CliqCashContainer;
