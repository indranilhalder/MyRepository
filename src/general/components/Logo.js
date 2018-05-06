import React from "react";
import styles from "./Logo.css";
import PropTypes from "prop-types";
export default class Logo extends React.Component {
  render() {
    return (
      <div
        className={styles.base}
        style={{ height: this.props.height, width: this.props.width }}
      >
        <img
          className={styles.image}
          src={this.props.image}
          alt=""
          style={{
            height: this.props.imageHeight,
            width: this.props.imageWidth
          }}
        />
      </div>
    );
  }
}
Logo.propTypes = {
  height: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  width: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  imageHeight: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  imageWidth: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  image: PropTypes.string
};
Logo.defaultProps = {
  imageHeight: "100%",
  imageWidth: "auto"
};
