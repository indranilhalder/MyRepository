import React from "react";
import PropTypes from "prop-types";
import CheckBox from "../../general/components/CheckBox.js";
import styles from "./FilterSelect.css";
import { URL_ROOT } from "../../lib/apiRequest";

export default class FilterSelect extends React.Component {
  handleUrlClick = e => {
    e.preventDefault();
    if (this.props.onClick) {
      this.props.onClick(this.props.url);
    }
  };

  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick(this.props.url);
    }
  };

  renderBody = () => {
    let contentClass = styles.itemContent;
    let countStyle = styles.count;
    if (this.props.selected) {
      contentClass = styles.contentSelected;
      countStyle = styles.countSelected;
    }
    return (
      <React.Fragment>
        {this.props.hexColor && (
          <div
            className={styles.colourIndicator}
            style={{ backgroundColor: this.props.hexColor }}
          />
        )}
        <div className={contentClass}>
          {this.props.icon && (
            <div className={styles.itemLogo}>{this.props.icon}</div>
          )}
          <div className={styles.dataHolder}>
            <div className={styles.data}>{this.props.label}</div>
          </div>
        </div>
        <div className={styles.check}>
          <div className={countStyle}>{this.props.count}</div>
          <div className={styles.checkCircle}>
            <CheckBox selected={this.props.selected} />
          </div>
        </div>
      </React.Fragment>
    );
  };

  constructCategoryAndBrandUrl = () => {
    return `${URL_ROOT}${
      this.props.history.location.pathname
    }/b-${this.props.value.toLowerCase()}`;
  };
  render() {
    if (this.props.isBrand && this.props.categoryId) {
      return (
        <a
          className={this.props.hexColor ? styles.itemHasColour : styles.item}
          onClick={this.handleUrlClick}
          href={this.constructCategoryAndBrandUrl()}
        >
          {this.renderBody()}
        </a>
      );
    }
    return (
      <div
        className={this.props.hexColor ? styles.itemHasColour : styles.item}
        onClick={this.handleClick}
      >
        {this.renderBody()}
      </div>
    );
  }
}

FilterSelect.propTypes = {
  selected: PropTypes.bool,
  icon: PropTypes.element,
  label: PropTypes.string,
  count: PropTypes.number,
  hexColor: PropTypes.string
};
