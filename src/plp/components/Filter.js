import React from "react";
import FilterWithMultiSelect from "./FilterWithMultiSelect";
import FilterSelect from "./FilterSelect";
import FilterCategories from "./FilterCategories";
import styles from "./Filter.css";

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
  };
  onApply = () => {
    if (this.props.onApply) {
      this.props.onApply();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.tabs}>
          <FilterCategories
            data={this.props.facetData}
            pageNumber={this.state.pageNumber}
            onClick={val => this.switchPage(val)}
          />
        </div>
        <div className={styles.options}>
          {this.props.facetData.map((datum, i) => {
            if (this.state.pageNumber === i) {
              return (
                <FilterWithMultiSelect>
                  {datum.values &&
                    datum.values.map((value, i) => {
                      return (
                        <FilterSelect
                          label={value.name}
                          value={value.value}
                          count={value.count}
                          key={i}
                        />
                      );
                    })}
                </FilterWithMultiSelect>
              );
            } else {
              return null;
            }
          })}
        </div>
        <div className={styles.footer}>
          <div className={styles.buttonHolder}>
            <div className={styles.button} onClick={this.onClear}>
              Clear
            </div>
          </div>
          <div className={styles.buttonHolder}>
            <div className={styles.redButton} onClick={this.onApply}>
              Apply
            </div>
          </div>
        </div>
      </div>
    );
  }
}
