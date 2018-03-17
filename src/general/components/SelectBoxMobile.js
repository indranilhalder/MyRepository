import React from "react";
import PropTypes from "prop-types";
import { Icon } from "xelpmoc-core";
import styles from "./SelectBoxMobile.css";
import GreyArrow from "./img/down-arrow-grey.svg";
import BlackArrow from "./img/down-arrow.svg";
import WhiteArrow from "./img/down-arrow-white.svg";
const BLACK = "black";
const WHITE = "white";
const GREY = "grey";
const HOLLOW_BOX = "hollowBox";
const BLACK_BOX = "blackBox";
const GREY_BOX = "greyBox";
export default class SelectBoxMobile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: this.props.value
        ? this.props.value
        : this.props.options ? this.props.options[0].value : ""
    };
  }
  handleChange(event) {
    this.setState({ value: event.target.value }, () => {
      if (this.props.onChange) {
        this.props.onChange(this.state.value);
      }
    });
  }
  render() {
    let arrow = GreyArrow;
    if (this.props.arrowColour === BLACK) {
      arrow = BlackArrow;
    }
    if (this.props.arrowColour === GREY) {
      arrow = GreyArrow;
    }
    if (this.props.arrowColour === WHITE) {
      arrow = WhiteArrow;
    }
    let themeClass = styles.base;
    if (this.props.theme === BLACK_BOX) {
      themeClass = styles.blackBox;
    }
    if (this.props.theme === HOLLOW_BOX) {
      themeClass = styles.hollowBox;
    }
    if (this.props.arrowColour === GREY_BOX) {
      themeClass = styles.base;
    }
    return (
      <div
        className={themeClass}
        style={{
          height: this.props.height,
          lineHeight: `${this.props.height}px`
        }}
      >
        <select
          name={this.props.name}
          className={styles.hideSelect}
          onChange={event => this.handleChange(event)}
        >
          {this.props.options &&
            this.props.options.map((item, i) => {
              return (
                <option key={i} value={item.value}>
                  {item.value}
                </option>
              );
            })}
        </select>
        <div className={styles.visibleBox}>{this.state.value}</div>
        <div className={styles.arrow}>
          <Icon image={arrow} size={12} />
        </div>
      </div>
    );
  }
}
SelectBoxMobile.propTypes = {
  height: PropTypes.number,
  options: PropTypes.arrayOf(PropTypes.shape({ value: PropTypes.string })),
  arrowColour: PropTypes.oneOf([BLACK, GREY, WHITE]),
  theme: PropTypes.oneOf([HOLLOW_BOX, BLACK_BOX, GREY_BOX])
};
SelectBoxMobile.defaultProps = {
  height: 35,
  arrowColour: GREY
};
