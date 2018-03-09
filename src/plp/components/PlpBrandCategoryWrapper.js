import React from "react";
import ProductListingsContainer from "../containers/ProductListingsContainer.js";
import BrandLandingPageContainer from "../../brands/containers/BrandLandingPageContainer";
import throttle from "lodash/throttle";
import queryString from "query-string";
import MDSpinner from "react-md-spinner";

const CATEGORY_REGEX = /c-msh*/;
const BRAND_REGEX = /c-mbh*/;
const CAPTURE_REGEX = /c-(.*)/;
const SUFFIX = `&isTextSearch=false&isFilter=false`;
const IS_FILTER_SUFFIX = `&isFilter=true`;
const SEARCH_CATEGORY_TO_IGNORE = "all";

export default class PlpBrandCategoryWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pageType: props.location.pathname
    };
  }
  getSearchTextFromUrl() {
    const parsedQueryString = queryString.parse(this.props.location.search);
    const searchCategory = parsedQueryString.searchCategory;
    let searchText = parsedQueryString.q;
    const brandOrCategoryId = this.props.match.params.brandOrCategoryId;
    let match;
    if (CATEGORY_REGEX.test(brandOrCategoryId)) {
      match = CAPTURE_REGEX.exec(brandOrCategoryId)[1];
      match = match.toUpperCase();
      searchText = `:relevance:category:${match}`;
    }

    if (BRAND_REGEX.test(brandOrCategoryId)) {
      match = CAPTURE_REGEX.exec(brandOrCategoryId)[1];
      match = match.toUpperCase();
      searchText = `:relevance:brand:${match}`;
    }

    if (searchCategory && searchCategory !== SEARCH_CATEGORY_TO_IGNORE) {
      searchText = `:category:${searchCategory}`;
    }

    if (!searchText) {
      searchText = parsedQueryString.text;
    }
    return searchText;
  }
  componentDidMount() {
    // this will do the check for category or brand
    // which does not happen now
    window.addEventListener("scroll", this.handleScroll);

    // I can just assume that we need to set filters here.
    // const searchText = this.getSearchTextFromUrl();
    let pageType = this.state.pageType.split("/")[2];
    this.props.homeFeed(pageType);
    // if (pageType.includes("msh") || [pageType.includes("mbh")]) {
    //   this.props.homeFeed(pageType);
    // } else {
    //   this.props.getProductListings(searchText, SUFFIX, 0);
    // }
  }

  componentDidUpdate() {
    // if (
    //   this.props.location.state &&
    //   this.props.location.state.isFilter &&
    //   this.props.homeFeedData.homeFeed === null
    // ) {
    //   const suffix = "&isFilter=true";
    //   const searchText = this.getSearchTextFromUrl();
    //   this.props.getProductListings(searchText, suffix, 0, true);
    // }
    if (
      this.props.homeFeedData.feedType === "blp" &&
      this.props.homeFeedData.homeFeed.length === 0
    ) {
      const suffix = "&isFilter=false";
      const searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, suffix, 0, true);
    }
  }

  handleScroll = () => {
    const windowHeight =
      "innerHeight" in window
        ? window.innerHeight
        : document.documentElement.offsetHeight;
    const body = document.body;
    const html = document.documentElement;
    const docHeight = Math.max(
      body.scrollHeight,
      body.offsetHeight,
      html.clientHeight,
      html.scrollHeight,
      html.offsetHeight
    );
    const windowBottom = windowHeight + window.pageYOffset;
    if (windowBottom >= docHeight) {
      this.props.paginate(this.props.page + 1, SUFFIX);
      // this is where I need to  update the page
      // I do a getProductListings call, but I need to throttle it.
    }
  };

  componentWillUnmount() {
    window.removeEventListener("scroll", throttle(this.handleScroll, 300));
  }

  // from the url I construct filters

  // and execute a search

  // so this page needs a container that will supply those actions
  // getProductListings works on the search state, so I will need to update that from the url
  // then call getProductListings

  // so this thing will need setFIlters, getProductListings
  renderLoader() {
    return <MDSpinner />;
  }
  render() {
    if (this.props.homeFeedData.loading) {
      return this.renderLoader();
    }

    let isFilter = false;
    if (this.props.location.state) {
      isFilter = this.props.location.state.isFilter
        ? this.props.location.state.isFilter
        : false;
    }
    return this.props.homeFeedData.feedType === "blp" &&
      this.props.homeFeedData.homeFeed.length > 0 ? (
      <BrandLandingPageContainer />
    ) : (
      <ProductListingsContainer isFilter={isFilter} />
    );
  }
}

// Brand Page
// https://uat2.tataunistore.com/marketplacewebservices/v2/mpl/products/serpsearch?type=category&channel=mobile&pageSize=20&typeID=all&page=0&searchText=:relevance:brand:MBH12E00001&isFilter=false&isTextSearch=false&isPwa=false

// Category Page
// https://uat2.tataunistore.com/marketplacewebservices/v2/mpl/products/serpsearch?type=category&channel=mobile&pageSize=20&typeID=all&page=0&searchText=:relevance:category:MSH1012100&isFilter=false&isTextSearch=false&isPwa=false
