import React from "react";
import DumbCarousel from "../../general/components/DumbCarousel.js";
import ProductModule from "../../general/components/ProductModule.js";
import { transformData } from "../../home/components/utils.js";
import Logo from "../../general/components/Logo.js";
import Button from "../../general/components/Button.js";
import { withRouter } from "react-router-dom";
import Observer from "@researchgate/react-intersection-observer";

import {
  ABOUT_THE_BRAND_WIDGET_KEY,
  SIMILAR_PRODUCTS_WIDGET_KEY
} from "../actions/pdp.actions.js";
import { FollowUnFollowButtonContainer } from "../containers/FollowUnFollowButtonContainer";
import styles from "./PDPRecommendedSections.css";
import {
  PDP_FOLLOW_AND_UN_FOLLOW,
  PRODUCT_DESCRIPTION_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
} from "../../lib/constants.js";
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
    console.log("RENDER ABOUT THE BRAND");
    console.log(this.props.aboutTheBrand);

    if (this.props.aboutTheBrand) {
      brandId = this.props.aboutTheBrand.id;
    }

    return (
      this.props.aboutTheBrand && (
        <div className={styles.brandSection}>
          <h3 className={styles.brandHeader}>About the Brand</h3>
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
    return this.props.msdItems[key] ? (
      <div className={styles.brandSection}>
        <h3 className={styles.brandHeader}>{title}</h3>
        {this.props.msdItems[key] &&
          this.renderCarousel(this.props.msdItems[key])}
      </div>
    ) : null;
  }

  handleIntersection = event => {
    if (event.isIntersecting) {
      if (this.props.visitedNewProduct) {
        if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
          this.props.setToOld();
          this.props.getMsdRequest(this.props.match.params[0]);
          this.props.pdpAboutBrand(this.props.match.params[0]);
        } else if (
          this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
        ) {
          this.props.setToOld();
          this.props.getMsdRequest(this.props.match.params[1]);
          this.props.pdpAboutBrand(this.props.match.params[1]);
        }
      }
    }
  };

  render() {
    const options = {
      onChange: this.handleIntersection
    };
    return (
      <React.Fragment>
        <Observer {...options}>
          <div className={styles.observer} />
        </Observer>
        {this.renderAboutTheBrand()}
        {this.renderProductModuleSection(
          "Similar Products",
          "recommendedProducts"
        )}
        {this.renderProductModuleSection(
          "Frequently Bought Together",
          SIMILAR_PRODUCTS_WIDGET_KEY
        )}
      </React.Fragment>
    );
  }
}

export default withRouter(PDPRecommendedSections);
