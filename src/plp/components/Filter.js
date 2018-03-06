import React from "react";
import FilterSection from "./FilterSection";
import FilterSelect from "./FilterSelect";
import FilterCategories from "./FilterCategories";
import FilterCategorySection from "./FilterCategorySection";
import PropTypes from "prop-types";
import styles from "./Filter.css";
import map from "lodash/map";
import compact from "lodash/compact";
import InformationHeader from "../../general/components/InformationHeader";
import queryString from "query-string";

const FILTER_HEADER = "Refine by";
export default class Filter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pageNumber: 0
    };
  }
  switchPage = val => {
    this.setState({ pageNumber: val });
  };
  onClear = () => {
    if (this.props.onClear) {
      this.props.onClear();
    }
    this.setState({ selected: [[null]] });
  };
  handleBackClick = () => {
    if (this.props.backPage) {
      this.props.backPage();
    }
  };
  handleSelect(val) {
    this.props.history.push(val, { isFilter: true });
  }
  onApply() {
    console.log("ON APPLY IS CALLED");
    console.log(this.props);
    const pathName = this.props.location.pathname;
    const search = this.props.location.search;
    const url = `${pathName}${search}`;
    this.props.history.push(search, {
      isFilter: false
    });
  }
  onCategorySelect = val => {
    const parsedQueryString = queryString.parse(this.props.location.search);
    const CATEGORY_REGEX = /:category:(\w+):/;
    let match;
    if (CATEGORY_REGEX.test(parsedQueryString.q)) {
      match = parsedQueryString.q.replace(CATEGORY_REGEX, `:category:${val}`);
    }

    this.props.history.push(this.props.location, {
      q: parsedQueryString,
      isFilter: true
    });
  };
  // handleSelect(val, index) {
  //   let selected = this.state.selected;
  //   selected[index] = val;
  //   this.setState({ selected });
  // }

  render() {
    const filterDatum = this.props.filterData[this.state.pageNumber];
    return (
      <div className={styles.base}>
        <div className={styles.pageHeader}>
          <InformationHeader
            onClick={this.handleBackClick}
            text={FILTER_HEADER}
          />
        </div>
        <div className={styles.filterData}>
          <div className={styles.tabs}>
            <FilterCategories
              data={this.props.filterData}
              selected={this.state.selected}
              pageNumber={this.state.pageNumber}
              onClick={val => this.switchPage(val)}
              hasCategory={false}
            />
          </div>
          <div className={styles.options}>
            {filterDatum.key !== "category" && (
              <FilterSection
                onSelect={val => {
                  this.handleSelect(val, this.state.pageNumber);
                }}
              >
                {filterDatum.values &&
                  filterDatum.values.map((value, i) => {
                    return (
                      <FilterSelect
                        label={value.name}
                        value={value.value}
                        count={value.count}
                        url={value.url}
                        selected={value.selected}
                        key={i}
                        selectItem={val => this.handleSelect(val)}
                      />
                    );
                  })}
              </FilterSection>
            )}
            {filterDatum.name === "Category" && (
              <FilterCategorySection
                categoryTypeList={filterDatum.filters}
                onCategorySelect={this.onCategorySelect}
              />
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
Filter.propTypes = {
  filterData: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string,
      value: PropTypes.string,
      count: PropTypes.number,
      selected: PropTypes.bool
    })
  ),
  onClear: PropTypes.func,
  onApply: PropTypes.func,
  backPage: PropTypes.func
};
