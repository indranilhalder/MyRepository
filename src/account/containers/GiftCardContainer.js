import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import GiftCard from "../components/GiftCard.js";
import {
  getGiftCardDetails,
  createGiftCardDetails
} from "../actions/account.actions";
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
    giftCardsDetails: state.profile.giftCards,
    giftCardDetailsStatus: state.profile.giftCardDetailsStatus,
    giftCardDetails: state.profile.giftCardDetails
  };
};

const GiftCardContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(GiftCard)
);

export default GiftCardContainer;
