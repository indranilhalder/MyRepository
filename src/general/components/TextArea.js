import React from "react";
import styles from "./TextArea.css";
import PropTypes from "prop-types";
export default class TextArea extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: props.value ? props.value : ""
    };
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
        <textarea
          className={styles.textAreaBox}
          placeholder="Write your comments"
          value={this.props.value ? this.props.value : this.state.value}
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
  height: 100,
  value: ""
};
