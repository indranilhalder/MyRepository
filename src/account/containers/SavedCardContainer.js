import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import UserSavedCard from "../components/UserSavedCard.js";
import {
  getSavedCardDetails,
  removeSavedCardDetails
} from "../actions/account.actions";

const mapDispatchToProps = dispatch => {
  return {
    getSavedCardDetails: (userId, customerAccessToken) => {
      dispatch(getSavedCardDetails(userId, customerAccessToken));
    },

    removeSavedCardDetails: () => {
      dispatch(removeSavedCardDetails());
    }
  };
};

const mapStateToProps = state => {
  return {
    account: state.account
  };
};

const SavedCardContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(UserSavedCard)
);

export default SavedCardContainer;
