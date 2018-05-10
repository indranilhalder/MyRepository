import React from "react";
import PropTypes from "prop-types";
import styles from "./Image.css";

const LOADING = "loading";
const LOADED = "loaded";
const ERROR = "error";
export default class Image extends React.Component {
  constructor(props) {
    super(props);
    this.state = { imageStatus: LOADING };
    this.styles = props.styles ? props.styles : styles;
  }
  handleImageLoaded() {
    this.setState({ imageStatus: LOADED });
  }

  handleImageErrored() {
    this.setState({ imageStatus: ERROR });
  }
  render() {
    const className = this.styles.base;
    const fit = this.props.fit;
    return (
      <div className={className} style={{ backgroundColor: this.props.color }}>
        <img
          className={this.styles.actual}
          alt="No Image"
          src={this.props.image}
          onLoad={() => this.handleImageLoaded()}
          onError={() => this.handleImageErrored()}
        />
        {this.state.imageStatus === LOADED && (
          <div
            className={this.styles.actual}
            style={{
              backgroundImage: `url(${this.props.image})`,
              backgroundSize: fit
            }}
          />
        )}
        {this.state.imageStatus === ERROR && (
          <div className={this.styles.failed} />
        )}
      </div>
    );
  }
}
Image.propTypes = {
  image: PropTypes.string.isRequired,
  fit: PropTypes.string,
  color: PropTypes.string
};

Image.defaultProps = {
  fit: "cover",
  color: "#fff"
};
