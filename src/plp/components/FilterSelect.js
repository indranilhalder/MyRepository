import React from "react";
import PropTypes from "prop-types";
import CheckBox from "../../general/components/CheckBox.js";
import styles from "./FilterSelect.css";

export default class FilterSelect extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let contentClass = styles.itemContent;
    let countStyle = styles.count;
    if (this.props.selected) {
      contentClass = styles.contentSelected;
      countStyle = styles.countSelected;
    }
    return (
      <div className={styles.item} onClick={() => this.handleClick()}>
        <div className={contentClass}>
          {this.props.icon && (
            <div className={styles.itemLogo}>{this.props.icon}</div>
          )}
          {this.props.label}
        </div>
        <div className={styles.check}>
          <div className={countStyle}>{this.props.count}</div>
          <div className={styles.checkCircle}>
            <CheckBox selected={this.props.selected} />
          </div>
        </div>
      </div>
    );
  }
}

FilterSelect.propTypes = {
  selected: PropTypes.bool,
  icon: PropTypes.element,
  label: PropTypes.string,
  count: PropTypes.string
};
