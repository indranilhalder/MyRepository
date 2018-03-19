import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import UserSavedCard from "../components/UserSavedCard.js";
import { getSavedCartDetails } from "../actions/account.actions";

const mapDispatchToProps = dispatch => {
  return {
    getSavedCartDetails: (userId, customerAccessToken) => {
      dispatch(getSavedCartDetails(userId, customerAccessToken));
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
