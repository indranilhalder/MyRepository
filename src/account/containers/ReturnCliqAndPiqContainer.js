import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { newReturnInitial, returnPinCode } from "../actions/account.actions";
import ReturnAddressList from "../components/ReturnAddressList.js";
import { addUserAddress } from "../../cart/actions/cart.actions.js";

const mapDispatchToProps = dispatch => {
  return {
    addUserAddress: (addressDetails, fromAccount) => {
      dispatch(addUserAddress(addressDetails, fromAccount));
    },
    newReturnInitial: returnDetails => {
      dispatch(newReturnInitial(returnDetails));
    },
    returnPinCode: productDetails => {
      dispatch(returnPinCode(productDetails));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    returnRequest: state.profile.returnRequest,
    returnPinCodeStatus: state.profile.returnPinCodeStatus,
    returnProducts: state.profile.returnProductDetails,
    AddUserAddressStatus: state.cart.AddUserAddressStatus,
    ...ownProps
  };
};

const ReturnCliqAndPiqContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnAddressList)
);
export default ReturnCliqAndPiqContainer;
