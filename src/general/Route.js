import React from "react";
import { Route as ReactRouterRoute } from "react-router";
import { setDataLayer } from "../lib/adobeUtils";
import { withRouter } from "react-router-dom";

const Route = props => {
  // set my data here.
  setDataLayer(props);
  return <ReactRouterRoute {...props} />;
};

export default withRouter(Route);
