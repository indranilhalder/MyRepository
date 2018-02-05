import React from "react";
import styles from "./FilterTab.css";
import PropTypes from "prop-types";

export default class SelectedFilter extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    let classActive = styles.base;
    if (this.props.type === "advance") {
      classActive = styles.selectedAdvance;
    }
    if (this.props.selected) {
      classActive = styles.selectedActive;
    }

    return (
      <div className={classActive} onClick={() => this.handleClick()}>
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

SelectedFilter.propTypes = {
  name: PropTypes.string,
  selectedFilterCount: PropTypes.number,
  type: PropTypes.oneOf(["advance", "global"]),
  selected: PropTypes.bool,
  onClick: PropTypes.func
};
