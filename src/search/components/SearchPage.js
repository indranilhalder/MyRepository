import React from "react";
import styles from "./SearchPage.css";
import SearchHeader from "./SearchHeader";
import SearchResultItem from "./SearchResultItem";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
export default class SearchPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showResults: false
    };
  }

  handleSearchClick(val) {
    this.setState({ showResults: val });
  }

  handleCategoryClick(webURL) {
    const urlSuffix = `c-${webURL.toLowerCase()}`.replace(TATA_CLIQ_ROOT, "");
    this.props.history.push(urlSuffix);
  }
  handleSearch(val) {
    if (this.props.getSearchResults) {
      this.props.getSearchResults(val);
    }
  }
  handleBackClick() {
    if (this.props.onBack) {
      this.props.onBack();
    }
  }
  render() {
    const data = this.props.searchResult;
    return (
      <div className={styles.base}>
        <div className={styles.searchBar}>
          <SearchHeader
            onSearchClick={val => {
              this.handleSearchClick(val);
            }}
            onSearch={val => this.handleSearch(val)}
            onClickBack={() => {
              this.handleBackClick();
            }}
            text={this.props.header}
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
                      this.handleCategoryClick(val.categoryCode);
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
