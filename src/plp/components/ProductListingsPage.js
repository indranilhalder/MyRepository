import React, { Component } from "react";
import PlpContainer from "../containers/PlpContainer";
import queryString from "query-string";

const SEARCH_CATEGORY_TO_IGNORE = "all";
const SUFFIX = `&isTextSearch=false&isFilter=false`;

class ProductListingsPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showFilter: false
    };
  }
  getSearchTextFromUrl() {
    const parsedQueryString = queryString.parse(this.props.location.search);
    console.log("GET SEARCH TEXT FROM URL");
    console.log(parsedQueryString);
    const searchCategory = parsedQueryString.searchCategory;
    let searchText = parsedQueryString.q;

    if (searchCategory && searchCategory !== SEARCH_CATEGORY_TO_IGNORE) {
      searchText = `:category:${searchCategory}`;
    }

    if (!searchText) {
      searchText = parsedQueryString.text;
    }
    return searchText;
  }

  componentDidMount() {
    if (
      this.props.location.state &&
      this.props.location.state.disableSerpSearch === true
    ) {
      return;
    }
    if (this.props.location.state && this.props.location.state.isFilter) {
      const suffix = "&isFilter=true";
      const searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, suffix, 0, true);
    } else {
      const searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, SUFFIX, 0);
    }
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (nextState.filterOpen !== this.state.filterOpen) {
      return false;
    } else {
      return true;
    }
  }

  onFilterClick = val => {
    this.setState({ filterOpen: val });
  };

  componentDidUpdate() {
    if (
      this.props.location.state &&
      this.props.location.state.disableSerpSearch === true
    ) {
      return;
    }

    if (
      this.props.location.state &&
      this.props.location.state.isFilter === true
    ) {
      const suffix = "&isFilter=true";
      const searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, suffix, 0, true);
    } else if (
      this.props.location.state &&
      this.props.location.state.isFilter === false
    ) {
      const searchText = this.getSearchTextFromUrl();
      this.props.getProductListings(searchText, SUFFIX, 0);
    }
  }

  render() {
    let isFilter = false;
    let showFilter = false;
    if (this.props.location.state && this.props.location.state.isFilter) {
      isFilter = true;
      showFilter = true;
    }

    if (this.props.location.state && !this.props.location.state.isFilter) {
      showFilter = false;
    }
    return (
      <PlpContainer
        paginate={this.props.paginate}
        onFilterClick={this.onFilterClick}
        isFilter={isFilter}
        showFilter={showFilter}
      />
    );
  }
}

export default ProductListingsPage;
