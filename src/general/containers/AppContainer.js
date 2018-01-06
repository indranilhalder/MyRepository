import { connect } from "react-redux";
import { showModal } from "../modal.actions.js";
import { withRouter } from "react-router-dom";
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

const AppContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(App)
);

export default AppContainer;
