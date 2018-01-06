import { connect } from "react-redux";
import { showModal } from "../modal.actions.js";
import App from "../../App.js";

const mapDispatchToProps = dispatch => {
  return {
    showModal: type => {
      dispatch(showModal(type));
    }
  };
};

const mapStateToProps = state => {
  return {
    modalStatus: state.modal.modalDisplayed
  };
};

const AppContainer = connect(mapStateToProps, mapDispatchToProps)(App);

export default AppContainer;
