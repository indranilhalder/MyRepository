import React from "react";
import { connect } from "react-redux";
import { Route as ReactRouterRoute } from "react-router";
import { setDataLayer } from "../lib/adobeUtils";
import { withRouter } from "react-router-dom";
import { setIcid, clearIcid } from "./icid.actions";
import { parse } from "query-string";
import { ICID2, CID } from "../lib/adobeUtils";
const Route = props => {
  const search = parse(props.location.search);
  let icid, icidType;
  if (search.icid2) {
    icid = search.icid2;
    icidType = ICID2;
  } else if (search.cid) {
    icid = search.cid;
    icidType = CID;
  }

  if (icid) {
    props.setIcid(icid, icidType);
  } else {
    props.clearIcid();
  }
  return <ReactRouterRoute {...props} />;
};

const mapDispatchToProps = dispatch => {
  return {
    setIcid: (icid, icidType) => {
      dispatch(setIcid(icid, icidType));
    },
    clearIcid: () => {
      dispatch(clearIcid());
    }
  };
};

export default withRouter(connect(null, mapDispatchToProps)(Route));
