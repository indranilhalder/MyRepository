import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { newReturnInitial, returnPinCode } from "../actions/account.actions";
import ReturnAddressList from "../components/ReturnAddressList.js";
import { addUserAddress } from "../../cart/actions/cart.actions.js";
import {
  SUCCESS,
  MY_ACCOUNT,
  MY_ACCOUNT_ORDERS_PAGE,
  RETURN_SUCCESS_MESSAGE,
  SUCCESS_CAMEL_CASE,
  SUCCESS_UPPERCASE
} from "../../lib/constants";
import { displayToast } from "../../general/toast.actions";
import {
  getPinCode,
  getPinCodeSuccess,
  getReturnRequest,
  getUserDetails,
  updateProfile,
  resetAddAddressDetails,
  clearPinCodeStatus
} from "../../account/actions/account.actions.js";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    addUserAddress: (addressDetails, fromAccount) => {
      if (addressDetails.emailId) {
        let userDetails = {};
        userDetails.emailId = addressDetails.emailId;
        dispatch(updateProfile(userDetails)).then(res => {
          if (res.status === SUCCESS || res.status === SUCCESS_CAMEL_CASE) {
            dispatch(addUserAddress(addressDetails, fromAccount)).then(
              addAddressResponse => {
                if (
                  res.status === SUCCESS ||
                  res.status === SUCCESS_CAMEL_CASE ||
                  res.status === SUCCESS_UPPERCASE
                ) {
                  dispatch(
                    getReturnRequest(
                      ownProps.returnProductDetails.orderProductWsDTO[0]
                        .sellerorderno,
                      ownProps.returnProductDetails.orderProductWsDTO[0]
                        .transactionId
                    )
                  );
                }
              }
            );
          }
        });
      } else {
        dispatch(addUserAddress(addressDetails, fromAccount)).then(
          addAddressResponse => {
            if (addAddressResponse.status === SUCCESS) {
              dispatch(
                getReturnRequest(
                  ownProps.returnProductDetails.orderProductWsDTO[0]
                    .sellerorderno,
                  ownProps.returnProductDetails.orderProductWsDTO[0]
                    .transactionId
                )
              );
            }
          }
        );
      }
    },
    newReturnInitial: (returnDetails, product) => {
      dispatch(newReturnInitial(returnDetails, product)).then(res => {
        if (res.status === SUCCESS) {
          dispatch(displayToast(RETURN_SUCCESS_MESSAGE));
          if (ownProps.isCOD) {
            ownProps.history.go(-6);
          } else {
            ownProps.history.go(-5);
          }
        }
      });
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
    },
    getUserDetails: () => {
      dispatch(getUserDetails());
    },
    resetAddAddressDetails: () => {
      dispatch(resetAddAddressDetails());
    },
    clearPinCodeStatus: () => {
      dispatch(clearPinCodeStatus());
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
    userDetails: state.profile.userDetails,
    addUserAddressStatus: state.profile.addUserAddressStatus,
    ...ownProps
  };
};

const ReturnCliqAndPiqContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnAddressList)
);
export default ReturnCliqAndPiqContainer;
