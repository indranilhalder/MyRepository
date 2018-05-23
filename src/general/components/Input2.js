import React from "react";
import styles from "./Input2.css";
import PropTypes from "prop-types";
export default class Input2 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      focused: false,
      value: props.value ? props.value : ""
    };
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
  handleChange(event) {
    this.setState({ value: event.target.value }, () => {
      if (this.props.onChange) {
        this.props.onChange(this.state.value);
      } else {
        this.setState({ value: event.target.value });
      }
    });
  }
  handleKeyPress(event) {
    if (this.props.onKeyPress) {
      this.props.onKeyPress(event);
    }
    if (this.props.onlyAlphabet) {
      var regex = new RegExp("^[a-zA-Z\\s]+$");
      var charCode = event.which ? event.which : event.keyCode;
      if (
        (charCode > 64 && charCode < 91) ||
        (charCode > 96 && charCode < 123)
      ) {
        return true;
      }
      var key = String.fromCharCode(
        !event.charCode ? event.which : event.charCode
      );
      if (!regex.test(key)) {
        event.preventDefault();
      } else {
        return false;
      }
    }
    if (this.props.onlyNumber) {
      var number_regex = new RegExp("^[0-9]+$");
      var keyCode = String.fromCharCode(
        !event.charCode ? event.which : event.charCode
      );
      if (!number_regex.test(keyCode)) {
        event.preventDefault();
      } else {
        return false;
      }
    }
  }
  handleKeyUp = event => {
    if (this.props.onKeyUp) {
      this.props.onKeyUp(event);
    }
  };

  render() {
    let className = styles.base;
    if (this.props.isWhite) {
      className = styles.whiteHollow;
    }
    if (this.props.isWhite && this.props.boxy) {
      className = styles.whiteBox;
    }
    return (
      <div className={className}>
        <div className={this.state.focused ? styles.focused : styles.wrapper}>
          <div
            className={this.props.hollow ? styles.hollow : styles.box}
            style={{
              paddingLeft: `${this.props.leftChildSize - 10}px`,
              paddingRight: `${this.props.rightChildSize - 10}px`,
              height: `${this.props.height}px`,
              borderColor: `${this.props.borderColor}`,
              borderBottom: `${this.props.borderBottom}`,
              background: `${this.props.background}`
            }}
          >
            <input
              type={this.props.type}
              id={this.props.id}
              placeholder={this.props.placeholder}
              className={styles.inputBox}
              onFocus={event => this.handleFocus(event)}
              onBlur={event => this.handleBlur(event)}
              onChange={event => this.handleChange(event)}
              style={{ ...this.props.textStyle }}
              onKeyUp={event => this.handleKeyUp(event)}
              value={this.props.value}
              maxLength={this.props.maxLength}
              disabled={this.props.disabled}
              autoFocus={this.props.autoFocus}
              onKeyPress={event => this.handleKeyPress(event)}
            />
          </div>
          {this.props.leftChild && (
            <div
              className={styles.boxIconLeft}
              style={{ width: this.props.leftChildSize }}
            >
              {this.props.leftChild}
            </div>
          )}
          {this.props.rightChild && (
            <div
              className={styles.boxIconRight}
              style={{ width: this.props.rightChildSize }}
            >
              {this.props.rightChild}
            </div>
          )}
        </div>
      </div>
    );
  }
}
Input2.propTypes = {
  id: PropTypes.string,
  hollow: PropTypes.bool,
  boxy: PropTypes.bool,
  type: PropTypes.string,
  leftChild: PropTypes.element,
  rightChild: PropTypes.element,
  isWhite: PropTypes.bool,
  onFocus: PropTypes.func,
  onBlur: PropTypes.func,
  placeholder: PropTypes.string,
  onChange: PropTypes.func,
  height: PropTypes.number,
  background: PropTypes.string,
  textStyle: PropTypes.shape({
    fontSize: PropTypes.number
  })
};

Input2.defaultProps = {
  id: null,
  height: 40,
  type: "text",
  textStyle: {
    fontSize: 14
  },
  disabled: false,
  borderBottom: "1px solid #d2d2d2",
  onlyAlphabet: false,
  onlyNumber: false
};
