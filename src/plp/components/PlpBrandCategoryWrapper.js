import React from "react";
import BrandLandingPageContainer from "../../brands/containers/BrandLandingPageContainer";
import { Redirect } from "react-router";
import MDSpinner from "react-md-spinner";
import { BLP_OR_CLP_FEED_TYPE } from "../../lib/constants";

export const CATEGORY_REGEX = /c-msh*/;
export const BRAND_REGEX = /c-mbh*/;
export const CAPTURE_REGEX = /c-(.*)/;

export default class PlpBrandCategoryWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pageType: props.location.pathname,
      redirectToPlp: false
    };
  }

  componentWillMount() {
    const categoryOrBrandId = this.props.location.pathname.match(
      CAPTURE_REGEX
    )[1];

    this.props.homeFeed(categoryOrBrandId);
  }

  renderLoader() {
    return <MDSpinner />;
  }

  getPlpUrl = () => {
    const url = this.props.location.pathname;
    let match;
    let searchText;
    if (CATEGORY_REGEX.test(url)) {
      match = CAPTURE_REGEX.exec(url)[1];
      match = match.toUpperCase();
      searchText = `:relevance:category:${match}`;
    }

    if (BRAND_REGEX.test(url)) {
      match = CAPTURE_REGEX.exec(url)[1];
      match = match.toUpperCase();
      searchText = `:relevance:brand:${match}`;
    }

    return `/search/?q=${searchText}`;
  };

  render() {
    if (
      this.props.homeFeedData.loading ||
      this.props.homeFeedData.feedType === null
    ) {
      return this.renderLoader();
    }

    if (this.props.homeFeedData.feedType === BLP_OR_CLP_FEED_TYPE) {
      if (this.props.homeFeedData.homeFeed.length > 0) {
        return <BrandLandingPageContainer />;
      } else {
        return <Redirect to={this.getPlpUrl()} />;
      }
    }
    return null;
  }
}
