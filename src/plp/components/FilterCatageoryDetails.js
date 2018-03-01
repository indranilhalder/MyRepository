import React from "react";
import styles from "./FilterCatageoryDetails.css";
import PropTypes from "prop-types";
import FilterCatageorySubList from "./FilterCatageorySubList";
export default class FilterCatageoryDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }
  handleClick() {
    this.setState({ isOpen: !this.state.isOpen }, () => {
      this.props.onClick();
    });
  }
  render() {
    let ClassName = styles.subCategoryRegular;
    if (this.state.isOpen) {
      ClassName = styles.subCategoryBold;
    }
    return (
      <div className={styles.base}>
        <div className={styles.header} onClick={() => this.handleClick()}>
          <div className={ClassName}>{this.props.category}</div>
          <div className={styles.subCategoryCount}>
            {this.props.categoryCount}
          </div>
        </div>
        {this.props.children && (
          <div className={styles.subListHolder}>{this.props.children}</div>
        )}
      </div>
    );
  }
}
