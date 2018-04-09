import React from "react";
import { connect } from "react-redux";
import { Route as ReactRouterRoute } from "react-router";
import { setDataLayer } from "../lib/adobeUtils";
import { withRouter } from "react-router-dom";
import { setIcid, clearIcid } from "./icid.actions";
import { parse } from "query-string";
const Route = props => {
  const search = parse(props.location.search);
  const icid = search.icid2;
  console.log("ICID");
  console.log(search);
  console.log(icid);
  if (icid) {
    props.setIcid(icid);
  } else {
    props.clearIcid();
  }
  return <ReactRouterRoute {...props} />;
};

const mapDispatchToProps = dispatch => {
  return {
    setIcid: icid => {
      dispatch(setIcid(icid));
    },
    clearIcid: () => {
      dispatch(clearIcid());
    }
  };
};

export default withRouter(connect(null, mapDispatchToProps)(Route));
