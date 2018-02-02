import React, { Component } from "react";
import MDSpinner from "react-md-spinner";

class ProductListing extends Component {
  componentWillMount() {
    this.props.productListing();
  }

  renderProduct() {
    if (this.props.product) {
    }
  }

  renderLoader() {
    return (
      <div>
        <MDSpinner />
      </div>
    );
  }

  render() {
    if (this.props.loading) {
      return this.renderLoader();
    }
    return <div>{this.renderProduct()}</div>;
  }
}

export default ProductListing;
