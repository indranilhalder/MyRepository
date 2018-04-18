import { connect } from "react-redux";
import {
  getCliqCashDetails,
  redeemCliqVoucher
} from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import CliqAndCash from "../components/CliqAndCash.js";
import { setHeaderText } from "../../general/header.actions";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
const mapDispatchToProps = dispatch => {
  return {
    getCliqCashDetails: () => {
      dispatch(getCliqCashDetails());
    },
    redeemCliqVoucher: cliqCashDetails => {
      dispatch(redeemCliqVoucher(cliqCashDetails)).then(
        dispatch(getCliqCashDetails())
      );
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    showSecondaryLoader: () => {
      dispatch(showSecondaryLoader());
    },
    hideSecondaryLoader: () => {
      dispatch(hideSecondaryLoader());
    }
  };
};

const mapStateToProps = state => {
  return {
    cliqCashUserDetails: state.profile.cliqCashUserDetails,
    cliqCashVoucherDetailsStatus: state.profile.cliqCashVoucherDetailsStatus,
    cliqCashVoucherDetails: state.profile.cliqCashVoucherDetails,
    cliqCashVoucherDetailsError: state.profile.cliqCashVoucherDetailsError,
    loading: state.profile.loading
  };
};

const CliqCashContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CliqAndCash)
);

export default CliqCashContainer;
