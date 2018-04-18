import React, { Component } from "react";
import SecondaryLoader from "../../general/components/SecondaryLoader.js";

export default class StaticPage extends Component {
  render() {
    console.log("DATA");
    if (this.props.loading) {
      return <SecondaryLoader />;
    } else {
      return <div> Static Page Null state </div>;
    }
  }
}
