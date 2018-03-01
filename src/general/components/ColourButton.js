import React from "react";
import PropTypes from "prop-types";
import styles from "./ColourButton.css";
export default class ColourButton extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        style={{
          color: this.props.colour,
          fontSize: this.props.size,
          fontFamily: this.props.fontFamily
        }}
        onClick={() => {
          this.handleClick();
        }}
      >
        {this.props.label}
      </div>
    );
  }
}
ColourButton.propTypes = {
  onClick: PropTypes.func,
  color: PropTypes.string,
  label: PropTypes.string,
  fontFamily: PropTypes.string,
  fontSize: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};
ColourButton.defaultProps = {
  size: "14px"
};
