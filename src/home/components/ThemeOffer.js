import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import Background from "./img/bg.jpg";
export default class ThemeOffer extends React.Component {
  handleClick() {
    if (this.props.seeAll) {
      this.props.seeAll();
    }
  }
  render() {
    return (
      <FeedComponent
        backgroundImage={Background}
        carouselOptions={{
          header: this.props.header,
          buttonText: "See All",
          isWhite: true,
          seeAll: () => {
            this.handleClick();
          }
        }}
        {...this.props}
      />
    );
  }
}
ThemeOffer.propTypes = {
  header: PropTypes.string,
  seeAll: PropTypes.func
};
