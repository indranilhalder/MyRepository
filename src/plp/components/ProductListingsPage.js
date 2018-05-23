import React, { Component } from "react";
import PlpContainer from "../containers/PlpContainer";
import queryString from "query-string";
import {
  CATEGORY_PRODUCT_LISTINGS_WITH_PAGE,
  BRAND_AND_CATEGORY_PAGE,
  SKU_PAGE,
  CATEGORY_PAGE_WITH_SLUG,
  BRAND_PRODUCT_LISTINGS_WITH_PAGE,
  BRAND_PAGE_WITH_SLUG
} from "../../lib/constants.js";
import delay from "lodash.delay";
import { getSearchTextFromUrlForCategoryProductListings } from "./SortUtils";
import { BRAND_REGEX, CATEGORY_REGEX } from "../../home/actions/home.actions";
const SEARCH_CATEGORY_TO_IGNORE = "all";
const SUFFIX = `&isTextSearch=false&isFilter=false`;
const SKU_SUFFIX = `&isFilter=false&channel=mobile`;
const PAGE_REGEX = /page-(\d+)/;
const MAX_PRICE_FROM_API = "and Above";
const MAX_PRICE_FROM_UI = "-₹9,999,999";

class ProductListingsPage extends Component {
  getSearchTextFromUrl() {
    const parsedQueryString = queryString.parse(this.props.location.search);

    const searchCategory = parsedQueryString.searchCategory;
    let searchText = parsedQueryString.q;

    if (
      searchCategory &&
      searchCategory !== "" &&
      searchCategory !== SEARCH_CATEGORY_TO_IGNORE
    ) {
      if (searchCategory === BRAND_REGEX) {
        searchText = `:brand:${searchCategory}`;
      } else if (searchCategory === CATEGORY_REGEX) {
        searchText = `:category:${searchCategory}`;
      }
    }

    if (!searchText) {
      searchText = parsedQueryString.text;
    }

    if (
      this.props.match.path === CATEGORY_PRODUCT_LISTINGS_WITH_PAGE ||
      this.props.match.path === CATEGORY_PAGE_WITH_SLUG
    ) {
      searchText = getSearchTextFromUrlForCategoryProductListings(
        searchText,
        this.props.match.params[0]
      );
    }
    if (
      this.props.match.path === BRAND_PRODUCT_LISTINGS_WITH_PAGE ||
      this.props.match.path === BRAND_PAGE_WITH_SLUG
    ) {
      if (searchText) {
        searchText = searchText.replace(
          ":relevance",
          `:relevance:brand:${this.props.match.params[0].toUpperCase()}`
        );
      } else {
        searchText = `:relevance:brand:${this.props.match.params[0].toUpperCase()}`;
      }
    }
    if (searchText) {
      searchText = searchText.replace("+", " ");
      searchText = searchText.replace(MAX_PRICE_FROM_API, MAX_PRICE_FROM_UI);
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

    if (this.props.isGoBackFromPdpPage) {
      if (this.props.clickedProductModuleRef) {
        const clickedElement = document.getElementById(
          this.props.clickedProductModuleRef
        );
        if (clickedElement) {
          delay(() => clickedElement.scrollIntoView(true), 50);
        }
      }
      return;
    }

    if (this.props.match.path === SKU_PAGE) {
      const skuId = this.props.match.params.slug;
      let searchText = `:relevance:collectionIds:${skuId}`;
      this.props.getProductListings(searchText, SKU_SUFFIX, 0);
      return;
    }

    if (this.props.searchText) {
      let searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, SUFFIX, 0);
      return;
    }
    let page = null;

    if (
      this.props.match.path === CATEGORY_PRODUCT_LISTINGS_WITH_PAGE ||
      this.props.match.path === CATEGORY_PAGE_WITH_SLUG
    ) {
      page = this.props.match.params[1];

      let searchText = this.getSearchTextFromUrl();

      this.props.getProductListings(searchText, SUFFIX, page - 1);
      return;
    }
    if (
      this.props.match.path === BRAND_PRODUCT_LISTINGS_WITH_PAGE ||
      this.props.match.path === BRAND_PAGE_WITH_SLUG
    ) {
      page = this.props.match.params[1];

      let searchText = this.getSearchTextFromUrl();

      this.props.getProductListings(searchText, SUFFIX, page - 1);
      return;
    }

    if (this.props.match.path === BRAND_AND_CATEGORY_PAGE) {
      const categoryId = this.props.match.params[0].toUpperCase();
      const brandId = this.props.match.params[1].toUpperCase();

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
        page = pageMatch[1] ? pageMatch[1] : 1;
        page = page - 1;
      }
      this.props.getProductListings(searchText, suffix, page, true);
    } else {
      const searchText = this.getSearchTextFromUrl();
      const pageMatch = PAGE_REGEX.exec(this.props.location.pathname);
      if (pageMatch) {
        page = pageMatch[1] ? pageMatch[1] : 1;
        page = page - 1;
      }

      this.props.getProductListings(searchText, SUFFIX, page);
    }

    if (!this.props.location.state) {
      const searchText = this.getSearchTextFromUrl();
      const pageMatch = PAGE_REGEX.exec(this.props.location.pathname);
      if (pageMatch) {
        page = pageMatch[1] ? pageMatch[1] : 1;
        page = page - 1;
      }
      this.props.getProductListings(searchText, SUFFIX, page);
    }
  }

  componentDidUpdate() {
    let page = null;
    if (this.props.isGoBackFromPdpPage) {
      if (this.props.clickedProductModuleRef) {
        const clickedElement = document.getElementById(
          this.props.clickedProductModuleRef
        );
        if (clickedElement) {
          delay(() => clickedElement.scrollIntoView(), 50);
        }
      }
      return;
    }

    if (this.props.match.path === SKU_PAGE) {
      const skuId = this.props.match.params.slug;
      const searchText = `:relevance:collectionIds:${skuId}`;
      this.props.getProductListings(searchText, SKU_SUFFIX, 0);
      return;
    }
    if (this.props.match.path === CATEGORY_PRODUCT_LISTINGS_WITH_PAGE) {
      page = this.props.match.params[1];
      page = page - 1;

      const searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, SUFFIX, page);
      return;
    }

    if (this.props.match.path === BRAND_AND_CATEGORY_PAGE) {
      const categoryId = this.props.match.params[0].toUpperCase();
      const brandId = this.props.match.params[1].toUpperCase();

      const searchText = `:relevance:category:${categoryId}:brand:${brandId}`;
      this.props.getProductListings(searchText, SUFFIX, 0, false);
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
        page = pageMatch[1] ? pageMatch[1] : 1;
        page = page - 1;
      }
      this.props.getProductListings(searchText, suffix, page, true);
    } else if (
      this.props.location.state &&
      this.props.location.state.isFilter === false
    ) {
      const searchText = this.getSearchTextFromUrl();
      const pageMatch = PAGE_REGEX.exec(this.props.location.pathname);
      if (pageMatch) {
        page = pageMatch[1] ? pageMatch[1] : 1;
        page = page - 1;
      }
      this.props.getProductListings(searchText, SUFFIX, page);
    }

    if (!this.props.location.state) {
      const searchText = this.getSearchTextFromUrl();
      const pageMatch = PAGE_REGEX.exec(this.props.location.pathname);
      if (pageMatch) {
        page = pageMatch[1] ? pageMatch[1] : 1;
        page = page - 1;
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
