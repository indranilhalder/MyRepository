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

import styles from "./PDPRecommendedSections.css";

class PDPRecommendedSections extends React.Component {
  renderAboutTheBrand() {
    return (
      <React.Fragment>
        {this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY] &&
        this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY].length > 0 ? (
          <div className={styles.brandSection}>
            <div className={styles.brandHeader}>About the brand</div>
            <div className={styles.brandLogoSection}>
              {this.props.productData.brandLogoImage && (
                <div className={styles.brandLogoHolder}>
                  <Logo image={this.props.productData.brandLogoImage} />
                </div>
              )}
              <div className={styles.followButton}>
                <Button label="Follow" type="tertiary" />
              </div>
            </div>
            {this.props.productData.brandInfo && (
              <div className={styles.brandDescription}>
                {this.props.productData.brandInfo}
              </div>
            )}

            {this.renderCarousel(
              this.props.msdItems[ABOUT_THE_BRAND_WIDGET_KEY]
            )}
            <div className={styles.visitBrandButton}>
              <Button
                type="secondary"
                label="Visit Brand Store"
                oncLick={() => this.visitBrand()}
              />
            </div>
          </div>
        ) : null}
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
    console.log("OFFERS");
    console.log(this.props.productData);
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
