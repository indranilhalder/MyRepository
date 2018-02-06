import React from "react";
import styles from "./SortTab.css";
import propTypes from "prop-types";
export default class SortTab extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick(this.props.value);
    }
  }
  render() {
    let data = this.props.data;
    return (
      <div className={styles.mainHolder} onClick={() => this.handleClick()}>
        {this.props.label}
      </div>
    );
  }
}
SortTab.propTypes = {
  label: propTypes.string,
  onClick: propTypes.func,
  value: propTypes.string
};
