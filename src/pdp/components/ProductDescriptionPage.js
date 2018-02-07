import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
import styles from "./ProductDescriptionPage.css";
import { PRODUCT_REVIEW_ROUTER } from "../../lib/constants";
class ProductDescriptionPage extends Component {
  componentWillMount() {
    this.props.getProductDescription();
  }

  renderLoader() {
    return (
      <div className={styles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  }

  goToReviewPage = () => {
    this.props.history.push(PRODUCT_REVIEW_ROUTER);
  };

  render() {
    if (this.props.productDetails) {
      return <div onClick={this.goToReviewPage}>Go to Review</div>;
    } else {
      return this.renderLoader();
    }
  }
}

export default ProductDescriptionPage;
