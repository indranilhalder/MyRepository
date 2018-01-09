import { connect } from "react-redux";
import ModalRoot from "../components/ModalRoot.js";
import * as modalActions from "../modal.actions.js";
import {
  resetPassword,
  otpVerification,
  forgotPassword,
  forgotPasswordOtpVerification
} from "../../auth/actions/user.actions";
const mapStateToProps = (state, ownProps) => {
  return {
    modalType: state.modal.modalType,
    ownProps: state.modal.ownProps,
    modalStatus: state.modal.modalDisplayed
  };
};

const mapDispatchToProps = dispatch => {
  return {
    showModal: (type, ownProps = null) => {
      dispatch(modalActions.showModal(type, ownProps));
    },
    hideModal: () => {
      dispatch(modalActions.hideModal());
    },
    otpVerification: userDetails => {
      dispatch(otpVerification(userDetails));
    },
    resetPassword: userDetails => {
      dispatch(resetPassword(userDetails));
    },
    forgotPassword: userDetails => {
      dispatch(forgotPassword(userDetails));
    },
    forgotPasswordOtpVerification: userDetails => {
      dispatch(forgotPasswordOtpVerification(userDetails));
    }
  };
};
const ModalContainer = connect(mapStateToProps, mapDispatchToProps)(ModalRoot);

export default ModalContainer;
