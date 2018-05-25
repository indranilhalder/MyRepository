import React, { Component } from "react";
import PropTypes from "prop-types";
import WidgetContainer from "../containers/WidgetContainer";
import HomeSkeleton from "../../general/components/HomeSkeleton.js";
import AutomatedBrandProductCarousel from "./AutomatedBrandProductCarousel.js";
import BannerProductCarousel from "./BannerProductCarousel.js";
import VideoProductCarousel from "./VideoProductCarousel.js";
import RecommendationWidget from "./RecommendationWidget.js";
import HeroBanner from "./HeroBanner.js";
import FollowBase from "./FollowBase.js";
import ConnectWidget from "./ConnectWidget";
import BannerSeparator from "../../general/components/BannerSeparator.js";
import FollowingBrands from "./FollowingBrands";
import ContentWidgetWrapper from "./ContentWidgetWrapper";
import FlashSale from "./FlashSale";
import OfferWidget from "./OfferWidget.js";
import ThemeOffer from "./ThemeOffer.js";
import ThemeProductWidget from "./ThemeProductWidget.js";
import DiscoverMore from "./DiscoverMore.js";
import CuratedProductsComponent from "./CuratedProductsComponent";
import MonoBanner from "./MonoBanner";
import styles from "./Feed.css";
import * as Cookie from "../../lib/Cookie";
import List from "@researchgate/react-intersection-list";
import map from "lodash.map";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  HOME_FEED_TYPE,
  SECONDARY_FEED_TYPE
} from "../../lib/constants";
import {
  renderMetaTags,
  renderMetaTagsWithoutSeoObject
} from "../../lib/seoUtils";
import Loadable from "react-loadable";
import delay from "lodash.delay";
export const PRODUCT_RECOMMENDATION_TYPE = "productRecommendationWidget";

const typeKeyMapping = {
  "Hero Banner Component": "heroBannerComponent"
};

const ProductCapsulesContainer = Loadable({
  loader: () => import("../containers/ProductCapsulesContainer"),
  loading() {
    return <div />;
  }
});

const CMSParagraphComponent = Loadable({
  loader: () => import("../../staticpage/components/CMSParagraphComponent"),
  loading() {
    return <div />;
  }
});

const SimpleBannerComponent = Loadable({
  loader: () => import("../../staticpage/components/SimpleBannerComponent.js"),
  loading() {
    return <div />;
  }
});

const CMSTextComponent = Loadable({
  loader: () => import("../../staticpage/components/CMSTextComponent.js"),
  loading() {
    return <div />;
  }
});

const AccountNavigationComponent = Loadable({
  loader: () =>
    import("../../staticpage/components/AccountNavigationComponent.js"),
  loading() {
    return <div />;
  }
});

const TopCategories = Loadable({
  loader: () => import("../../blp/components/TopCategories"),
  loading() {
    return <div />;
  }
});

const SubBrandsBanner = Loadable({
  loader: () => import("../../blp/components/SubBrandsBanner"),
  loading() {
    return <div />;
  }
});

const BrandCardHeader = Loadable({
  loader: () => import("../../blp/components/BrandCardHeader"),
  loading() {
    return <div />;
  }
});

const AllBrandTypes = Loadable({
  loader: () => import("../../blp/components/AllBrandTypes"),
  loading() {
    return <div />;
  }
});

const CuratedFeature = Loadable({
  loader: () => import("../../blp/components/CuratedFeature"),
  loading() {
    return <div />;
  }
});

const LatestCollections = Loadable({
  loader: () => import("../../blp/components/LatestCollections"),
  loading() {
    return <div />;
  }
});

export const typeComponentMapping = {
  "Product Capsules Component": props => (
    <ProductCapsulesContainer {...props} />
  ),
  "Landing Page Header Component": props => {
    return <BrandCardHeader {...props} />;
  },
  "Hero Banner Component": props => <HeroBanner {...props} />, // no hard coded data
  "Theme Offers Component": props => <ThemeOffer {...props} />, // no hard coded data
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
  "Automated Banner Product Carousel Component": props => (
    <AutomatedBrandProductCarousel {...props} />
  ),
  "Auto Following Brands Component": props => <FollowingBrands {...props} />,
  "Flash Sales Component": props => <FlashSale {...props} />, // wired up
  "Offers Component": props => <OfferWidget {...props} />, // wired up
  "Multipurpose Banner Component": props => <ConnectWidget {...props} />, // modal not working - need to figure out what to show here.
  "Multi Click Component": props => <ThemeProductWidget {...props} />,
  "Auto Fresh From Brands Component": props => <FollowBase {...props} />, // wired up with clickable url
  "Banner Separator Component": props => <BannerSeparator {...props} />,
  "Auto Discover More Component": props => <DiscoverMore {...props} />,
  "Top Categories Component": props => <TopCategories {...props} />,
  "Recently viewed product": props => <RecommendationWidget {...props} />,
  "Single Banner Component": props => <MonoBanner {...props} />,
  "Curated Listing Strip Component": props => <LatestCollections {...props} />,
  "Two by Two Banner Component": props => <CuratedFeature {...props} />,
  "Curated Products Component": props => (
    <CuratedProductsComponent {...props} />
  ),
  "Sub Brands Banner Component": props => <SubBrandsBanner {...props} />,
  "Landing Page Hierarchy": props => <AllBrandTypes {...props} />,
  "Landing Page Hierarchy Component": props => <AllBrandTypes {...props} />,
  "CMS Paragraph Component": props => <CMSParagraphComponent {...props} />,
  "Simple Banner Component": props => {
    return (
      <div className={styles.simpleBannerHolder}>
        {" "}
        <SimpleBannerComponent {...props} />{" "}
      </div>
    );
  },
  "CMS Text Component": props => {
    let parsedContent;

    try {
      parsedContent = JSON.parse(props.feedComponentData.content);
    } catch (e) {
      if (props.displayToast) {
        props.displayToast("JSON Parse error, check static page content");
      }
    }
    return (
      parsedContent &&
      map(parsedContent, content => {
        return <CMSTextComponent data={content} />;
      })
    );
  },
  "Account Navigation Component": props => (
    <AccountNavigationComponent {...props} />
  )
};

