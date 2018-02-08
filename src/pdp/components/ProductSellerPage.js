import React, { Component } from "react";
import styles from "./ProductSellerPage.css";
import ProductDetailsCard from "./ProductDetailsCard";
import SellerWithMultiSelect from "./SellerWithMultiSelect";
import SellerCard from "./SellerCard";
import map from "lodash/map";
import extend from "lodash/map";
let filterData;
class ProductSellerPage extends Component {
  constructor(props) {
    super(props);
    filterData = map(this.props.filterData, subData => {
      return extend({}, subData, { selected: false });
    });
    this.state = {
      pageNumber: 0,
      selected: filterData.map(val => {
        return !val.selected;
      })
    };
  }

  handleSelect(val, index) {
    let selected = this.state.selected;
    selected[index] = val;
    this.setState({ selected });
  }

  handleApply(values) {
    this.props.onApply(
      values,
      this.props.feedComponentData.data.questionId,
      this.props.positionInFeed
    );
  }

  render() {
    return (
      <div className={styles.base}>
        <ProductDetailsCard
          productImage={this.props.productDetails.galleryImagesList}
          productName={this.props.productDetails.productName}
          productMaterial={
            this.props.productDetails.classificationList[0].value
              .classificationListJwlry[5].value.classificationListValueJwlry[0]
          }
          price={this.props.productDetails.mrpPrice.formattedValue}
          discountPrice={
            this.props.productDetails.discountedPrice.formattedValue
          }
          averageRating={this.props.productDetails.averageRating}
          totalNoOfReviews={this.props.productDetails.productReviewsCount}
        />
        <div>
          <SellerWithMultiSelect
            onSelect={val => {
              this.handleSelect(val, this.state.pageNumber);
            }}
          >
            {this.props.productDetails.otherChildProducts &&
              this.props.productDetails.otherChildProducts.map(
                (value, index) => {
                  return (
                    <SellerCard
                      heading={value[0].productListingId}
                      value={index}
                    />
                  );
                }
              )}
          </SellerWithMultiSelect>
        </div>
      </div>
    );
  }
}

export default ProductSellerPage;
