import React from "react";
import Grid from "../../general/components/Grid";
import ProductModule from "../../general/components/ProductModule";
import PropTypes from "prop-types";
import styles from "./FlashSale.css";
import concat from "lodash/concat";
import { transformItem } from "./utils.js";

const OFFER_AND_ITEM_LIMIT = 4;
export default class FlashSale extends React.Component {
  componentDidUpdate() {
    const offers = this.props.feedComponentData.offers;
    const itemIds = this.props.feedComponentData.itemIds;
    let itemIdsToAdd;

    if (offers.length < OFFER_AND_ITEM_LIMIT && itemIds) {
      const numberOfItemsToTake = OFFER_AND_ITEM_LIMIT - offers.length;
      itemIdsToAdd = itemIds.slice(0, numberOfItemsToTake);
      if (
        itemIds.length > 0 &&
        this.props.feedComponentData.items.length === 0
      ) {
        this.props.getItems(this.props.positionInFeed, itemIdsToAdd);
      }
    }
  }

  render() {
    const items = this.props.feedComponentData.items.map(item => {
      return transformItem(item);
    });
    let offersAndItemsArray = concat(
      this.props.feedComponentData.offers,
      items
    );

    console.log("FLASH SALE");
    console.log(this.props.feedComponentData.offers);

    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: this.props.backgroundImage
            ? `url(${this.props.backgroundImage})`
            : "linear-gradient(204deg, #fd2c7a, #ff7255)"
        }}
      >
        <div className={styles.header}>
          <div className={styles.headingText}>{this.props.headingText}</div>
          <div className={styles.offerTime}>
            <div className={styles.clock} />
          </div>
        </div>
        <div className={styles.subheader}>{this.props.subHeader}</div>
        <Grid offset={20}>
          {offersAndItemsArray &&
            offersAndItemsArray.map((datum, i) => {
              return (
                <ProductModule
                  key={i}
                  isWhite={true}
                  productImage={datum.imageURL}
                  title={datum.title}
                  price={datum.price}
                  description={datum.description}
                />
              );
            })}
        </Grid>
      </div>
    );
  }
}
FlashSale.propTypes = {
  offerTime: PropTypes.string,
  headingText: PropTypes.string,
  subHeader: PropTypes.string,
  backgroundImage: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      imageURL: PropTypes.string,
      title: PropTypes.string,
      description: PropTypes.string,
      price: PropTypes.string
    })
  )
};
FlashSale.defaultProps = {
  headingText: "Exclusive offers",
  subHeader: "Grab these offers for a limited time only!"
};
