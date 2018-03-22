import { connect } from "react-redux";
import { getUserAddress } from "../../cart/actions/cart.actions";
import { getUserDetails } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import EditAccountDetails from "../components/EditAccountDetails.js";

const mapDispatchToProps = dispatch => {
  return {
    getUserDetails: addressId => {
      dispatch(getUserDetails(addressId));
    }
  };
};

const mapStateToProps = state => {
  return {
    userDetails: state.profile.userDetails
  };
};

const UpdateProfileContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(EditAccountDetails)
);

export default UpdateProfileContainer;
