import React from "react";
import styles from "./Logo.css";
import PropTypes from "prop-types";
export default class Logo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoaded: false
    };
  }
  handleImageLoaded() {
    this.setState({ isLoaded: true });
  }

  handleImageErrored() {
    this.setState({ isLoaded: false });
  }
  render() {
    let imageClass = styles.image;
    if (this.state.isLoaded) {
      imageClass = styles.imageLoaded;
    }
    return (
      <div
        className={styles.base}
        style={{ height: this.props.height, width: this.props.width }}
      >
        <img
          className={imageClass}
          src={this.props.image}
          alt="No"
          onLoad={() => this.handleImageLoaded()}
          onError={() => this.handleImageErrored()}
        />
      </div>
    );
  }
}
Logo.propTypes = {
  height: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  width: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  image: PropTypes.string
};
