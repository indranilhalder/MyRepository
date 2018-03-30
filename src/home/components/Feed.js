import React, { Component } from "react";
import PropTypes from "prop-types";
import WidgetContainer from "../containers/WidgetContainer";
import AutomatedBrandProductCarousel from "./AutomatedBrandProductCarousel.js";
import BannerProductCarousel from "./BannerProductCarousel.js";
import VideoProductCarousel from "./VideoProductCarousel.js";
import RecommendationWidget from "./RecommendationWidget.js";
import HeroBanner from "./HeroBanner.js";
import FollowBase from "./FollowBase.js";
import SearchContainer from "../../search/SearchContainer";
import ConnectWidget from "./ConnectWidget";
import BrandCardHeader from "../../blp/components/BrandCardHeader";
import BannerSeparator from "../../general/components/BannerSeparator.js";

import SingleQuestionContainer from "../containers/SingleQuestionContainer.js";
import DiscoverMoreCarousel from "./DiscoverMoreCarousel.js";
import ProductCapsules from "./ProductCapsules.js";
import FollowingBrands from "./FollowingBrands";
import ContentWidgetWrapper from "./ContentWidgetWrapper";
import FlashSale from "./FlashSale";
import AllBrandTypes from "../../blp/components/AllBrandTypes";
import OfferWidget from "./OfferWidget.js";
import DiscoverMore500 from "./DiscoverMore500.js";
import ThemeOffer from "./ThemeOffer.js";
import ThemeProductWidget from "./ThemeProductWidget.js";
import MultiSelectQuestionContainer from "../containers/MultiSelectQuestionContainer.js";
import DiscoverMore from "./DiscoverMore.js";
import CuratedProductsComponent from "./CuratedProductsComponent";
import CuratedFeature from "../../blp/components/CuratedFeature";
import LatestCollections from "../../blp/components/LatestCollections";
import MonoBanner from "./MonoBanner";
import styles from "./Feed.css";
import MDSpinner from "react-md-spinner";
import TopCategories from "../../blp/components/TopCategories";
import SubBrandsBanner from "../../blp/components/SubBrandsBanner";
import { MERGE_CART_ID_SUCCESS } from "../../cart/actions/cart.actions";
import { CHECKOUT_ROUTER } from "../../lib/constants";
import queryString from "query-string";
export const PRODUCT_RECOMMENDATION_TYPE = "productRecommendationWidget";

const typeKeyMapping = {
  "Hero Banner Component": "heroBannerComponent"
};

const typeComponentMapping = {
  "Landing Page Header Component": props => <BrandCardHeader {...props} />,
  "Hero Banner Component": props => <HeroBanner {...props} />,
  "Theme Offers Component": props => <ThemeOffer {...props} />,
  "Auto Product Recommendation Component": props => (
    <RecommendationWidget {...props} />
  ),
  "Content Component": props => <ContentWidgetWrapper {...props} />,
  "Banner Product Carousel Component": props => (
    <BannerProductCarousel {...props} />
  ),
  "Video Product Carousel Component": props => (
    <VideoProductCarousel {...props} />
  ),

  // // automatedBrandProductCarousel: props => (
  // //   <AutomatedBrandProductCarousel {...props} />
  // // ),

  "Flash Sales Component": props => <FlashSale {...props} />, // wired up
  "Offers Component": props => <OfferWidget {...props} />, // wired up
  "Multipurpose Banner Component": props => <ConnectWidget {...props} />, // modal not working - need to figure out what to show here.
  // "Multi Click Component": props => <ThemeProductWidget {...props} /> // not wired up for some reason
  "Auto Fresh From Brands Component": props => <FollowBase {...props} />, // wired up with clickable url
  "Banner Separator Component": props => <BannerSeparator {...props} />,
  "Auto Discover More Component": props => <DiscoverMore {...props} />, // wired up with clickable urls
  "Auto Product Recommendation": props => <RecommendationWidget {...props} />,
  "Recently viewed product": props => <RecommendationWidget {...props} />,
  "Single Banner Component": props => <MonoBanner {...props} />,
  "Curated Listing Strip Component": props => <LatestCollections {...props} />,
  "Two by Two Banner Component": props => <CuratedFeature {...props} />,
  "Curated Products Component": props => (
    <CuratedProductsComponent {...props} />
  ),
  "Sub Brands Banner Component": props => <SubBrandsBanner {...props} />,
  "Top Categories Component": props => <TopCategories {...props} />,
  "Landing Page Hierarchy": props => <AllBrandTypes {...props} />
};

class Feed extends Component {
  constructor(props) {
    super(props);
    this.hasSeenThemeProductOrAutomatedBrandCarousel = true;
  }

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
    return (
      this.props.homeFeedData &&
      this.props.homeFeedData.map((feedDatum, i) => {
        return this.renderFeedComponent(feedDatum, i);
      })
    );
  }

  renderLoader() {
    return (
      <div className={styles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  }

  componentWillMount() {
    if (this.props.isHomeFeedPage) {
      this.props.homeFeed();
    }
  }

  render() {
    if (this.props.loading) {
      return this.renderLoader();
    }

    let propsForHeader = {};
    if (this.props.isHomeFeedPage) {
      propsForHeader = {
        hasBackButton: false,
        text: this.props.headerMessage
      };
    } else {
      let landingPageTitleObj = this.props.homeFeedData[0]
        ? this.props.homeFeedData[0]
        : {};
      if (landingPageTitleObj.type === "Landing Page Title") {
        propsForHeader = {
          hasBackButton: true,
          text: landingPageTitleObj.title
        };
      }
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
