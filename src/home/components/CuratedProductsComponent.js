import React from "react";
import Button from "../../general/components/Button";
import styles from "./CuratedProductsComponent.css";
import Grid from "../../general/components/Grid";
import ProductModule from "../../general/components/ProductModule";
import { transformItem } from "./utils.js";
class CuratedProductsComponent extends React.Component {
  render() {
    const {
      feedComponentData,
      backgroundImage,
      headingText,
      subHeader,
      ...rest
    } = this.props;
    const items = feedComponentData.items.map(item => {
      return transformItem(item);
    });

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
                  productImage={
                    datum.imageURL ? datum.imageURL : datum.imageUrl
                  }
                  title={datum.title}
                  price={datum.price}
                  description={datum.description}
                  webURL={datum.webURL}
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
              onClick={this.handleClick}
              label={feedComponentData.btnText}
              color="white"
            />
          )}
        </div>
      </div>
    );
  }
}
export default CuratedProductsComponent;
