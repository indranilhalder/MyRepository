import React, { Component } from "react";
import PropTypes from "prop-types";
import WidgetContainer from "../containers/WidgetContainer";
import AutomatedBrandProductCarousel from "./AutomatedBrandProductCarousel.js";
import BannerProductCarousel from "./BannerProductCarousel.js";
import RecommendationWidget from "./RecommendationWidget.js";
import DiscoverMoreCarousel from "./DiscoverMoreCarousel.js";
import ProductCapsules from "./ProductCapsules.js";
import ThemeOffer from "./ThemeOffer.js";
import styles from "./Feed.css";
import MDSpinner from "react-md-spinner";

const typeComponentMapping = {
  themeOffers: props => <ThemeOffer {...props} />,
  productRecommendationWidget: props => <RecommendationWidget {...props} />,
  bannerProductCarousel: props => <BannerProductCarousel {...props} />,
  automatedBrandProductCarousel: props => (
    <AutomatedBrandProductCarousel {...props} />
  ),
  productCapsules: props => <ProductCapsules {...props} />,
  discoverMoreBaseWidget: props => <DiscoverMoreCarousel {...props} />
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

  renderFeedComponents() {
    return this.props.homeFeedData.map((feedDatum, i) => {
      return this.renderFeedComponent(feedDatum, i);
    });
  }

  renderLoader() {
    return (
      <div className={styles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  }

  componentWillMount() {
    this.props.homeFeed();
  }
  render() {
    if (this.props.loading) {
      return this.renderLoader();
    }
    return <div>{this.renderFeedComponents()}</div>;
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
