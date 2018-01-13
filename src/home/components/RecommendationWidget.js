import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";

export default class RecommendationWidget extends React.Component {
  handleClick() {
    if (this.props.seeAll) {
      this.props.seeAll();
    }
  }
  render() {
    return (
      <FeedComponent
        backgroundColor="#e4e4e4"
        carouselOptions={{
          header: "Recommended for you",
          buttonText: "See All",
          seeAll: () => {
            this.handleClick();
          }
        }}
        {...this.props}
      />
    );
  }
}
RecommendationWidget.propTypes = {
  seeAll: PropTypes.func
};
