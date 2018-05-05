import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./Input.css";

class Input extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: props.value ? props.value : "",
      disabled: this.props.disabled,
      focused: false
    };
    this.styles = this.props.styles ? this.props.styles : styles;
  }
  handleChange(event) {
    if (this.props.onChange) {
      this.props.onChange(event.target.value);
    }
    this.setState({ value: event.target.value });
  }
  handleFocus(event) {
    if (this.props.onFocus) {
      this.props.onFocus(event);
    }
    this.setState({ focused: true });
  }
  handleBlur(event) {
    if (this.props.onBlur) {
      this.props.onBlur(event);
    }
    this.setState({ focused: false });
  }
  handleKeyUp(event) {
    if (this.props.onKeyUp) {
      this.props.onKeyUp(event);
    }
  }
  render() {
    var { leftChild, rightChild, placeholder, type, ...other } = this.props;
    let className = this.styles.base;
    if (this.state.focused) {
      className = this.styles.focused;
    }

    if (this.state.disabled) {
      className = this.styles.disabled;
    }

    if (this.props.hollow) {
      className = this.styles.hollow;
    }
    return (
      <div className={className}>
        {leftChild && <div className={styles.leftChild}>{leftChild}</div>}
        <input
          type={type}
          value={this.props.value ? this.props.value : this.state.value}
          className={styles.box}
          onChange={event => this.handleChange(event)}
          onFocus={event => this.handleFocus(event)}
          onKeyUp={event => this.handleKeyUp(event)}
          onBlur={event => this.handleBlur(event)}
          placeholder={placeholder}
        />
        {rightChild && (
          <div className={this.styles.rightChild}>{rightChild}</div>
        )}
      </div>
    );
  }
}

Input.propTypes = {
  onFocus: PropTypes.func,
  onBlur: PropTypes.func,
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  maxLength: PropTypes.number,
  value: PropTypes.string
};

Input.defaultProps = {
  disabled: false
};

export default Input;
