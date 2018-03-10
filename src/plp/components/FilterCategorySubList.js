import React from "react";
import styles from "./FilterCategorySubList.css";
import PropTypes from "prop-types";
export default class FilterCategorySubList extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={this.props.selected ? styles.active : styles.header}>
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
