import React from "react";
import styles from "./YesNoQuestion.css";
import CheckBox from "../../general/components/CheckBox";
import PropTypes from "prop-types";
export default class YesNoQuestion extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div
          className={styles.yesNoQuestion}
          onClick={() => this.handleClick()}
        >
          <span className={styles.bold}>{this.props.confirmation}</span>
          {this.props.text}
          <div className={styles.checkBoxHolder}>
            <CheckBox selected={this.props.selected} />
          </div>
        </div>
      </div>
    );
  }
}
YesNoQuestion.propTypes = {
  selected: PropTypes.bool,
  selectItem: PropTypes.func,
  confirmation: PropTypes.string,
  text: PropTypes.string
};
