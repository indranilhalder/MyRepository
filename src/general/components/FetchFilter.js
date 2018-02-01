import React from "react";
import styles from "./FetchFilter.css";
import PropTypes from "prop-types";

export default class FetchFilter extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={styles.selectedText}>{this.props.name}</div>
        {this.props.selectedFilterCount && (
          <div className={styles.selected}>
            {this.props.selectedFilterCount}
          </div>
        )}
      </div>
    );
  }
}
FetchFilter.propTypes = {
  selectedFilterCount: PropTypes.string,
  name: PropTypes.string,
  onClick: PropTypes.func
};
