import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import GiftCard from "../components/GiftCard.js";
import {
  getGiftCardDetails,
  createGiftCardDetails,
  getOtpToActivateWallet,
  verifyWallet
} from "../actions/account.actions";

const mapDispatchToProps = dispatch => {
  return {
    getGiftCardDetails: () => {
      dispatch(getGiftCardDetails());
    },

    createGiftCardDetails: giftCardDetails => {
      dispatch(createGiftCardDetails(giftCardDetails));
    },
    getOtpToActivateWallet: customerDetails => {
      dispatch(getOtpToActivateWallet(customerDetails));
    },
    verifyWallet: customerDetailsWithOtp => {
      dispatch(verifyWallet(customerDetailsWithOtp));
    }
  };
};

const mapStateToProps = state => {
  console.log(state.profile);
  return {
    giftCardsDetails: state.profile
  };
};

const GiftCardContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(GiftCard)
);

export default GiftCardContainer;
