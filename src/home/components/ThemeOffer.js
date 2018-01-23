import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import Background from "./img/bg.jpg";
import { transformData } from "./utils.js";

export default class ThemeOffer extends React.Component {
  handleClick() {
    if (this.props.seeAll) {
      this.props.seeAll();
    }
  }

  render() {
    const feedComponentData = this.props.feedComponentData;
    let themeData = [];

    if (
      feedComponentData.data.offers &&
      feedComponentData.data.offers.length < 4
    ) {
      let themeOffersData = feedComponentData.data.offers;
      let count = 4 - themeOffersData.length;
      let themeItemsData = [...feedComponentData.data.items].slice(0, count);
      themeData = [...themeOffersData, ...themeItemsData];
      themeData = themeData.map(transformData);
    } else {
      if (feedComponentData.data.offers) {
        themeData = feedComponentData.data.offers.slice(0, 4);
        themeData = themeData.map(transformData);
      }
    }

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
