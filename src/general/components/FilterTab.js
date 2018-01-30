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
    if (this.props.selected === true) {
      classActive = styles.selectedActive;
    }
    if (this.props.type === "advance") {
      classActive = styles.selectedAdvance;
    }
    if (this.props.selected && this.props.type) {
      classActive = styles.selectedActive;
    }
    return (
      <div className={classActive} onClick={() => this.handleClick()}>
        <div className={styles.selectedText}>{this.props.text}</div>
        {this.props.value && (
          <div className={styles.selected}>{this.props.value}</div>
        )}
      </div>
    );
  }
}

SelectedFilter.propTypes = {
  text: PropTypes.string,
  value: PropTypes.string,
  type: PropTypes.oneOf(["advance"]),
  selected: PropTypes.bool,
  onClick: PropTypes.func
};
