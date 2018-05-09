import React from "react";
import DumbCarousel from "../../general/components/DumbCarousel.js";
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
import { FollowUnFollowButtonContainer } from "../containers/FollowUnFollowButtonContainer";
import styles from "./PDPRecommendedSections.css";
import { PDP_FOLLOW_AND_UN_FOLLOW } from "../../lib/constants.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

// only want to kick off a request for the MSD stuff if they are visible.

class PDPRecommendedSections extends React.Component {
  goToProductDescription = url => {
    this.props.history.push(url);
  };
  visitBrand() {
    if (this.props.aboutTheBrand.webURL) {
      const url = this.props.aboutTheBrand.webURL.replace(TATA_CLIQ_ROOT, "$1");
      this.props.history.push(url);
    } else if (this.props.aboutTheBrand && this.props.aboutTheBrand.brandId) {
      this.props.history.push(`c-${this.props.aboutTheBrand.brandId}`);
    }
  }
  renderAboutTheBrand() {
    let brandId;

    if (this.props.aboutTheBrand) {
      brandId = this.props.aboutTheBrand.id;
    }

    return (
      this.props.aboutTheBrand && (
        <React.Fragment>
          <div className={styles.brandSection}>
            <h3 className={styles.brandHeader}>About the brand</h3>
            <div className={styles.brandLogoSection}>
              {this.props.aboutTheBrand.brandLogo && (
                <div className={styles.brandLogoHolder}>
                  <Logo image={this.props.aboutTheBrand.brandLogo} />
                </div>
              )}
              {brandId && (
                <div className={styles.followButton}>
                  <FollowUnFollowButtonContainer
                    brandId={brandId}
                    isFollowing={this.props.aboutTheBrand.isFollowing}
                    pageType={PDP_FOLLOW_AND_UN_FOLLOW}
                  />
                </div>
              )}
            </div>
            {this.props.aboutTheBrand.description && (
              <h3 className={styles.brandDescription}>
                {this.props.aboutTheBrand.description}
              </h3>
            )}

            {this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY] &&
              this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY].length > 0 &&
              this.renderCarousel(
                this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY]
              )}
            {brandId && (
              <div className={styles.visitBrandButton}>
                <Button
                  type="secondary"
                  label="Visit Brand Store"
                  onClick={() => this.visitBrand()}
                />
              </div>
            )}
          </div>
        </React.Fragment>
      )
    );
  }

  renderCarousel(items) {
    return (
      <div className={styles.brandProductCarousel}>
        <DumbCarousel elementWidth={45}>
          {items.map((val, i) => {
            const transformedDatum = transformData(val);
            const productImage = transformedDatum.image;
            return (
              <ProductModule
                key={i}
                {...transformedDatum}
                {...this.props}
                productImage={productImage}
                productId={val.productListingId}
                isShowAddToWishlistIcon={false}
                onClick={url => this.goToProductDescription(url)}
              />
            );
          })}
        </DumbCarousel>
      </div>
    );
  }

  renderProductModuleSection(title, key) {
    return (
      this.props.msdItems[key] && (
        <div className={styles.brandSection}>
          <h3 className={styles.brandHeader}>{title}</h3>
          {this.props.msdItems[key] &&
            this.renderCarousel(this.props.msdItems[key])}
        </div>
      )
    );
  }

  render() {
    return (
      <React.Fragment>
        {this.renderProductModuleSection(
          "Recommended Products",
          "recommendedProducts"
        )}
        {this.renderProductModuleSection(
          "Similar Products",
          SIMILAR_PRODUCTS_WIDGET_KEY
        )}
        {this.renderAboutTheBrand()}
      </React.Fragment>
    );
  }
}

export default withRouter(PDPRecommendedSections);
