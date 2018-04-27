import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { newReturnInitial, returnPinCode } from "../actions/account.actions";
import ReturnAddressList from "../components/ReturnAddressList.js";
import { addUserAddress } from "../../cart/actions/cart.actions.js";
import { SUCCESS } from "../../lib/constants";

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    addUserAddress: async (addressDetails, fromAccount) => {
      const addAddressResponse = await dispatch(
        addUserAddress(addressDetails, fromAccount)
      );
      if (addAddressResponse.status === SUCCESS) {
        ownProps.history.goBack();
      }
    },
    newReturnInitial: (returnDetails, product) => {
      dispatch(newReturnInitial(returnDetails, product));
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
    returnInitiateStatus: state.profile.returnInitiateStatus,
    returnInitiateError: state.profile.returnInitiateError,
    returnPinCodeError: state.profile.returnPinCodeError,
    orderDetails: state.profile.fetchOrderDetails,
    ...ownProps
  };
};

const ReturnCliqAndPiqContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnAddressList)
);
export default ReturnCliqAndPiqContainer;
