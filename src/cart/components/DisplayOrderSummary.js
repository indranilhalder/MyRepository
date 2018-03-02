import React from "react";
class DisplayOrderSummary extends React.Component {
  componentDidMount() {
    this.props.getOrderSummary();
  }

  render() {
    return <div />;
  }
}

export default DisplayOrderSummary;
