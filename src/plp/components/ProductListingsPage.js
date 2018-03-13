import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
import Plp from "./Plp";
class ProductListingsPage extends Component {
  renderLoader() {
    return (
      <div>
        <MDSpinner />
      </div>
    );
  }
  render() {
    console.log("PRODUCT LISTINGS PAGE RENDER");
    console.log(this.props.loading);
    console.log(this.props.pageNumber);
    console.log(this.props.isFilter);
    if (
      this.props.loading &&
      this.props.pageNumber === 0 &&
      this.props.isFilter === false
    ) {
      return this.renderLoader();
    } else if (this.props.productListings !== null) {
      console.log("PRODUCT LISTINGS PATH");
      return (
        <Plp
          history={this.props.history}
          searchresult={this.props.productListings.searchresult}
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
