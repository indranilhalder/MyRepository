import React from "react";
import BrandLandingPageContainer from "../../blp/containers/BrandLandingPageContainer";
import Loader from "../../general/components/Loader";
import ProductListingsContainer from "../containers/ProductListingsContainer";
import { SECONDARY_FEED_TYPE, NOT_FOUND } from "../../lib/constants";

import queryString from "query-string";

export const CATEGORY_REGEX = /c-msh*/;
export const BRAND_REGEX = /c-mbh*/;
export const CATEGORY_CAPTURE_REGEX = /c-msh([a-zA-Z0-9]+)/;
export const BRAND_CAPTURE_REGEX = /c-mbh([a-zA-Z0-9]+)/;
export const BRAND_CATEGORY_PREFIX = "c-";

export default class PlpBrandCategoryWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pageType: props.location.pathname,
      redirectToPlp: false
    };
    this.pathname = props.location.pathname;
  }

  componentWillMount() {
    try {
      const url = this.props.location.pathname;

      let categoryOrBrandId = null;

      if (CATEGORY_REGEX.test(url)) {
        categoryOrBrandId = url.match(CATEGORY_CAPTURE_REGEX)[0];
      }

      if (BRAND_REGEX.test(url)) {
        categoryOrBrandId = url.match(BRAND_CAPTURE_REGEX)[0];
      }

      categoryOrBrandId = categoryOrBrandId.replace(BRAND_CATEGORY_PREFIX, "");

      this.props.getFeed(categoryOrBrandId);
    } catch (e) {
      this.props.history.replace(NOT_FOUND);
    }
  }

  componentDidUpdate() {
    try {
      const url = this.props.location.pathname;

      let categoryOrBrandId = null;

      if (CATEGORY_REGEX.test(url)) {
        categoryOrBrandId = url.match(CATEGORY_CAPTURE_REGEX)[0];
      }

      if (BRAND_REGEX.test(url)) {
        categoryOrBrandId = url.match(BRAND_CAPTURE_REGEX)[0];
      }

      categoryOrBrandId = categoryOrBrandId.replace(BRAND_CATEGORY_PREFIX, "");

      if (
        this.props.homeFeedData.feedType === SECONDARY_FEED_TYPE &&
        this.pathname !== this.props.location.pathname
      ) {
        this.pathname = this.props.location.pathname;
        this.props.getFeed(categoryOrBrandId);
      }
    } catch (e) {
      this.props.history.replace(NOT_FOUND);
    }
  }
  renderLoader() {
    return <Loader />;
  }

  getPlpSearchText = () => {
    const url = this.props.location.pathname;
    let match;
    let searchText;
    const parsedQueryString = queryString.parse(this.props.location.search);
    if (parsedQueryString && parsedQueryString.q) {
      searchText = parsedQueryString.q;
      return searchText;
    }
    if (CATEGORY_REGEX.test(url)) {
      match = CATEGORY_CAPTURE_REGEX.exec(url)[0];
      match = match.replace(BRAND_CATEGORY_PREFIX, "");

      match = match.toUpperCase();

      searchText = `:relevance:category:${match}`;
    }

    if (BRAND_REGEX.test(url)) {
      match = BRAND_CAPTURE_REGEX.exec(url)[0];
      match = match.replace(BRAND_CATEGORY_PREFIX, "");

      match = match.toUpperCase();

      searchText = `:relevance:brand:${match}`;
    }

    return searchText;
  };

  render() {
    if (
      this.props.homeFeedData.loading ||
      this.props.homeFeedData.feedType === null
    ) {
      return this.renderLoader();
    }

    if (this.props.homeFeedData.feedType === SECONDARY_FEED_TYPE) {
      if (this.props.homeFeedData.secondaryFeed.length > 0) {
        return <BrandLandingPageContainer />;
      } else {
        return (
          <ProductListingsContainer searchText={this.getPlpSearchText()} />
        );
      }
    }
    return null;
  }
}
