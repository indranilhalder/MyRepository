import React from "react";
import FilterTab from "./FilterTab";
import FilterSelect from "./FilterSelect";
import FilterCategory from "./FilterCategory";
import FilterCategoryL1 from "./FilterCategoryL1";
import SearchInput from "../../general/components/SearchInput";
import styles from "./FilterMobile.css";
import queryString from "query-string";
import { createUrlFromQueryAndCategory } from "./FilterUtils.js";

const FILTER_HEADER = "Refine by";
const BRAND = "brand";
export default class FilterMobile extends React.Component {
  constructor(props) {
    super(props);
    const url = `${props.location.pathname}${props.location.search}`;
    this.state = {
      showCategory: true,
      url,
      brandSearchString: "",
      filterSelectedIndex: 0
    };
  }
  selectTab(val) {
    this.setState({ showCategory: false, filterSelectedIndex: val });
    this.setState({ brandSearchString: "" });
  }
  selectCategories() {
    this.setState({ showCategory: true });
    this.setState({ brandSearchString: "" });
  }

  onClear = () => {
    this.props.history.push(this.state.url, { isFilter: false });
  };

  onApply = () => {
    const pathName = this.props.location.pathname;
    const search = this.props.location.search;
    const url = `${pathName}${search}`;
    this.props.history.push(url, {
      isFilter: false
    });
  };
  onBrandSearch = val => {
    this.setState({ brandSearchString: val });
  };
  onCategorySelect = (val, isFilter) => {
    const parsedQueryString = queryString.parse(this.props.location.search);
    let query = parsedQueryString.q;
    const pathName = this.props.location.pathname;
    const url = createUrlFromQueryAndCategory(query, pathName, val);
    this.props.history.push(url, { isFilter });
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

  handleBackClick = () => {
    if (this.props.backPage) {
      this.props.backPage();
    }
  };

  onFilterClick = val => {
    this.props.history.push(val, { isFilter: true });
  };
  render() {
    const { facetData, facetdatacategory } = this.props;
    let filteredFacetData = facetData[this.state.filterSelectedIndex].values;
    if (facetData[this.state.filterSelectedIndex].key === BRAND) {
      filteredFacetData = facetData[
        this.state.filterSelectedIndex
      ].values.filter(val => {
        return this.state.brandSearchString === ""
          ? val
          : val.name
              .toLowerCase()
              .includes(this.state.brandSearchString.toLowerCase());
      });
    }
    return (
      <React.Fragment>
        <div
          className={this.props.showFilter ? styles.filterOpen : styles.base}
        >
          <div className={styles.pageHeader} />
          <div className={styles.tabHolder}>
            <div className={styles.slider}>
              <FilterTab
                name="Categories"
                onClick={() => {
                  this.selectCategories();
                }}
                selected={this.state.showCategory}
              />
              {facetData &&
                facetData.map((val, i) => {
                  return (
                    <FilterTab
                      name={val.name}
                      selectedFilterCount={val.selectedFilterCount}
                      selected={
                        i === this.state.filterSelectedIndex &&
                        !this.state.showCategory
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
              {this.state.showCategory &&
                facetdatacategory &&
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
              {!this.state.showCategory && (
                <React.Fragment>
                  {facetData[this.state.filterSelectedIndex].key === BRAND && (
                    <div className={styles.search}>
                      <SearchInput
                        placeholder="Search brands"
                        onChange={val => this.onBrandSearch(val)}
                      />
                    </div>
                  )}
                  {filteredFacetData.map((val, i) => {
                    return (
                      <FilterSelect
                        onClick={this.onFilterClick}
                        selected={val.selected}
                        hexColor={val.hexColor}
                        label={val.name}
                        url={val.url}
                      />
                    );
                  })}
                </React.Fragment>
              )}
            </div>
          </div>
        </div>
        <div
          className={this.props.showFilter ? styles.footerOpen : styles.footer}
        >
          <div className={styles.buttonHolder}>
            <div className={styles.button} onClick={() => this.onClear()}>
              Clear
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
