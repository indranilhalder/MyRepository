import React from "react";
import styles from "./FilterCategorySubList.css";
import PropTypes from "prop-types";
export default class FilterCategorySubList extends React.Component {
  handleClick() {
    console.log("IS FILTER CATEGORY SUB LIST CLICKED");

    if (this.props.onClick) {
      this.props.onClick(this.props.value);
    }
  }
  render() {
    let className = styles.header;
    console.log("CATEGORY SUB LIST RENDER");
    console.log(this.props);
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={this.props.selected ? styles.active : className}>
          <div className={styles.subCategoryList}>{this.props.subListItem}</div>
          <div className={styles.subCategoryListCount}>
            {this.props.subListCount}
          </div>
        </div>
      </div>
    );
  }
}
FilterCategorySubList.propTypes = {
  selectItem: PropTypes.func,
  subListItem: PropTypes.string,
  subListCount: PropTypes.number
};
