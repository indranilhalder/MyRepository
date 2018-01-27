import React, { Component } from "react";
import PropTypes from "prop-types";
import WidgetContainer from "../containers/WidgetContainer";
import AutomatedBrandProductCarousel from "./AutomatedBrandProductCarousel.js";
import BannerProductCarousel from "./BannerProductCarousel.js";
import VideoProductCarousel from "./VideoProductCarousel.js";
import RecommendationWidget from "./RecommendationWidget.js";
import FollowBase from "./FollowBase.js";
import BannerSeparator from "../../general/components/BannerSeparator.js";
import SingleQuestionContainer from "../containers/SingleQuestionContainer.js";
import DiscoverMoreCarousel from "./DiscoverMoreCarousel.js";
import ProductCapsules from "./ProductCapsules.js";
import FollowingBrands from "./FollowingBrands";
import FlashSale from "./FlashSale";
import OfferWidget from "./OfferWidget.js";
import ThemeOffer from "./ThemeOffer.js";
import MultiSelectQuestionContainer from "../containers/MultiSelectQuestionContainer.js";
import styles from "./Feed.css";
import MDSpinner from "react-md-spinner";

export const PRODUCT_RECOMMENDATION_TYPE = "productRecommendationWidget";

const typeComponentMapping = {
  themeOffers: props => <ThemeOffer {...props} />,
  productRecommendationWidget: props => <RecommendationWidget {...props} />,
  bannerProductCarousel: props => <BannerProductCarousel {...props} />,
  videoProductCarousel: props => <VideoProductCarousel {...props} />,
  automatedBrandProductCarousel: props => (
    <AutomatedBrandProductCarousel {...props} />
  ),
  flashSales: props => <FlashSale {...props} />,
  offersWidget: props => <OfferWidget {...props} />,
  multiSelectQuestion: props => <MultiSelectQuestionContainer {...props} />,
  followBaseWidget: props => <FollowBase {...props} />,
  singleSelectQuestion: props => <SingleQuestionContainer {...props} />,
  bannerSeparator: props => <BannerSeparator {...props} />,
  productCapsules: props => <ProductCapsules {...props} />,
  discoverMoreBaseWidget: props => <DiscoverMoreCarousel {...props} />,
  followedWidget: props => <FollowingBrands {...props} />
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
