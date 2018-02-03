import React, { Component } from "react";
import MDSpinner from "react-md-spinner";

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
    }
    return (
      <div
        style={{ width: 200, height: 200, background: "#666" }}
        onClick={() => {
          this.props.showSort();
        }}
      />
    );
  }
}

export default ProductListingsPage;
