import React from "react";
import styles from "./BrandsTypeList.css";
import PropTypes from "prop-types";
export default class BrandsTypeList extends React.Component {
  handleClick(val) {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let className = styles.base;
    return (
      <div
        className={this.props.selected ? styles.active : className}
        onClick={val => this.handleClick(this.props.list)}
      >
        {this.props.list}
      </div>
    );
  }
}
BrandsTypeList.propTypes = {
  list: PropTypes.string,
  onClick: PropTypes.func
};
