import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
class ProductDescriptionPage extends Component {
  componentWillMount() {
    this.props.getProductDescription();
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
    } else {
      return <div />;
    }
  }
}

export default ProductDescriptionPage;
