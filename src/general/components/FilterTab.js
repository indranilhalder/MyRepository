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
    let classActive = styles.dataHolder;
    if (this.props.selected) {
      classActive = styles.selectedActive;
    }
    if (this.props.type) {
      classActive = styles.selectedAdvance;
    }
    if (this.props.selected && this.props.type) {
      classActive = styles.selectedActive;
    }
    return (
      <div className={styles.base}>
        <div className={classActive} onClick={() => this.handleClick()}>
          <div className={styles.selectedText}>{this.props.text}</div>
          {this.props.value && (
            <div className={styles.selected}>{this.props.value}</div>
          )}
        </div>
      </div>
    );
  }
}

SelectedFilter.propTypes = {
  text: PropTypes.string,
  value: PropTypes.string,
  selected: PropTypes.bool,
  onClick: PropTypes.func
};
