import React from "react";
import Grid from "../../general/components/Grid";
import ProductModule from "../../general/components/ProductModule";
import PropTypes from "prop-types";
import styles from "./FlashSale.css";
import concat from "lodash/concat";
import { transformData } from "./utils.js";
import Button from "../../general/components/Button.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import TimerCounter from "../../general/components/TimerCounter.js";
import { Icon } from "xelpmoc-core";
import ClockImage from "../../pdp/components/img/clockWhite.svg";

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

  handleClick = () => {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
  };

  render() {
    const {
      feedComponentData,
      backgroundImage,
      headingText,
      subHeader,
      ...rest
    } = this.props;
    let items = [];

    if (feedComponentData.items) {
      items = feedComponentData.items.map(transformData);
    }

    let offersAndItemsArray;
    if (feedComponentData.offers) {
      offersAndItemsArray = concat(feedComponentData.offers, items);
    } else {
      offersAndItemsArray = items;
    }
    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: backgroundImage
            ? `url(${backgroundImage})`
            : "linear-gradient(204deg, #fd2c7a, #ff7255)"
        }}
      >
        <div className={styles.header}>
          <div className={styles.headingText}>{headingText}</div>
          <div className={styles.offerTime}>
            <div className={styles.clock}>
              <div className={styles.timerHolder}>
                <Icon image={ClockImage} size={20} />
              </div>
              <div className={styles.countDownHolder}>
                <TimerCounter endTime={feedComponentData.endDate} />
              </div>
            </div>
          </div>
        </div>
        <div className={styles.subheader}>{subHeader}</div>
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
                  webURL={datum.webURL}
                  onClick={this.handleClick}
                  {...rest}
                />
              );
            })}
        </Grid>
        <div className={styles.button}>
          <Button
            type="hollow"
            width={100}
            onClick={this.handleClick}
            label={feedComponentData.btnText}
            color="white"
          />
        </div>
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
