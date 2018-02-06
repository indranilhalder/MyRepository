import React from "react";
import styles from "./TextArea.css";
import PropTypes from "prop-types";
export default class TextArea extends React.Component {
  handleChange(event) {
    if (this.props.onChange) {
      this.props.onChange();
    }
  }
  render() {
    return (
      <div className={styles.container}>
        <textarea
          className={styles.textAreaBox}
          placeholder="Address*"
          onChange={event => {
            this.handleChange(event);
          }}
        />
      </div>
    );
  }
}
TextArea.propTypes = {
  onChange: PropTypes.func
};
