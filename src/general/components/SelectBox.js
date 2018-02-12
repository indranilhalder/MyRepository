import React, { Component } from "react";
import PropTypes from "prop-types";
import Select from "react-select";
import "react-select/dist/react-select.css";
import styles from "./SelectBox.css";
export default class SelectBox extends React.Component {
  onChange(val) {
    if (this.props.onChange) {
      this.props.onChange(val.value);
    }
  }
  render() {
    return (
      <div className={styles.selectBox}>
        <Select
          options={this.props.options}
          value={this.props.selected && this.props.selected}
          placeholder={this.props.placeholder}
          disabled={this.props.disabled}
          searchable={this.props.searchable}
          clearable={this.props.clearable}
          onChange={val => this.onChange(val)}
        />
      </div>
    );
  }
}
SelectBox.propTypes = {
  selected: PropTypes.string,
  onChange: PropTypes.func,
  placeholder: PropTypes.string,
  disabled: PropTypes.bool,
  searchable: PropTypes.bool,
  clearable: PropTypes.bool,
  options: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
      value: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
    })
  )
};
SelectBox.defaultProps = {
  selected: null,
  disabled: false,
  searchable: false,
  clearable: false,
  placeholder: "Select"
};
