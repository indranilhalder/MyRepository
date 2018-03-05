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
const FILTER_HEADER = "Refine by";
export default class Filter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pageNumber: 0
      // selected: this.props.filterData.map(val => {
      //   return val.values.map(v => {
      //     if (v.selected) {
      //       return v.value;
      //     } else {
      //       return null;
      //     }
      //   });
      // })
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
  onApply(val) {
    if (this.props.onApply) {
      const filters = map(this.state.selected, (selectedArr, i) => {
        const key = this.props.filterData[i].key;
        const compactedSelectedArr = compact(selectedArr);
        const selectedArrObject = {
          key,
          filters: compactedSelectedArr
        };
        return selectedArrObject;
      });

      this.props.onApply(filters);
    }
  }
  onCategorySelect(val) {
    console.log(val);
  }
  // handleSelect(val, index) {
  //   let selected = this.state.selected;
  //   selected[index] = val;
  //   this.setState({ selected });
  // }

  render() {
    let hasCategory = false;
    console.log("category data");
    console.log(this.props.categoryData);
    console.log("facet data");
    console.log(this.props.filterData);
    if (this.props.categoryData) {
      console.log(this.props.categoryData.category);
      if (this.props.categoryData.category) {
        hasCategory = true;
      }
    }
    // If state.pageNumber is 0, then we need to display the category section
    //
    console.log("FILTER");

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
              hasCategory={hasCategory}
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
            {filterDatum.key === "category" &&
              this.props.categoryData && (
                <FilterCategorySection
                  categoryTypeList={this.props.categoryData.filters}
                  onCategorySelect={val => this.onCategorySelect(val)}
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
