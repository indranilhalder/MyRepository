import { showModal, CONNECT_DETAILS } from "../../general/modal.actions";

import { connect } from "react-redux";
import ConnectKnowMore from "../components/ConnectKnowMore.js";

import { withRouter } from "react-router-dom";

const mapStateToProps = (state, ownProps) => {
  return {
    data: ownProps.feedComponentData
  };
};
const mapDispatchToProps = dispatch => {
  return {
    showConnectModal: data => {
      dispatch(showModal(CONNECT_DETAILS, data));
    }
  };
};

const ConnectKnowMoreContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ConnectKnowMore)
);

export default ConnectKnowMoreContainer;
