import React, { Component } from "react";
import MDSpinner from "react-md-spinner";

class ProductListingsPage extends Component {
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

export default ProductListingsPage;
