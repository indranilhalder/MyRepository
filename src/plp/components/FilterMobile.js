import React from "react";
import FilterTab from "./FilterTab";
import FilterSelect from "./FilterSelect";
import FilterCategory from "./FilterCategory";
import FilterCategoryL1 from "./FilterCategoryL1";
import { facetData } from "./FacetData";
import { facetdatacategory } from "./State";
import styles from "./FilterMobile.css";

export default class FilterMobile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showCategory: true,
      filterSelectedIndex: 0
    };
  }
  onApply() {
    if (this.props.onApply) {
      this.props.onApply();
    }
  }
  onClear() {
    if (this.props.onClear) {
      this.props.onClear();
    }
  }
  selectTab(val) {
    this.setState({ showCategory: false, filterSelectedIndex: val });
  }
  selectCategories() {
    this.setState({ showCategory: true });
  }
  render() {
    //console.log(facetData);
    console.log(facetdatacategory);
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
                console.log(val);
                return (
                  <FilterCategoryL1
                    name={val.categoryName}
                    count={val.quantity}
                  >
                    <FilterCategory categoryTypeList={val.childFilters} />
                  </FilterCategoryL1>
                );
              })}
            {!this.state.showCategory && (
              <React.Fragment>
                {facetData[this.state.filterSelectedIndex].values.map(
                  (val, i) => {
                    return (
                      <FilterSelect selected={val.selected} label={val.name} />
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
