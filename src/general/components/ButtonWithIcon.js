import React from "react";
import PropTypes from "prop-types";
import styles from "./ButtonWithIcon.css";
export default class ButtonWithIcon extends React.Component {
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
          width: this.props.width,
          height: this.props.height,
          backgroundColor: this.props.backgroundColor,
          borderRadius: this.props.height / 2,
          lineHeight: `${this.props.height}px`
        }}
        onClick={() => this.handleClick()}
      >
        {this.props.icon &&
          this.props.icon.element && (
            <div
              className={styles.iconHolder}
              style={{
                height: this.props.icon.height,
                width: this.props.icon.width,
                marginRight: this.props.icon.offset
              }}
            >
              {this.props.icon.element}
            </div>
          )}
        <div className={styles.text} style={{ ...this.props.textStyle }}>
          {this.props.label}
        </div>
      </div>
    );
  }
}
ButtonWithIcon.propTypes = {
  height: PropTypes.number,
  width: PropTypes.number,
  backgroundColor: PropTypes.string,
  onClick: PropTypes.func,
  label: PropTypes.string,
  textStyle: PropTypes.shape({
    color: PropTypes.string,
    fontSize: PropTypes.number,
    fontFamily: PropTypes.string
  }),
  icon: PropTypes.shape({
    element: PropTypes.element,
    width: PropTypes.number,
    height: PropTypes.number,
    offset: PropTypes.number
  })
};

ButtonWithIcon.defaultProps = {
  height: 40,
  backgroundColor: "#1cc7d0",
  textStyle: {
    color: "#fff",
    fontSize: 12,
    fontFamily: "regular"
  },
  icon: {
    width: 40,
    height: 40,
    offset: 10
  }
};
