import React from "react";
import styles from "./ImageUpload.css";
import PropTypes from "prop-types";
export default class ImageUpload extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: this.props.value ? this.props.value : "Upload picture of Receipt"
    };
  }
  handleChange(event) {
    const file = event.target.files[0];
    this.setState(
      {
        value: file.name,
        file: file
      },
      () => {
        if (this.props.onChange) {
          this.props.onChange(this.state.file);
        }
      }
    );
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.inputHolder}>
          <div className={styles.fileUploadHolder}>
            <div className={styles.pathUploadHolder}>{this.state.value}</div>
            <div className={styles.fileHolder}>
              Browse
              <input
                id="fileinput"
                type="file"
                className={styles.file}
                multiple
                onChange={val => this.handleChange(val)}
              />
            </div>
          </div>
        </div>
      </div>
    );
  }
}
ImageUpload.propTypes = {
  onChange: PropTypes.func
};
