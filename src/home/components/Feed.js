import React, { Component } from "react";
import PropTypes from "prop-types";
import WidgetContainer from "../containers/WidgetContainer";
import AutomatedBrandProductCarousel from "./AutomatedBrandProductCarousel.js";
import BannerProductCarousel from "./BannerProductCarousel.js";
import RecommendationWidget from "./RecommendationWidget.js";
import ThemeOffer from "./ThemeOffer.js";

const typeComponentMapping = {
  themeOffers: props => <ThemeOffer {...props} />,
  productRecommendationWidget: props => <RecommendationWidget {...props} />,
  bannerProductCarousel: props => <BannerProductCarousel {...props} />,
  automatedBrandProductCarousel: props => (
    <AutomatedBrandProductCarousel {...props} />
  )
};

class Feed extends Component {
  renderFeedComponent(feedDatum, i) {
    return (
      typeComponentMapping[feedDatum.type] && (
        <WidgetContainer positionInFeed={i} key={i}>
          {typeComponentMapping[feedDatum.type] &&
            typeComponentMapping[feedDatum.type]}
        </WidgetContainer>
      )
    );
  }

  componentWillMount() {
    this.props.homeFeed();
  }
  render() {
    return (
      <div>
        {this.props.homeFeedData.map((feedDatum, i) => {
          return this.renderFeedComponent(feedDatum, i);
        })}
      </div>
    );
  }
}

Feed.propTypes = {
  onSubmit: PropTypes.func,
  onForgotPassword: PropTypes.func,
  onChangeEmail: PropTypes.func,
  onChangePassword: PropTypes.func,
  emailValue: PropTypes.string,
  passwordValue: PropTypes.string,
  loading: PropTypes.bool
};

Feed.defaultProps = {
  loading: false
};

export default Feed;
