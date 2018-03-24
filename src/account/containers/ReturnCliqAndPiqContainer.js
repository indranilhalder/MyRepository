import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import {
  getReturnRequest,
  newReturnInitiateForCliqAndPiq,
  returnPinCode,
  returnProductDetails
} from "../actions/account.actions";
import ReturnAddressList from "../components/ReturnAddressList.js";
import { addUserAddress } from "../../cart/actions/cart.actions.js";

const mapDispatchToProps = dispatch => {
  return {
    getReturnRequest: (orderCode, transactionId) => {
      dispatch(getReturnRequest(orderCode, transactionId));
    },
    addUserAddress: (addressDetails, fromAccount) => {
      dispatch(addUserAddress(addressDetails, fromAccount));
    },
    newReturnInitiateForCliqAndPiq: returnDetails => {
      dispatch(newReturnInitiateForCliqAndPiq(returnDetails));
    },
    returnPinCode: productDetails => {
      dispatch(returnPinCode(productDetails));
    },
    returnProductDetails: () => {
      dispatch(returnProductDetails());
    }
  };
};

const mapStateToProps = state => {
  return {
    returnRequest: state.profile.returnRequest,
    returnPinCodeStatus: state.profile.returnPinCodeStatus,
    returnProducts: state.profile.returnProducts,
    AddUserAddressStatus: state.cart.AddUserAddressStatus
  };
};

const ReturnCliqAndPiqContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnAddressList)
);
export default ReturnCliqAndPiqContainer;
