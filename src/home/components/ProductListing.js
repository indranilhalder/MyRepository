import React, { Component } from "react";
import MDSpinner from "react-md-spinner";

class ProductListing extends Component {
  componentWillMount() {
    this.props.productSearch();
  }

  renderProduct() {
    if (this.props.product) {
      console.log(this.props.product.facetdata);
      console.log(this.props.product.searchresult);
      console.log(this.props.product.sorts);
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
