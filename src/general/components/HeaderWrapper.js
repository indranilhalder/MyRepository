import React from "react";
import InformationHeader from "./InformationHeader.js";
import HollowHeader from "./HollowHeader.js";
import { withRouter } from "react-router-dom";

const PRODUCT_CODE_REGEX = /p-(.*)/;
class HeaderWrapper extends React.Component {
  render() {
    const url = this.props.location.pathname;
    let productCode = null;
    if (PRODUCT_CODE_REGEX.test(url)) {
      productCode = PRODUCT_CODE_REGEX.exec(url);
    }

    console.log("HISTORY");
    console.log(this.props.history);
    return (
      <React.Fragment>
        {" "}
        {productCode ? <HollowHeader /> : <InformationHeader />}{" "}
      </React.Fragment>
    );
  }
}

export default withRouter(HeaderWrapper);
