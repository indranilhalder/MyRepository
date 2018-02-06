import React from "react";
import PropTypes from "prop-types";
import styles from "./UnderLinedButton.css";
export default class UnderLinedButton extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        styles={{
          color: this.props.color,
          fontSize: this.props.size,
          fontFamily: this.props.fontFamily
        }}
        onClick={() => {
          this.handleClick();
        }}
      >
        {this.props.label}
        <div
          className={styles.underline}
          styles={{ backgroundColor: this.props.color }}
        />
      </div>
    );
  }
}
UnderLinedButton.propTypes = {
  onClick: PropTypes.func,
  color: PropTypes.string,
  label: PropTypes.string,
  fontFamily: PropTypes.string,
  fontSize: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};
