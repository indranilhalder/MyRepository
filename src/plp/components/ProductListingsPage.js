import React, { Component } from "react";
import MDSpinner from "react-md-spinner";
import PlpContainer from "../containers/PlpContainer";
import queryString from "query-string";
import throttle from "lodash/throttle";

const CATEGORY_REGEX = /c-msh*/;
const BRAND_REGEX = /c-mbh*/;
const CAPTURE_REGEX = /c-(.*)/;
const SEARCH_CATEGORY_TO_IGNORE = "all";
const SUFFIX = `&isTextSearch=false&isFilter=false`;

// Here I can serve a CLP/BLP
// And any number of PLP pages.

// /c-msh
// /c-mbh
// /electronics/c-msh
// /electronics/c-mbh

// better to take this from the url I think.

// so on ComponentDidMount and ComponentDidUpdate, I need to figure out which
// sort of page I am dealing with.

class ProductListingsPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showFilter: false
    };
  }
  getSearchTextFromUrl() {
    console.log("PROPS");
    console.log(this.props);
    const parsedQueryString = queryString.parse(this.props.location.search);
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
    window.addEventListener("scroll", this.handleScroll);
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

  handleScroll = () => {
    if (!this.state.showFilter) {
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
        this.props.paginate(this.props.pageNumber + 1, SUFFIX);
        // this is where I need to  update the page
        // I do a getProductListings call, but I need to throttle it.
      }
    }
  };

  componentWillUnmount() {
    window.removeEventListener("scroll", throttle(this.handleScroll, 300));
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
        onFilterClick={this.onFilterClick}
        isFilter={isFilter}
        showFilter={showFilter}
      />
    );
  }
}

export default ProductListingsPage;
