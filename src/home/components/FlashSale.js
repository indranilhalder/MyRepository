import React from "react";
import Grid from "../../general/components/Grid";
import ProductModule from "../../general/components/ProductModule";
import PropTypes from "prop-types";
import styles from "./FlashSale.css";
import concat from "lodash.concat";
import { transformData } from "./utils.js";
import Button from "../../general/components/Button.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import TimerCounter from "../../general/components/TimerCounter.js";
import Icon from "../../xelpmoc-core/Icon";
import ClockImage from "../../pdp/components/img/clockWhite.svg";
import { convertDateTimeFromIndianToAmerican } from "../../home/dateTimeUtils.js";

const OFFER_AND_ITEM_LIMIT = 4;

export default class FlashSale extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      collapse: false
    };
  }
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

  handleItemClick = url => {
    const urlSuffix = url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  };

  handleClick = () => {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  };

  onComplete = () => {
    this.setState({ collapse: true });
  };

  render() {
    const { feedComponentData, ...rest } = this.props;
    let items = [];

    if (
      !feedComponentData.endDate ||
      !feedComponentData.startDate ||
      this.state.collapse
    ) {
      return null;
    }

    const startDateTime = new Date(
      convertDateTimeFromIndianToAmerican(feedComponentData.startDate)
    );

    const endDateTime = new Date(
      convertDateTimeFromIndianToAmerican(feedComponentData.endDate)
    );
    // if date time

    const now = Date.now();

    // if now is > start and < end, show
    // if now is < start do not show
    // if now is > end do not show
    if (now > endDateTime || now < startDateTime) {
      return null;
    }

    if (feedComponentData.items) {
      items = feedComponentData.items.map(transformData);
    }

    // Check for date validation

    // feedComponentData.startDate = "13/04/2018 25:40:00";

    let offersAndItemsArray;
    if (feedComponentData.offers) {
      const offers = feedComponentData.offers.map(transformData);
      offersAndItemsArray = concat(offers, items);
    } else {
      offersAndItemsArray = items;
    }

    return (
      <div
        className={styles.base}
        style={{
          background: `${feedComponentData.backgroundHexCode} url(${
            feedComponentData.backgroundImageURL
          })`
        }}
      >
        <div className={styles.header}>
          <div className={styles.headingText}>{feedComponentData.title}</div>
          <div className={styles.offerTime}>
            <div className={styles.clock}>
              <div className={styles.timerHolder}>
                <Icon image={ClockImage} size={20} />
              </div>
              <div className={styles.countDownHolder}>
                <TimerCounter
                  endTime={endDateTime}
                  onComplete={this.onComplete}
                />
              </div>
            </div>
          </div>
        </div>
        <div className={styles.subheader}>{feedComponentData.description}</div>
        <Grid offset={20}>
          {offersAndItemsArray &&
            offersAndItemsArray.map((datum, i) => {
              return (
                <ProductModule
                  key={i}
                  isWhite={true}
                  productImage={datum.image}
                  title={datum.title}
                  price={datum.price}
                  discountPrice={datum.discountPrice}
                  description={datum.description}
                  webURL={datum.webURL}
                  onClick={this.handleItemClick}
                  {...rest}
                  {...datum}
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
