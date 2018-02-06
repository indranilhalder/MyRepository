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
    if (this.props.onChange) {
      this.props.onChange(event.target.value);
    }
    this.setState({ value: event.target.value });
  }
  render() {
    return (
      <div className={styles.container}>
        <div className={this.state.focused ? styles.focused : styles.wrapper}>
          <div
            className={this.props.hollow ? styles.hollow : styles.box}
            style={{
              paddingLeft: `${this.props.leftchildsize - 10}px`,
              paddingRight: `${this.props.rightchildsize - 10}px`,
              height: this.props.hollow
                ? `${this.props.hollowheight}px`
                : `${this.props.boxheight}px`
            }}
          >
            <input
              type="text"
              className={styles.inputBox}
              hollow={this.props.hollow}
              onFocus={event => this.handleFocus(event)}
              onBlur={event => this.handleBlur(event)}
              onChange={event => this.handleChange(event)}
              {...this.props}
            />
          </div>
          {this.props.leftchild && (
            <div
              className={styles.boxIconLeft}
              style={{ width: this.props.leftchildsize }}
            >
              {this.props.leftchild}
            </div>
          )}
          {this.props.rightchild && (
            <div
              className={styles.boxIconRight}
              style={{ width: this.props.rightchildsize }}
            >
              {this.props.rightchild}
            </div>
          )}
        </div>
      </div>
    );
  }
}
Input2.propTypes = {
  hollow: PropTypes.string,
  boxy: PropTypes.string,
  leftchild: PropTypes.element,
  rightchild: PropTypes.element,
  onFocus: PropTypes.func
};
Input2.defaultProps = {
  hollowheight: 40,
  boxheight: 35
};
