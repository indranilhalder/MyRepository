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
              height: `${this.props.height}px`
            }}
          >
            <input
              type="text"
              placeholder={this.props.placeholder}
              className={styles.inputBox}
              onFocus={event => this.handleFocus(event)}
              onBlur={event => this.handleBlur(event)}
              onChange={event => this.handleChange(event)}
              style={{ ...this.props.textStyle }}
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
  hollow: PropTypes.bool,
  boxy: PropTypes.bool,
  leftChild: PropTypes.element,
  rightChild: PropTypes.element,
  isWhite: PropTypes.bool,
  onFocus: PropTypes.func,
  onBlur: PropTypes.func,
  placeholder: PropTypes.string,
  onChange: PropTypes.func,
  height: PropTypes.number,
  textStyle: PropTypes.shape({
    fontSize: PropTypes.number
  })
};
Input2.defaultProps = {
  height: 40,
  textStyle: {
    fontSize: 14
  }
};
