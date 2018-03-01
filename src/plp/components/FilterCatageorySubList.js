import React from "react";
import styles from "./FilterCatageorySubList.css";
import PropTypes from "prop-types";
export default class FilterCatageorySubList extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let className = styles.header;
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
