import React, { Component } from "react";
import PlpContainer from "../containers/PlpContainer";
import queryString from "query-string";
import {
  CATEGORY_PRODUCT_LISTINGS_WITH_PAGE,
  BRAND_AND_CATEGORY_PAGE,
  SKU_PAGE
} from "../../lib/constants.js";

const SEARCH_CATEGORY_TO_IGNORE = "all";
const SUFFIX = `&isTextSearch=false&isFilter=false`;
const SKU_SUFFIX = `&isFilter=false&channel=mobile`;
const PAGE_REGEX = /page-(\d)/;

class ProductListingsPage extends Component {
  getSearchTextFromUrl() {
    const parsedQueryString = queryString.parse(this.props.location.search);

    const searchCategory = parsedQueryString.searchCategory;
    let searchText = parsedQueryString.q;

    if (searchCategory && searchCategory !== SEARCH_CATEGORY_TO_IGNORE) {
      searchText = `:category:${searchCategory}`;
    }

    if (!searchText) {
      searchText = parsedQueryString.text;
    }

    return encodeURIComponent(searchText);
  }

  componentDidMount() {
    if (
      this.props.location.state &&
      this.props.location.state.disableSerpSearch === true
    ) {
      return;
    }

    if (this.props.match.path === SKU_PAGE) {
      const url = this.props.match.params[0];
      let skuId;
      if (url.indexOf("/") > -1) {
        const urlSplitBySlash = url.split("/");
        skuId = urlSplitBySlash[urlSplitBySlash.length - 1];
      } else {
        skuId = this.props.match.params[0];
      }
      const searchText = `:relevance:collectionIds:${skuId}`;
      this.props.getProductListings(searchText, SKU_SUFFIX, 0);
      return;
    }

    if (this.props.searchText) {
      this.props.getProductListings(this.props.searchText, SUFFIX, 0);
      return;
    }
    let page = null;
    if (this.props.match.path === CATEGORY_PRODUCT_LISTINGS_WITH_PAGE) {
      page = this.props.match.params[1];
      const searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, SUFFIX, page);
      return;
    }

    if (this.props.match.path === BRAND_AND_CATEGORY_PAGE) {
      const categoryId = this.props.match.params[0];
      const brandId = this.props.match.params[1];
      const searchText = `:relevance:category:${categoryId}:brand:${brandId}`;
      this.props.getProductListings(searchText, SUFFIX, 0, false);
      return;
    }

    page = 0;

    if (this.props.location.state && this.props.location.state.isFilter) {
      const suffix = "&isFilter=true";
      const searchText = this.getSearchTextFromUrl();
      const pageMatch = PAGE_REGEX.exec(this.props.location.pathname);
      if (pageMatch) {
        page = pageMatch[1] ? pageMatch[1] : 0;
      }
      this.props.getProductListings(searchText, suffix, page, true);
    } else {
      const searchText = this.getSearchTextFromUrl();
      const pageMatch = PAGE_REGEX.exec(this.props.location.pathname);
      if (pageMatch) {
        page = pageMatch[1] ? pageMatch[1] : 0;
      }
      this.props.getProductListings(searchText, SUFFIX, page);
    }
  }

  componentDidUpdate() {
    let page = null;

    if (this.props.match === SKU_PAGE) {
      const url = this.props.params[0];
      let skuId;
      if (url.indexOf("/") > -1) {
        const urlSplitBySlash = url.split("/");
        skuId = urlSplitBySlash[urlSplitBySlash.length - 1];
      } else {
        skuId = this.props.params[0];
      }
      const searchText = `searchText=:relevance:collectionIds:${skuId}`;
      this.props.getProductListings(searchText, SUFFIX, 0);
      return;
    }
    if (this.props.match.path === CATEGORY_PRODUCT_LISTINGS_WITH_PAGE) {
      page = this.props.match.params[1];
      const searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, SUFFIX, page);
      return;
    }
    if (
      this.props.location.state &&
      this.props.location.state.disableSerpSearch === true
    ) {
      return;
    }
    page = 0;
    if (
      this.props.location.state &&
      this.props.location.state.isFilter === true
    ) {
      const suffix = "&isFilter=true";
      const searchText = this.getSearchTextFromUrl();
      const pageMatch = PAGE_REGEX.exec(this.props.location.pathname);
      if (pageMatch) {
        page = pageMatch[1] ? pageMatch[1] : 0;
      }
      this.props.getProductListings(searchText, suffix, page, true);
    } else if (
      this.props.location.state &&
      this.props.location.state.isFilter === false
    ) {
      const searchText = this.getSearchTextFromUrl();
      const pageMatch = PAGE_REGEX.exec(this.props.location.pathname);
      if (pageMatch) {
        page = pageMatch[1] ? pageMatch[1] : 0;
      }
      this.props.getProductListings(searchText, SUFFIX, page);
    }
  }

  render() {
    let isFilter = false;
    if (this.props.location.state && this.props.location.state.isFilter) {
      isFilter = true;
    }

    return (
      <PlpContainer
        paginate={this.props.paginate}
        onFilterClick={this.onFilterClick}
        isFilter={isFilter}
      />
    );
  }
}

export default ProductListingsPage;
