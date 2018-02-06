import React from "react";
import styles from "./TextArea.css";
import PropTypes from "prop-types";
export default class TextArea extends React.Component {
  handleChange(event) {
    if (this.props.onChange) {
      this.props.onChange(event.target.value);
    }
  }
  render() {
    return (
      <div className={styles.container}>
        <textarea
          className={styles.textAreaBox}
          placeholder="Address*"
          value={this.props.value}
          onChange={event => {
            this.handleChange(event);
          }}
          style={{ height: `${this.props.height}px` }}
        />
      </div>
    );
  }
}
TextArea.propTypes = {
  onChange: PropTypes.func,
  height: PropTypes.number,
  value: PropTypes.string
};
TextArea.defaultProps = {
  height: 100
};
