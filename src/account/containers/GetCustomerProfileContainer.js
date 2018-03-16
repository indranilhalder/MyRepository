import { connect } from "react-redux";
import {
  getCustomerProfileDetails,
  getCustomerAddressDetails
} from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import AccountSetting from "../components/AccountSetting";

const mapDispatchToProps = dispatch => {
  return {
    getCustomerProfileDetails: () => {
      dispatch(getCustomerProfileDetails());
    },
    getCustomerAddressDetails: () => {
      dispatch(getCustomerAddressDetails());
    }
  };
};

const mapStateToProps = state => {
  return {
    customerProfileDetails: state.account.customerProfileDetails,
    customerAddressDetails: state.account.customerAddressDetails
  };
};

const GetCustomerProfileContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AccountSetting)
);

export default GetCustomerProfileContainer;
