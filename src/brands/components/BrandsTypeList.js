import React from "react";
import styles from "./BrandsTypeList.css";
import PropTypes from "prop-types";
export default class BrandsTypeList extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        {this.props.list}
      </div>
    );
  }
}
BrandsTypeList.propTypes = {
  list: PropTypes.string,
  onClick: PropTypes.func
};
