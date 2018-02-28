import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import Background from "./img/bg.jpg";
import { PRODUCT_LISTINGS } from "../../lib/constants";
import concat from "lodash/concat";
import { transformData, transformItem } from "./utils.js";

const OFFER_AND_ITEM_LIMIT = 4;

export default class ThemeOffer extends React.Component {
  handleClick() {
    this.props.history.push(PRODUCT_LISTINGS);
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

  render() {
    let themeData = [];
    const items = this.props.feedComponentData.items.map(item => {
      return transformItem(item);
    });
    const offers = this.props.feedComponentData.offers.map(offer => {
      return transformData(offer);
    });
    themeData = concat(offers, items);

    return (
      <FeedComponent
        backgroundImage={Background}
        carouselOptions={{
          header: this.props.feedComponentData.title,
          buttonText: this.props.buttonText,
          isWhite: true,
          seeAll: () => {
            this.handleClick();
          }
        }}
        data={themeData}
      />
    );
  }
}
ThemeOffer.propTypes = {
  header: PropTypes.string,
  seeAll: PropTypes.func,
  buttonText: PropTypes.string,
  feedComponentData: PropTypes.object
};
