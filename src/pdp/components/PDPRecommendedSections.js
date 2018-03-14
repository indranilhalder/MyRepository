import React from "react";
import Carousel from "../../general/components/Carousel.js";
import ProductModule from "../../general/components/ProductModule.js";
import { transformData } from "../../home/components/utils.js";
import Logo from "../../general/components/Logo.js";
import Button from "../../general/components/Button.js";
import { withRouter } from "react-router-dom";
import {
  ABOUT_THE_BRAND_WIDGET_KEY,
  RECOMMENDED_PRODUCTS_WIDGET_KEY,
  SIMILAR_PRODUCTS_WIDGET_KEY
} from "../actions/pdp.actions.js";
import FollowUnFollowButtonContainer from "../containers/FollowUnFollowButtonContainer";
import styles from "./PDPRecommendedSections.css";

class PDPRecommendedSections extends React.Component {
  goToProductDescription = url => {
    this.props.history.push(url);
  };
  visitBrand() {
    if (
      this.props.msdItems.brandDetails &&
      this.props.msdItems.brandDetails.brandId
    ) {
      this.props.history.push(`c-${this.props.msdItems.brandDetails.brandId}`);
    }
  }
  renderAboutTheBrand() {
    let brandId;
    if (this.props.msdItems.brandDetails) {
      brandId = this.props.msdItems.brandDetails.brandId;
    }
    return (
      <React.Fragment>
        <div className={styles.brandSection}>
          <div className={styles.brandHeader}>About the brand</div>
          <div className={styles.brandLogoSection}>
            {this.props.productData.brandLogoImage && (
              <div className={styles.brandLogoHolder}>
                <Logo image={this.props.productData.brandLogoImage} />
              </div>
            )}
            {brandId && (
              <div className={styles.followButton}>
                <FollowUnFollowButtonContainer brandId={brandId} />
              </div>
            )}
          </div>
          {this.props.productData.brandInfo && (
            <div className={styles.brandDescription}>
              {this.props.productData.brandInfo}
            </div>
          )}

          {this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY] &&
            this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY].length > 0 &&
            this.renderCarousel(
              this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY]
            )}
          <div className={styles.visitBrandButton}>
            <Button
              type="secondary"
              label="Visit Brand Store"
              onClick={() => this.visitBrand()}
            />
          </div>
        </div>
      </React.Fragment>
    );
  }

  renderCarousel(items) {
    return (
      <div className={styles.brandProductCarousel}>
        <Carousel>
          {items.map((val, i) => {
            const transformedDatum = transformData(val);
            const productImage = transformedDatum.image;
            return (
              <ProductModule
                {...transformedDatum}
                {...this.props}
                productImage={productImage}
                productId={val.productListingId}
                onClick={url => this.goToProductDescription(url)}
              />
            );
          })}
        </Carousel>
      </div>
    );
  }

  renderProductModuleSection(title, key) {
    return (
      this.props.msdItems[key] && (
        <div className={styles.brandSection}>
          <div className={styles.brandHeader}>{title}</div>
          {this.props.msdItems[key] &&
            this.renderCarousel(this.props.msdItems[key])}
        </div>
      )
    );
  }

  render() {
    return (
      <React.Fragment>
        {this.renderAboutTheBrand()}
        {this.renderProductModuleSection(
          "Recommended Products",
          RECOMMENDED_PRODUCTS_WIDGET_KEY
        )}
        {this.renderProductModuleSection(
          "Similar Products",
          SIMILAR_PRODUCTS_WIDGET_KEY
        )}
      </React.Fragment>
    );
  }
}

export default withRouter(PDPRecommendedSections);
