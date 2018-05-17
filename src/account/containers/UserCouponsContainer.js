import { getUserCoupons } from "../actions/account.actions";
import { connect } from "react-redux";
import UserCoupons from "../components/UserCoupons";

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getUserCoupons: () => {
      dispatch(getUserCoupons());
    },
    displayToast: message => {
      ownProps.displayToast(message);
    }
  };
};

const mapStateToProps = state => {
  return {
    userCoupons: state.profile.userCoupons
  };
};

const UserCouponsContainer = connect(mapStateToProps, mapDispatchToProps)(
  UserCoupons
);

export default UserCouponsContainer;
