import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { newReturnInitial, returnPinCode } from "../actions/account.actions";
import ReturnAddressList from "../components/ReturnAddressList.js";
import { addUserAddress } from "../../cart/actions/cart.actions.js";
import {
  SUCCESS,
  MY_ACCOUNT,
  MY_ACCOUNT_ORDERS_PAGE,
  RETURN_SUCCESS_MESSAGE
} from "../../lib/constants";
import { displayToast } from "../../general/toast.actions";
import {
  getPinCode,
  getPinCodeSuccess,
  getReturnRequest
} from "../../account/actions/account.actions.js";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    addUserAddress: async (addressDetails, fromAccount) => {
      const addAddressResponse = await dispatch(
        addUserAddress(addressDetails, fromAccount)
      );
      if (addAddressResponse.status === SUCCESS) {
        dispatch(displayToast(RETURN_SUCCESS_MESSAGE));
        dispatch(
          getReturnRequest(
            ownProps.returnProductDetails.orderProductWsDTO[0].sellerorderno,
            ownProps.returnProductDetails.orderProductWsDTO[0].transactionId
          )
        );
        if (ownProps.isCOD) {
          ownProps.history.go(-5);
        } else {
          ownProps.history.go(-4);
        }
      }
    },
    newReturnInitial: (returnDetails, product) => {
      dispatch(newReturnInitial(returnDetails, product));
    },
    returnPinCode: productDetails => {
      dispatch(returnPinCode(productDetails));
    },
    displayToast: message => {
      dispatch(displayToast(message));
    },
    getPinCode: pinCode => {
      dispatch(getPinCode(pinCode));
    },
    resetAutoPopulateDataForPinCode: () => {
      dispatch(getPinCodeSuccess(null));
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
    getPincodeStatus: state.profile.getPinCodeStatus,
    getPinCodeDetails: state.profile.getPinCodeDetails,
    orderDetails: state.profile.fetchOrderDetails,
    ...ownProps
  };
};

const ReturnCliqAndPiqContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnAddressList)
);
export default ReturnCliqAndPiqContainer;
