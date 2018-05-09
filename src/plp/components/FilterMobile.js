import React from "react";
import FilterTab from "./FilterTab";
import FilterSelect from "./FilterSelect";
import FilterCategory from "./FilterCategory";
import FilterCategoryL1 from "./FilterCategoryL1";
import SearchInput from "../../general/components/SearchInput";
import styles from "./FilterMobile.css";
import queryString from "query-string";
import { createUrlFromQueryAndCategory } from "./FilterUtils.js";
import {
  CATEGORY_CAPTURE_REGEX,
  CATEGORY_REGEX
} from "../../plp/components/PlpBrandCategoryWrapper";

const BRAND = "brand";
export default class FilterMobile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      brandSearchString: ""
    };
  }

  onClear = () => {
    const parsedQueryString = queryString.parse(this.props.location.search);
    const query = parsedQueryString.q;

    if (query) {
      // I know that there is a query.
      // if the first char is a :, then we know this is a brand or a category that we are dealing with

      // if it is not, then we know that it's a search string.

      const firstChar = query.charAt(0);
      if (firstChar !== ":") {
        const splitQuery = query.split(":");
        const searchText = splitQuery[0];
        const url = `${this.props.location.pathname}?q=${searchText}`;

        this.props.history.push(url, {
          isFilter: false
        });
      } else {
        let brandOrCategoryId = null;
        brandOrCategoryId = /category:([a-zA-Z0-9]+)/.exec(query);
        if (!brandOrCategoryId) {
          brandOrCategoryId = /brand:([a-zA-Z0-9]+)/.exec(query);
        }

        if (brandOrCategoryId) {
          const brandOrCategoryIdIndex = brandOrCategoryId.index;
          const clearedQuery = query.substring(
            0,
            brandOrCategoryIdIndex + brandOrCategoryId[0].length
          );

          const url = `${this.props.location.pathname}?q=${clearedQuery}`;
          this.props.history.push(url, {
            isFilter: false
          });
        }
      }
    }
    if (this.props.onClear) {
      this.props.onClear();
    }
  };

  selectTab(val) {
    this.props.setFilterSelectedData(false, val);
    this.setState({ brandSearchString: "" });
  }
  selectCategories() {
    this.props.resetFilterSelectedData();
    this.setState({ brandSearchString: "" });
  }

  onApply = () => {
    this.props.onApply();
  };
  onBrandSearch = val => {
    this.setState({ brandSearchString: val });
  };
  onCategorySelect = (val, isFilter) => {
    const parsedQueryString = queryString.parse(this.props.location.search);
    // special case the search category case
    let url;
    let query = parsedQueryString.q;
    let pathName = this.props.location.pathname;

    if (parsedQueryString.searchCategory) {
      const searchValue = this.props.location.search;
      url = `${pathName}${searchValue}`;
      url = createUrlFromQueryAndCategory(searchValue, url, val);
    } else {
      url = createUrlFromQueryAndCategory(query, pathName, val);
    }

    this.props.history.push(url, { isFilter });
    if (isFilter === false) {
      this.props.onL3CategorySelect();
    }
  };

  onL1Click = val => {
    this.onCategorySelect(val, true);
  };

  onL2Click = val => {
    this.onCategorySelect(val, true);
  };

  onL3Click = val => {
    this.onCategorySelect(val, false);
  };

  onFilterClick = val => {
    const url = val.replace("{pageNo}", 1);

    this.props.history.push(url, { isFilter: true });
  };
  render() {
    const { facetData, facetdatacategory } = this.props;
    let filteredFacetData = null;
    if (facetData) {
      filteredFacetData = facetData[this.props.filterSelectedIndex].values;
      if (
        facetData &&
        facetData[this.props.filterSelectedIndex].key === BRAND
      ) {
        filteredFacetData = facetData[
          this.props.filterSelectedIndex
        ].values.filter(val => {
          return this.state.brandSearchString === ""
            ? val
            : val.name
                .toLowerCase()
                .includes(this.state.brandSearchString.toLowerCase());
        });
      }
    }
    const url = this.props.location.pathname;
    let categoryId = null;
    if (CATEGORY_REGEX.test(url)) {
      categoryId = url.match(CATEGORY_CAPTURE_REGEX)[0];
    }

    return (
      <React.Fragment>
        <div
          className={this.props.isFilterOpen ? styles.filterOpen : styles.base}
        >
          <div className={styles.pageHeader} />
          <div className={styles.tabHolder}>
            <div className={styles.slider}>
              {this.props.facetdatacategory && (
                <FilterTab
                  name="Categories"
                  onClick={() => {
                    this.selectCategories();
                  }}
                  selected={this.props.isCategorySelected}
                />
              )}

              {facetData &&
                facetData.map((val, i) => {
                  return (
                    <FilterTab
                      name={val.name}
                      selectedFilterCount={val.selectedFilterCount}
                      selected={
                        i === this.props.filterSelectedIndex &&
                        !this.props.isCategorySelected
                      }
                      onClick={() => {
                        this.selectTab(i);
                      }}
                    />
                  );
                })}
            </div>
          </div>
          <div className={styles.contenHolder}>
            <div className={styles.slider}>
              {this.props.isCategorySelected &&
                facetdatacategory &&
                facetdatacategory.filters &&
                facetdatacategory.filters.map((val, i) => {
                  return (
                    <FilterCategoryL1
                      name={val.categoryName}
                      count={val.quantity}
                      value={val.categoryCode}
                      onClick={this.onL1Click}
                      isOpen={val.selected}
                    >
                      <FilterCategory
                        onClick={this.onL2Click}
                        onL3Click={this.onL3Click}
                        categoryTypeList={val.childFilters}
                      />
                    </FilterCategoryL1>
                  );
                })}
              {!this.props.isCategorySelected && (
                <React.Fragment>
                  {facetData[this.props.filterSelectedIndex].key === BRAND && (
                    <div className={styles.search}>
                      <SearchInput
                        placeholder="Search by brands"
                        onChange={val => this.onBrandSearch(val)}
                      />
                    </div>
                  )}
                  {filteredFacetData &&
                    filteredFacetData.map((val, i) => {
                      return (
                        <FilterSelect
                          onClick={this.onFilterClick}
                          selected={val.selected}
                          hexColor={val.hexColor}
                          label={val.name}
                          count={val.count}
                          url={val.url}
                          value={val.value}
                          isBrand={
                            facetData[this.props.filterSelectedIndex].key ===
                            BRAND
                          }
                          categoryId={categoryId}
                          history={this.props.history}
                        />
                      );
                    })}
                </React.Fragment>
              )}
            </div>
          </div>
        </div>
        <div
          className={
            this.props.isFilterOpen ? styles.footerOpen : styles.footer
          }
        >
          <div className={styles.buttonHolder}>
            <div className={styles.button} onClick={this.onClear}>
              Reset
            </div>
          </div>
          <div className={styles.buttonHolder}>
            <div className={styles.redButton} onClick={() => this.onApply()}>
              Apply
            </div>
          </div>
        </div>
      </React.Fragment>
    );
  }
}
