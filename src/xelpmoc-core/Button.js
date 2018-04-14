import React from "react";
import PropTypes from "prop-types";
import styles from "./Button.css";
export default class Button extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      backgroundColor: this.props.backgroundColor,
      borderColor: this.props.borderColor,
      hovered: false
    };
    this.styles = props.styles ? props.styles : styles;
  }
  hoverIn() {
    if (this.props.hoverBackgroundColor || this.props.hoverBorderColor) {
      this.setState({
        backgroundColor: this.props.hoverBackgroundColor
          ? this.props.hoverBackgroundColor
          : this.props.backgroundColor,
        borderColor: this.props.hoverBorderColor,
        hovered: true
      });
    }
  }
  hoverOut() {
    this.setState({
      backgroundColor: this.props.backgroundColor,
      borderColor: this.props.borderColor,
      hovered: false
    });
  }
  handleClick(e) {
    if (this.props.onClick) {
      this.props.onClick(e);
    }
  }
  render() {
    let className = this.styles.base;

    if (this.props.disabled) {
      className = this.styles.disabled;
    }

    if (this.props.loading) {
      className = this.styles.loading;
    }
    let iconClass = styles.icon;
    let secondaryIconClass = styles.iconHovered;
    if (this.state.hovered) {
      if (this.props.icon.hoveredElement) {
        iconClass = this.styles.iconHovered;
      }
      secondaryIconClass = styles.icon;
    }

    return (
      <div
        className={className}
        style={{
          height: this.props.height,
          width: this.props.width,
          borderRadius: this.props.borderRadius,
          backgroundColor: this.state.backgroundColor,
          border: `2px solid ${this.state.borderColor}`
        }}
        onMouseEnter={() => this.hoverIn()}
        onMouseLeave={() => this.hoverOut()}
        onClick={e => this.handleClick(e)}
      >
        {this.props.icon &&
          this.props.icon.element && (
            <div
              className={this.styles.iconWrapper}
              style={{
                height: this.props.icon.size,
                width: this.props.icon.size,
                marginRight: this.props.icon.offset
              }}
            >
              <div className={iconClass}>{this.props.icon.element}</div>
              {this.props.icon.hoveredElement && (
                <div className={secondaryIconClass}>
                  {this.props.icon.hoveredElement}
                </div>
              )}
            </div>
          )}
        <span style={{ ...this.props.textStyle }}>{this.props.label}</span>
      </div>
    );
  }
}

Button.propTypes = {
  height: PropTypes.number,
  width: PropTypes.number,
  backgroundColor: PropTypes.string,
  label: PropTypes.string,
  hoverBackgroundColor: PropTypes.string,
  disabled: PropTypes.bool,
  loading: PropTypes.bool,
  borderColor: PropTypes.string,
  borderRadius: PropTypes.number,
  textStyle: PropTypes.shape({
    color: PropTypes.string,
    fontSize: PropTypes.number,
    fontFamily: PropTypes.string
  }),
  icon: PropTypes.shape({
    element: PropTypes.element,
    hoveredElement: PropTypes.element,
    size: PropTypes.number,
    offset: PropTypes.number
  })
};

Button.defaultProps = {
  height: 40,
  backgroundColor: "#1cc7d0",
  disabled: false,
  loading: false,
  iconHeight: 40,
  iconWidth: 40,
  textStyle: {
    color: "#fff",
    fontSize: 12,
    fontFamily: "regular"
  },
  icon: {
    size: 30,
    offset: 10
  }
};
