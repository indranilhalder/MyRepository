import React, { Component } from "react";
import FilterCatageoryDetails from "./FilterCatageoryDetails";
import FilterCatageorySubList from "./FilterCatageorySubList";
import GridSelect from "../../general/components/GridSelect";
import styles from "./FilterCatageory.css";
export default class FilterCatageory extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: false
    };
  }
  openList() {
    this.setState({ selected: !this.state.selected });
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.categoryTypeList.map((val, i) => {
          return (
            <FilterCatageoryDetails
              category={val.category}
              categoryCount={val.categoryCount}
              onClick={() => this.openList()}
              key={i}
            >
              {this.state.selected && (
                <GridSelect limit={1} offset={0} elementWidthMobile={100}>
                  {val.typeList.map((data, i) => {
                    return (
                      <FilterCatageorySubList
                        subListItem={data.subListItem}
                        key={i}
                        value={i}
                        subListCount={data.subListCount}
                      />
                    );
                  })}
                </GridSelect>
              )}
            </FilterCatageoryDetails>
          );
        })}
      </div>
    );
  }
}
