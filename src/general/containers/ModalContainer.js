import { connect } from "react-redux";
import ModalRoot from "../components/ModalRoot.js";
import * as modalActions from "../modal.actions.js";

const mapStateToProps = (state, ownProps) => {
  return {
    modalType: state.modals.modalType,
    ownProps: state.modals.ownProps,
    showModal: state.modals.showModal
  };
};

const mapDispatchToProps = dispatch => {
  return {
    showModal: (type, ownProps = null) => {
      dispatch(modalActions.showModal(type, ownProps));
    },
    hideModal: () => {
      dispatch(modalActions.hideModal());
    }
  };
};
const ModalContainer = connect(mapStateToProps, mapDispatchToProps)(ModalRoot);

export default ModalContainer;
