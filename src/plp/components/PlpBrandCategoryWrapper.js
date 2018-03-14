import React from "react";
import ProductListingsContainer from "../containers/ProductListingsContainer.js";
import BrandLandingPageContainer from "../../brands/containers/BrandLandingPageContainer";
import throttle from "lodash/throttle";
import queryString from "query-string";
import { Redirect } from "react-router";
import { SEARCH_RESULTS_PAGE } from "../../lib/constants.js";
import MDSpinner from "react-md-spinner";
import { BLP_OR_CLP_FEED_TYPE } from "../../lib/constants";

export const CATEGORY_REGEX = /c-msh*/;
export const BRAND_REGEX = /c-mbh*/;
export const CAPTURE_REGEX = /c-(.*)/;

const SUFFIX = `&isTextSearch=false&isFilter=false`;
const IS_FILTER_SUFFIX = `&isFilter=true`;
const SEARCH_CATEGORY_TO_IGNORE = "all";

// If this is a BLP/CLP, hit the home feed api (which should be renamed to feed)
// If that comes back empty, then display a PLP

// I also serve the PLP at its own urls.

// So My Plan -

// First make this thing work with brand and category pages and make sure that the fall back works
// To do this what needs to happen?
// We need to check the url, get the brand or category and send it to the home feed.

// Then make sure that filter and sort are working ok.
// Then make sure back and stuff are working ok.
// Then I can do the changes I need to make for pagination, because I'll have access to whether the filter is open or not.

export default class PlpBrandCategoryWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pageType: props.location.pathname,
      redirectToPlp: false
    };
  }

  componentDidMount() {
    console.log("COMPONENT DID MOUNT");
    const categoryOrBrandId = this.props.location.pathname.match(
      CAPTURE_REGEX
    )[1];
    console.log("CATEGORY OR BRAND ID");
    console.log(categoryOrBrandId);
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

  componentDidUpdate() {}
  render() {
    if (
      this.props.homeFeedData.loading ||
      this.props.homeFeedData.feedType === null
    ) {
      return this.renderLoader();
    }

    return this.props.homeFeedData.feedType === BLP_OR_CLP_FEED_TYPE &&
      // so if this happens, what I can do is redirect to a PLP

      this.props.homeFeedData.homeFeed.length > 0 ? (
      <BrandLandingPageContainer />
    ) : (
      <Redirect to={this.getPlpUrl()} />
    );
  }
}
