import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import GiftCard from "../components/GiftCard.js";
import {
  getGiftCardDetails,
  createGiftCardDetails,
  getOtpToActivateWallet,
  verifyWallet
} from "../actions/account.actions";
import {
  showModal,
  ADDRESS,
  PRODUCT_COUPONS,
  SIZE_GUIDE,
  EMI_MODAL
} from "../../general/modal.actions";
const mapDispatchToProps = dispatch => {
  return {
    getGiftCardDetails: () => {
      dispatch(getGiftCardDetails());
    },
    createGiftCardDetails: giftCardDetails => {
      dispatch(createGiftCardDetails(giftCardDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    giftCardsDetails: state.profile
  };
};

const GiftCardContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(GiftCard)
);

export default GiftCardContainer;
