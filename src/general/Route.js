import React from "react";
import { connect } from "react-redux";
import { Route as ReactRouterRoute } from "react-router";
import { setDataLayer } from "../lib/adobeUtils";
import { withRouter } from "react-router-dom";

const Route = props => {
  // set my data here.
  setDataLayer(props);
  return <ReactRouterRoute {...props} />;
};
const mapStateToProps = (state, ownProps) => {
  return {
    state,
    ownProps
  };
};

export default withRouter(connect(mapStateToProps)(Route));
