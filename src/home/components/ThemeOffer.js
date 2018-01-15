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
    let carouselData;
    if (feedComponentData.data instanceof Array) {
      carouselData = this.props.feedComponentData.data.map(transformData);
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
        data={carouselData}
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
