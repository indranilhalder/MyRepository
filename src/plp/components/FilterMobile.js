import React from "react";
import FilterTab from "./FilterTab";
import FilterSelect from "./FilterSelect";
import FilterCategory from "./FilterCategory";
import FilterCategoryL1 from "./FilterCategoryL1";
// import { facetData } from "./FacetData";
// import { facetdatacategory } from "./State";
import styles from "./FilterMobile.css";
import queryString from "query-string";

const CATEGORY_CAPTURE_REGEX = /:category:(.*)/;
const QUERY_REGEX = /(q=)(.*?)(&)/;

export default class FilterMobile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showCategory: true,
      filterSelectedIndex: 0
    };
  }
  selectTab(val) {
    this.setState({ showCategory: false, filterSelectedIndex: val });
  }
  selectCategories() {
    this.setState({ showCategory: true });
  }

  onCategorySelect = (val, disableSerpSearch) => {
    const parsedQueryString = queryString.parse(this.props.location.search);
    let query = parsedQueryString.q;
    if (CATEGORY_CAPTURE_REGEX.test(query)) {
      query = query.replace(
        CATEGORY_CAPTURE_REGEX,
        `:category:${val.toLowerCase()}`
      );
    } else {
      // there is no category, so safe to append
      query = `${query}:category:${val.toLowerCase()}`;
    }
    const newUrl = `${
      this.props.location.pathname
    }${this.props.location.search.replace(QUERY_REGEX, `$1${query}$3`)}`;

    this.props.history.push(newUrl, {
      disableSerpSearch
    });
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
    this.props.history.push(val, { isFilter: true });
  };
  render() {
    //console.log(facetData);
    const { facetData, facetdatacategory } = this.props;
    // console.log(facetData[this.state.filterSelectedIndex].values);
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
                        onClick={this.onL3Click}
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
      </div>
    );
  }
}