class Feed extends Component {
  componentDidMount() {
    const titleObj =
      this.props.homeFeedData &&
      this.props.homeFeedData.find(data => {
        return data.type === "Landing Page Title Component";
      });
    if (this.props.feedType === HOME_FEED_TYPE) {
      if (titleObj) {
        this.props.setHeaderText(titleObj.title);
      } else {
        this.props.setHeaderText(this.props.headerMessage);
      }
    } else {
      if (!this.props.headerMessage) {
        if (titleObj && this.props.setHeaderText) {
          this.props.setHeaderText(titleObj.title);
        }
      }
    }
  }
  componentDidUpdate() {
    if (this.props.homeFeedData && !this.props.headerMessage) {
      const titleObj =
        this.props.homeFeedData &&
        this.props.homeFeedData.find(data => {
          return data.type === "Landing Page Title Component";
        });

      if (titleObj && this.props.setHeaderText) {
        this.props.setHeaderText(titleObj.title);
      }
    }

    if (this.props.headerMessage) {
      this.props.setHeaderText(this.props.headerMessage);
    }
  }

  renderFeedComponent = (index, key) => {
    const feedDatum = this.props.homeFeedData[index];
    if (feedDatum.type === "Product Capsules Component") {
      return <ProductCapsulesContainer positionInFeed={index} />;
    }

    if (this.props.pageSize && index > this.props.pageSize) {
      this.props.setPageFeedSize(index);
    }

    const setClickedElementId = (id => {
      return () => {
        this.props.setClickedElementId(`Feed-${id}`);
      };
    })(index);

    let props = {
      positionInFeed: index,
      key: index,
      id: `Feed-${index}`,
      type: typeKeyMapping[feedDatum.type],
      postData: feedDatum.postParams,
      feedType: this.props.feedType
    };

    if (this.props.setClickedElementId) {
      props = {
        ...props,
        setClickedElementId
      };
    }
    return (
      typeComponentMapping[feedDatum.type] && (
        <WidgetContainer {...props}>
          {typeComponentMapping[feedDatum.type] &&
            typeComponentMapping[feedDatum.type]}
        </WidgetContainer>
      )
    );
  };

  renderFeedComponents() {
    return (
      this.props.homeFeedData &&
      this.props.homeFeedData.map((feedDatum, i) => {
        return this.renderFeedComponent(feedDatum, i);
      })
    );
  }

  componentWillMount() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    console.log("COMPONENT WILL MOUNT");
    if (
      this.props.feedType === HOME_FEED_TYPE &&
      this.props.homeFeedData.length === 0
    ) {
      this.props.homeFeed();
    }
    if (userDetails && customerCookie && this.props.getWishListItems) {
      this.props.getWishListItems();
    }
    if (this.props.clickedElementId) {
      delay(() => {
        const clickedElement = document.getElementById(
          this.props.clickedElementId
        );
        if (clickedElement) {
          delay(() => clickedElement.scrollIntoView(true), 10);
        }
      });
    }
  }

  renderFeed = (items, ref) => {
    return (
      <div className={styles.base} ref={ref}>
        <div className={styles.center}>{items}</div>
      </div>
    );
  };

  renderMetaTags = () => {
    const data = this.props.homeFeedData;
    return data.seo
      ? renderMetaTags(data)
      : renderMetaTagsWithoutSeoObject(data);
  };

  render() {
    if (this.props.loading) {
      return <HomeSkeleton />;
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
      if (landingPageTitleObj.type === "Landing Page Title Component") {
        propsForHeader = {
          hasBackButton: true,
          text: landingPageTitleObj.title
        };
      }
    }
    return (
      <React.Fragment>
        {this.renderMetaTags()}
        {this.props.homeFeedData ? (
          <List
            pageSize={this.props.pageSize ? this.props.pageSize : 1}
            currentLength={this.props.homeFeedData.length}
            itemsRenderer={this.renderFeed}
          >
            {this.renderFeedComponent}
          </List>
        ) : null}
      </React.Fragment>
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
  loading: PropTypes.bool,
  feedType: PropTypes.oneOf([HOME_FEED_TYPE, SECONDARY_FEED_TYPE])
};

Feed.defaultProps = {
  loading: false
};

export default Feed;
