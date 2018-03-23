import React from "react";
import PropTypes from "prop-types";

export default class ReturnReasonAndModeOfReturn extends React.Component {
  componentDidMount() {
    this.props.returnProductDetails();
    this.props.getReturnRequest();
  }
  render() {
    console.log(this.props);
    return <div />;
  }
}
