import React from "react";
import FilterWithMultiSelect from "./FilterWithMultiSelect";
import FilterSelect from "./FilterSelect";
import FilterCategories from "./FilterCategories";
import PropTypes from "prop-types";
import styles from "./Filter.css";

export default class Filter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pageNumber: 0,
      selected: this.props.filterData.map(val => {
        return val.values.map(v => {
          if (v.selected) {
            return v.value;
          } else {
            return null;
          }
        });
      })
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
  onApply = () => {
    if (this.props.onApply) {
      this.props.onApply();
    }
  };
  handleSelect(val, index) {
    let selected = this.state.selected;
    selected[index] = val;

    this.setState({ selected });
    console.log(val);
    console.log(index);
  }
  render() {
    console.log(this.state.selected);
    return (
      <div className={styles.base}>
        <div className={styles.tabs}>
          <FilterCategories
            data={this.props.filterData}
            selected={this.state.selected}
            pageNumber={this.state.pageNumber}
            onClick={val => this.switchPage(val)}
          />
        </div>
        <div className={styles.options}>
          {this.props.filterData.map((val, i) => {
            if (this.state.pageNumber === i) {
              return (
                <FilterWithMultiSelect
                  selected={this.state.selected[i]}
                  key={i}
                  onSelect={val => {
                    this.handleSelect(val, i);
                  }}
                >
                  {val.values &&
                    val.values.map((value, i) => {
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
  onApply: PropTypes.func
};
