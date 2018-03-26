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
  console.log(state.profile);
  return {
    giftCards: state.profile.giftCards
  };
};

const GiftCardContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(GiftCard)
);

export default GiftCardContainer;
