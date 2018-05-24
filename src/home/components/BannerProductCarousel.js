import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import ProductImageHeader from "../../general/components/ProductImageHeader";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class BannerProductCarousal extends React.Component {
  handleClick() {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
  }

  componentDidUpdate() {
    const data = this.props.feedComponentData;
    if (
      data.items &&
      data.items.length === 0 &&
      data.itemIds &&
      data.itemIds.length > 0
    ) {
      this.props.getItems(this.props.positionInFeed, data.itemIds);
    }
  }
  render() {
    const feedComponentData = this.props.feedComponentData;
    if (!feedComponentData) {
      return null;
    }

    let data = [];
    if (feedComponentData.items && feedComponentData.items instanceof Array) {
      data = feedComponentData.items.map(transformData);
    } else {
      return null;
    }
    return (
      <FeedComponent
        banner={
          <ProductImageHeader
            image={feedComponentData.imageURL}
            name={feedComponentData.title}
            label={feedComponentData.description}
            onClick={() => this.handleClick()}
          />
        }
        carouselOptions={{
          buttonText: feedComponentData.btnText,
          seeAll: () => {
            this.handleClick();
          }
        }}
        setClickedElementId={this.props.setClickedElementId}
        data={data}
      />
    );
  }
}
BannerProductCarousal.propTypes = {
  seeAll: PropTypes.func
};
