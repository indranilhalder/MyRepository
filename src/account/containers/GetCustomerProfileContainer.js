import { connect } from "react-redux";
import { getCustomerProfileDetails } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import AccountSetting from "../components/AccountSetting";

const mapDispatchToProps = dispatch => {
  return {
    getCustomerProfileDetails: () => {
      dispatch(getCustomerProfileDetails());
    }
  };
};

const mapStateToProps = state => {
  return {
    customerProfileDetails: state.account.customerProfileDetails
  };
};

const GetCustomerProfileContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AccountSetting)
);

export default GetCustomerProfileContainer;
