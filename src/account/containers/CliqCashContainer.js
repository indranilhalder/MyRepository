import { connect } from "react-redux";
import {
  getCliqCashDetails,
  redeemCliqVoucher
} from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import CliqAndCash from "../components/CliqAndCash.js";
import { setHeaderText, SUCCESS } from "../../general/header.actions";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
import { SUCCESS_CAMEL_CASE, SUCCESS_UPPERCASE } from "../../lib/constants";
import { displayToast } from "../../general/toast.actions";
const CLIQ_CASH_REDEEM_SUCCESS =
  "Congrats!  Money has been added to your Cliq Cash balance";
const mapDispatchToProps = dispatch => {
  return {
    getCliqCashDetails: () => {
      dispatch(getCliqCashDetails());
    },
    redeemCliqVoucher: cliqCashDetails => {
      dispatch(redeemCliqVoucher(cliqCashDetails)).then(result => {
        console.log(result.status);
        if (
          result.status === "success" ||
          result.status === SUCCESS_CAMEL_CASE ||
          result.status === SUCCESS_UPPERCASE
        ) {
          dispatch(displayToast(CLIQ_CASH_REDEEM_SUCCESS));
          dispatch(getCliqCashDetails());
        } else {
          dispatch(displayToast(result.error));
        }
      });
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
