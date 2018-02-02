import React, { Component } from "react";
import MDSpinner from "react-md-spinner";

class ProductListingPage extends Component {
  componentWillMount() {
    this.props.getProducts();
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
    return <div />;
  }
}

export default ProductListingPage;
