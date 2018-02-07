import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
import Plp from "./Plp";
class ProductListingsPage extends Component {
  componentWillMount() {
    this.props.getProductListings();
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
    } else if (this.props.productListings !== null) {
      return (
        <Plp
          searchresult={this.props.productListings.searchresult}
          facetData={this.props.productListings.facetdata}
          showSort={this.props.showSort}
          onApply={this.props.onApply}
        />
      );
    } else {
      return this.renderLoader();
    }
  }
}

export default ProductListingsPage;
