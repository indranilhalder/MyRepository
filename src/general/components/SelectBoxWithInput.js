import React from "react";
import styles from "./SelectBoxWithInput.css";
import PropTypes from "prop-types";
import Input2 from "./Input2.js";
import SelectBoxMobile from "./SelectBoxMobile.js";

export default class SelectBoxWithInput extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      fullNmaeValue: props.fullNmaeValue ? props.fullNmaeValue : "",
      titleValue: props.titleValue ? props.titleValue : ""
    };
  }
  getTitleValue(val) {
    this.props.onChange(val);
    this.setState({ titleValue: val });
  }
  getFullNameValue(val) {
    this.props.onChange(val);
    this.setState({ fullNmaeValue: val });
  }
  render() {
    const options = [
      { value: "Mr", label: "Mr" },
      { value: "Mrs", label: "Mrs" }
    ];
    return (
      <div className={styles.base}>
        <div className={styles.dropDownHolder}>
          <div className={styles.dropDownBox}>
            <SelectBoxMobile
              theme="hollowBox"
              options={options}
              selected={this.props.titleValue}
              onChange={val => this.getTitleValue(val)}
            />
          </div>
          <div className={styles.fullNameBox}>
            <Input2
              boxy={true}
              placeholder="Full name*"
              onChange={val => this.getFullNameValue(val)}
              textStyle={{ fontSize: 14 }}
              height={35}
              value={this.props.value && this.props.value}
            />
          </div>
        </div>
      </div>
    );
  }
}
SelectBoxWithInput.propTypes = {
  value: PropTypes.string,
  heading: PropTypes.string,
  theme: PropTypes.string,
  onClick: PropTypes.func,
  height: PropTypes.string,
  placeholder: PropTypes.string
};
