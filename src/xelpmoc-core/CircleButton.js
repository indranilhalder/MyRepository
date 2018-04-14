import React from "react";
import PropTypes from "prop-types";
import styles from "./CircleButton.css";

export default class CircleButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      hovered: this.props.hovered ? this.props.hovered : false
    };
  }
  hoverIn() {
    if (this.props.hover && this.props.hasOwnHover) {
      this.setState({ hovered: true });
    }
  }
  hoverOut() {
    if (this.props.hover && this.props.hasOwnHover) {
      this.setState({ hovered: false });
    }
  }
  handleClick() {
    if (this.props.onClick && !this.props.disabled) {
      this.props.onClick();
    }
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.hovered) {
      this.setState({ hovered: true });
    } else {
      this.setState({ hovered: false });
    }
  }
  render() {
    let className = styles.base;
    if (this.props.disabled) {
      className = styles.disabled;
    }
    let layerClass = styles.default;
    if (this.state.hovered) {
      layerClass = styles.hovered;
    }
    let hoverLayerClass = styles.hovered;
    if (this.state.hovered) {
      hoverLayerClass = styles.default;
    }
    return (
      <div
        className={className}
        onMouseOver={() => this.hoverIn()}
        onMouseOut={() => this.hoverOut()}
        onClick={() => this.handleClick()}
        style={{ width: this.props.size, height: this.props.size }}
      >
        <div
          className={layerClass}
          style={{
            backgroundColor: this.props.color,
            border: `1px solid ${this.props.borderColor}`
          }}
        >
          {this.props.icon}
        </div>
        {this.props.hover && (
          <div
            className={hoverLayerClass}
            style={{
              backgroundColor: this.props.hover.color,
              border: `1px solid ${this.props.hover.borderColor}`
            }}
          >
            {this.props.hover.icon ? this.props.hover.icon : this.props.icon}
          </div>
        )}
      </div>
    );
  }
}

CircleButton.propTypes = {
  size: PropTypes.number,
  color: PropTypes.string,
  icon: PropTypes.element,
  onClick: PropTypes.func,
  disabled: PropTypes.bool,
  hasOwnHover: PropTypes.bool,
  hovered: PropTypes.bool,
  hover: PropTypes.shape({
    color: PropTypes.string,
    icon: PropTypes.element,
    borderColor: PropTypes.string
  })
};

CircleButton.defaultProps = {
  size: 40,
  color: "#666",
  disabled: false,
  hasOwnHover: true
};
