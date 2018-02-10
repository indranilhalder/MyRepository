import React, { Component } from "react";
import styles from "./ProductSellerPage.css";
import ProductDetailsCard from "./ProductDetailsCard";
import SellerWithMultiSelect from "./SellerWithMultiSelect";
import SellerCard from "./SellerCard";
import {
  MOBILE_PDP_VIEW,
  PRICE_TEXT,
  OFFER_AVAILABLE,
  DELIVERY_INFORMATION_TEXT,
  DELIVERY_RATES,
  CASH_TEXT
} from "../../lib/constants";

class ProductSellerPage extends Component {
  render() {
    return (
      <div className={styles.base}>
        <ProductDetailsCard
          productImage={
            this.props.productDetails.galleryImagesList.filter(val => {
              return val.imageType === MOBILE_PDP_VIEW;
            })[0].galleryImages[0].value
          }
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
          {this.props.productDetails.otherSellers && (
            <SellerWithMultiSelect>
              {this.props.productDetails.otherSellers.map((value, index) => {
                return (
                  <SellerCard
                    heading={value.sellerName}
                    priceTitle={PRICE_TEXT}
                    discountPrice={value.sellerMOP}
                    price={value.sellerMRP}
                    offerText={OFFER_AVAILABLE}
                    deliveryText={DELIVERY_INFORMATION_TEXT}
                    shippingText={value.deliveryModesATP[0].value}
                    cashText={CASH_TEXT}
                    policyText={DELIVERY_RATES}
                    key={index}
                    value={value}
                  />
                );
              })}
            </SellerWithMultiSelect>
          )}
        </div>
      </div>
    );
  }
}

export default ProductSellerPage;
