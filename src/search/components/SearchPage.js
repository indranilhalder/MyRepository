import React from "react";
import styles from "./SearchPage.css";
import BrandHeader from "../../brands/components/BrandHeader";
import SearchResultItem from "./SearchResultItem";

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
  handleItemClick(val) {
    if (this.props.onItemClick) {
      this.props.onItemClick(val);
    }
  }
  handleSearch(val) {
    if (this.props.getSearchResults) {
      this.props.getSearchResults(val);
    }
  }
  render() {
    const data = this.props.searchResult;
    return (
      <div className={styles.base}>
        <div className={styles.searchBar}>
          <BrandHeader
            onSearchClick={val => {
              this.handleSearchClick(val);
            }}
            onSearch={val => this.handleSearch(val)}
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
                      this.handleItemClick(val.categoryCode);
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
                      this.handleItemClick(val.categoryCode);
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
