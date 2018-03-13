import React from "react";
import FilterTab from "./FilterTab";
import FilterSelect from "./FilterSelect";
import FilterCategory from "./FilterCategory";
import FilterCategoryL1 from "./FilterCategoryL1";
import styles from "./FilterMobile.css";
import queryString from "query-string";
import { createUrlFromQueryAndCategory } from "./FilterUtils.js";

export default class FilterMobile extends React.Component {
  constructor(props) {
    super(props);
    const url = `${props.location.pathname}${props.location.search}`;

    this.state = {
      showCategory: true,
      filterSelectedIndex: 0,
      url
    };
  }

  selectTab(val) {
    this.setState({ showCategory: false, filterSelectedIndex: val });
  }
  selectCategories() {
    this.setState({ showCategory: true });
  }

  onClear = () => {
    this.props.history.push(this.state.url, { isFilter: true });
  };

  onApply = () => {
    const pathName = this.props.location.pathname;
    const search = this.props.location.search;
    const url = `${pathName}${search}`;
    this.props.history.push(url, {
      isFilter: false
    });
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

  onFilterClick = val => {
    console.log("ON FILTER CLICK");
    this.props.history.push(val, { isFilter: true });
  };
  render() {
    const { facetData, facetdatacategory } = this.props;

    return (
      <div className={styles.base}>
        <div className={styles.tabHolder}>
          <div className={styles.slider}>
            <FilterTab
              name="Categories"
              onClick={() => {
                this.selectCategories();
              }}
              selected={this.state.showCategory}
            />
            {facetData.map((val, i) => {
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
                {facetData[this.state.filterSelectedIndex].values.map(
                  (val, i) => {
                    return (
                      <FilterSelect
                        onClick={this.onFilterClick}
                        selected={val.selected}
                        label={val.name}
                        url={val.url}
                      />
                    );
                  }
                )}
              </React.Fragment>
            )}
          </div>
        </div>
        <div className={styles.footer}>
          <div className={styles.buttonHolder}>
            <div className={styles.button} onClick={this.onClear}>
              Clear
            </div>
          </div>
          <div className={styles.buttonHolder}>
            <div
              className={styles.redButton}
              onClick={() => this.onApply(this.state.selected)}
            >
              Apply
            </div>
          </div>
        </div>
      </div>
    );
  }
}
