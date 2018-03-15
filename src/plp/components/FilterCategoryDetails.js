import React from "react";
import styles from "./FilterCategoryDetails.css";
import PropTypes from "prop-types";
export default class FilterCategoryDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }
  handleClick() {
    this.setState({ isOpen: !this.state.isOpen }, () => {
      if (this.props.onClick) {
        this.props.onClick(this.props.value);
      }
    });
  }
  render() {
    let className = styles.subCategoryRegular;
    if (this.state.isOpen) {
      className = styles.subCategoryBold;
    }
    return (
      <div className={styles.base}>
        <div className={styles.header} onClick={() => this.handleClick()}>
          <div className={className}>{this.props.category}</div>
          <div className={styles.subCategoryCount}>
            {this.props.categoryCount}
          </div>
        </div>
        {this.props.children &&
          this.state.isOpen && (
            <div className={styles.subListHolder}>{this.props.children}</div>
          )}
      </div>
    );
  }
}
FilterCategoryDetails.propTypes = {
  onClick: PropTypes.func,
  category: PropTypes.string,
  categoryCount: PropTypes.number
};
