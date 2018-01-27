import { showModal, CONNECT_DETAILS } from "../../general/modal.actions";

import { connect } from "react-redux";
import ConnectKnowMore from "../components/ConnectKnowMore.js";

import { withRouter } from "react-router-dom";
const mapDispatchToProps = dispatch => {
  return {
    showConnectModal: () => {
      dispatch(showModal(CONNECT_DETAILS));
    }
  };
};

const ConnectKnowMoreContainer = withRouter(
  connect(null, mapDispatchToProps)(ConnectKnowMore)
);

export default ConnectKnowMoreContainer;
