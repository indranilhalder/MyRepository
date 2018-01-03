import { connect } from "react-redux";
import { showModal } from "../actions/modal.actions.js";
import App from "../App.js";

const mapDispatchToProps = dispatch => {
  return {
    showModal: type => {
      dispatch(showModal(type));
    }
  };
};

const mapStateToProps = state => {
  return {
    modalStatus: state.modals.modalDisplayed
  };
};

const AppContainer = connect(mapStateToProps, mapDispatchToProps)(App);

export default AppContainer;
