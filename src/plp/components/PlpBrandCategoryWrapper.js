import React from "react";
import ProductListingsContainer from "../containers/ProductListingsContainer.js";
import throttle from "lodash/throttle";
import queryString from "query-string";
import { Redirect } from "react-router";
import { SEARCH_RESULTS_PAGE } from "../../lib/constants.js";

export const CATEGORY_REGEX = /c-msh*/;
export const BRAND_REGEX = /c-mbh*/;
export const CAPTURE_REGEX = /c-(.*)/;
const SUFFIX = `&isTextSearch=false&isFilter=false`;
const IS_FILTER_SUFFIX = `&isFilter=true`;
const SEARCH_CATEGORY_TO_IGNORE = "all";

// here is where we decide whether to display a BLP, CLP or ProductListingsPage

// in ProductListings is where we will get ProductListings and such.

export default class PlpBrandCategoryWrapper extends React.Component {
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
    // this will do the check for category or brand\

    window.addEventListener("scroll", this.handleScroll);

    const searchText = this.getSearchTextFromUrl();

    // I can just assume that we need to set filters here.
    this.props.getProductListings(searchText, SUFFIX, 0);
  }

  componentDidUpdate() {
    if (
      this.props.location.state &&
      this.props.location.state.disableSerpSearch === true
    ) {
      return;
    }
    if (this.props.page === 0) {
      if (this.props.location.state && this.props.location.state.isFilter) {
        console.log("IS THE RIGHT THING BEING HIT");
        const suffix = "&isFilter=true";
        const searchText = this.getSearchTextFromUrl();
        this.props.getProductListings(searchText, suffix, 0, true);
      } else {
        const searchText = this.getSearchTextFromUrl();
        this.props.getProductListings(searchText, SUFFIX, 0);
      }
    }
  }

  handleScroll = () => {
    console.log("HANDLE SCROLL");
    console.log(this.props.modalDisplayed);
    if (this.props.modalDisplayed) {
      return;
    }
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

  render() {
    let isFilter = false;
    if (this.props.location.state) {
      isFilter = this.props.location.state.isFilter
        ? this.props.location.state.isFilter
        : false;
    }
    if (this.props.location.pathname === SEARCH_RESULTS_PAGE) {
      const parsedQueryString = queryString.parse(this.props.location.search);
      const searchCategory = parsedQueryString.searchCategory;
      const searchText = parsedQueryString.text;
      if (searchCategory && searchText) {
        const url = `/search/?q=${searchText}:relevance:${SUFFIX}`;
        return <Redirect to={url} />;
      }
    }
    return <ProductListingsContainer isFilter={isFilter} />;
  }
}

// Brand Page
// https://uat2.tataunistore.com/marketplacewebservices/v2/mpl/products/serpsearch?type=category&channel=mobile&pageSize=20&typeID=all&page=0&searchText=:relevance:brand:MBH12E00001&isFilter=false&isTextSearch=false&isPwa=false

// Category Page
// https://uat2.tataunistore.com/marketplacewebservices/v2/mpl/products/serpsearch?type=category&channel=mobile&pageSize=20&typeID=all&page=0&searchText=:relevance:category:MSH1012100&isFilter=false&isTextSearch=false&isPwa=false
