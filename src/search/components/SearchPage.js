import React from "react";
import styles from "./SearchPage.css";
import SearchHeader from "./SearchHeader";
import SearchResultItem from "./SearchResultItem";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
export default class SearchPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showResults: false,
      showSearchBar: false,
      searchString: null
    };
  }

  onSearchOrCloseIconClick = () => {
    const showResults = this.state.showResults;
    this.props.clearSearchResults();
    this.setState({
      showResults: !showResults,
      searchString: null,
      showSearchBar: !this.state.showSearchBar
    });
  };

  handleBrandClick(webURL) {
    const brandCode = `${webURL}`.replace(TATA_CLIQ_ROOT, "$1");
    const searchQuery = this.state.searchString;
    this.props.clearSearchResults();
    this.setState({
      showResults: false,
      searchString: null,
      showSearchBar: false
    });
    const url = `/search/?searchCategory=all&text=${searchQuery}:relevance:brand:${brandCode}`;
    this.props.history.push(url, {
      isFilter: false
    });
  }

  handleCategoryClick(webURL) {
    const categoryCode = `${webURL}`.replace(TATA_CLIQ_ROOT, "$1");
    const searchQuery = this.state.searchString;
    const url = `/search/?searchCategory=all&text=${searchQuery}:relevance:category:${categoryCode}`;
    this.props.clearSearchResults();
    this.setState({
      showResults: false,
      searchString: null,
      showSearchBar: false
    });

    this.props.history.push(url, {
      isFilter: false
    });
  }
  handleSearch(val, e) {
    if (this.props.getSearchResults) {
      this.setState({ searchString: val });
      this.props.getSearchResults(val);
    }
  }
  handleBackClick() {
    if (this.props.canGoBack) {
      this.props.canGoBack();
    }
  }
  handleOnSearchString(searchString) {
    this.props.history.push(
      `/search/?searchCategory=all&text=${searchString}`,
      {
        isFilter: false
      }
    );
    this.props.clearSearchResults();
    this.setState({ showResults: false, searchString, showSearchBar: false });
  }
  render() {
    const data = this.props.searchResult;
    return (
      <div className={styles.base}>
        <div className={styles.searchBar}>
          <SearchHeader
            onSearchOrCloseIconClick={this.onSearchOrCloseIconClick}
            onSearch={val => this.handleSearch(val)}
            onClickBack={() => {
              this.handleBackClick();
            }}
            isGoBack={this.props.hasBackButton}
            text={this.props.header}
            isLogo={this.props.isLogo}
            hasCrossButton={this.props.hasCrossButton}
            toggleSearchBar={this.toggleSearchBar}
            display={this.state.showSearchBar}
            onSearchString={val => this.handleOnSearchString(val)}
            searchString={this.state.searchString}
          />
        </div>
        {this.state.showResults && (
          <div className={styles.searchResults}>
            {data &&
              data.topBrands.map((val, i) => {
                return (
                  <SearchResultItem
                    key={i}
                    suggestedText={data.suggestionText[0]}
                    text={val.categoryName}
                    value={val.categoryCode}
                    onClick={() => {
                      this.handleBrandClick(val.categoryCode);
                    }}
                  />
                );
              })}
            {data &&
              data.topCategories.map((val, i) => {
                return (
                  <SearchResultItem
                    key={i}
                    suggestedText={data.suggestionText[0]}
                    text={val.categoryName}
                    value={val.categoryCode}
                    onClick={() => {
                      this.handleCategoryClick(val.categoryCode);
                    }}
                  />
                );
              })}
          </div>
        )}
      </div>
    );
  }
}
