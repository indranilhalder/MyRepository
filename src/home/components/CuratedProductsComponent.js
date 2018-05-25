import React from "react";
import Button from "../../general/components/Button";
import styles from "./CuratedProductsComponent.css";
import Grid from "../../general/components/Grid";
import ProductModule from "../../general/components/ProductModule";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

class CuratedProductsComponent extends React.Component {
  onClick = val => {
    this.props.history.push(val);
  };

  handleSeeAll = () => {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  };
  render() {
    const { feedComponentData, ...rest } = this.props;
    let items = [];
    if (feedComponentData.items) {
      items = feedComponentData.items.map(transformData);
    }
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <div className={styles.headingText}>{feedComponentData.title}</div>
        </div>
        <Grid offset={20}>
          {items &&
            items.map((datum, i) => {
              return (
                <ProductModule
                  key={i}
                  productImage={datum.image}
                  title={datum.title}
                  price={datum.price}
                  discountPrice={datum.discountPrice}
                  description={datum.description}
                  webURL={datum.webURL}
                  onClick={this.onClick}
                  {...rest}
                />
              );
            })}
        </Grid>
        <div className={styles.button}>
          {feedComponentData.btnText && (
            <Button
              type="hollow"
              width={100}
              onClick={this.handleSeeAll}
              label={feedComponentData.btnText}
              color="#212121"
            />
          )}
        </div>
      </div>
    );
  }
}
export default CuratedProductsComponent;
