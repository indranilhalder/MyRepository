import React, { Component } from "react";
import PropTypes from "prop-types";
import WidgetContainer from "../containers/WidgetContainer";
import AutomatedBrandProductCarousel from "./AutomatedBrandProductCarousel.js";
import BannerProductCarousel from "./BannerProductCarousel.js";
import VideoProductCarousel from "./VideoProductCarousel.js";
import RecommendationWidget from "./RecommendationWidget.js";
import HeroBanner from "./HeroBanner.js";
import FollowBase from "./FollowBase.js";
import ConnectWidget from "./ConnectWidget";
import BannerSeparator from "../../general/components/BannerSeparator.js";
import SingleQuestionContainer from "../containers/SingleQuestionContainer.js";
import DiscoverMoreCarousel from "./DiscoverMoreCarousel.js";
import ProductCapsules from "./ProductCapsules.js";
import FollowingBrands from "./FollowingBrands";
import FlashSale from "./FlashSale";
import OfferWidget from "./OfferWidget.js";
import DiscoverMore500 from "./DiscoverMore500.js";
import ThemeOffer from "./ThemeOffer.js";
import ThemeProductWidget from "./ThemeProductWidget.js";
import MultiSelectQuestionContainer from "../containers/MultiSelectQuestionContainer.js";
import styles from "./Feed.css";
import MDSpinner from "react-md-spinner";

export const PRODUCT_RECOMMENDATION_TYPE = "productRecommendationWidget";

const typeKeyMapping = {
  "Hero Banner Component": "heroBannerComponent"
};

const typeComponentMapping = {
  // "Hero Banner Component": props => <HeroBanner {...props} />,
  "Theme Offers Component": props => <ThemeOffer {...props} />
  // productRecommendationWidget: props => <RecommendationWidget {...props} />,
  // "Banner Product Carousel Component": props => (
  //   <BannerProductCarousel {...props} />
  // )
  // "Video Product Carousel Component": props => (
  //   <VideoProductCarousel {...props} />
  // )
  // automatedBrandProductCarousel: props => (
  //   <AutomatedBrandProductCarousel {...props} />
  // ),
  // "Flash Sales Component": props => <FlashSale {...props} />,
  // "Offers Component": props => <OfferWidget {...props} />,
  // "Multipurpose Banner Component": props => <ConnectWidget {...props} />
  // themeProductWidget: props => <ThemeProductWidget {...props} />,
  // multiSelectQuestion: props => <MultiSelectQuestionContainer {...props} />,
  // followBaseWidget: props => <FollowBase {...props} />,
  // singleSelectQuestion: props => <SingleQuestionContainer {...props} />,
  // bannerSeparator: props => <BannerSeparator {...props} />,
  // productCapsules: props => <ProductCapsules {...props} />,
  // discoverMoreBaseWidget: props => <DiscoverMoreCarousel {...props} />,
  // discoverMoreWidget: props => <DiscoverMore500 {...props} />,
  // followedWidget: props => <FollowingBrands {...props} />
};

class Feed extends Component {
  renderFeedComponent(feedDatum, i) {
    return (
      typeComponentMapping[feedDatum.type] && (
        <WidgetContainer
          positionInFeed={i}
          key={i}
          type={typeKeyMapping[feedDatum.type]}
          postData={feedDatum.postParams}
        >
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
    return (
      <div className={styles.base}>
        <div className={styles.center}>{this.renderFeedComponents()}</div>
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
