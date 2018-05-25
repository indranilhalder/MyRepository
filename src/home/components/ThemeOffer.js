import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import concat from "lodash.concat";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

const OFFER_AND_ITEM_LIMIT = 4;

export default class ThemeOffer extends React.Component {
  handleClick() {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
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
    let themeData = [],
      items = [];
    const { feedComponentData, ...rest } = this.props;
    if (!feedComponentData) {
      return null;
    }
    if (feedComponentData.items && feedComponentData.items instanceof Array) {
      items = feedComponentData.items.map(transformData);
    }

    let offers = [];
    if (feedComponentData.offers) {
      offers = feedComponentData.offers.map(offer => {
        return transformData(offer);
      });
    }
    themeData = concat(offers, items);
    return (
      <FeedComponent
        backgroundImage={feedComponentData.backgroundImageURL}
        backgroundColor={feedComponentData.backgroundHexCode}
        carouselOptions={{
          header: feedComponentData.title,
          buttonText: feedComponentData.btnText,
          isWhite: true,
          seeAll: () => {
            this.handleClick();
          }
        }}
        {...rest}
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
