import React from "react";
import PropTypes from "prop-types";
import styles from "./Image.css";
import VisibilityChild from "../home/components/VisibilityChild";

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

  renderImage = () => {
    const img = (
      <img
        className={this.styles.actual}
        alt="No Image"
        src={this.props.image}
        onLoad={() => this.handleImageLoaded()}
        onError={() => this.handleImageErrored()}
      />
    );
    if (this.props.lazyLoad) {
      return <VisibilityChild> {img} </VisibilityChild>;
    }
    return img;
  };
  render() {
    const className = this.styles.base;
    const fit = this.props.fit;
    return (
      <div className={className} style={{ backgroundColor: this.props.color }}>
        {this.renderImage()}

        <div
          className={
            this.state.imageStatus === LOADED
              ? this.styles.loaded
              : this.styles.actual
          }
          style={{
            backgroundImage: `url(${this.props.image})`,
            backgroundSize: fit
          }}
        />
        {/* )} */}
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
  color: PropTypes.string,
  lazyLoad: PropTypes.bool
};

Image.defaultProps = {
  fit: "cover",
  color: "#fff",
  lazyLoad: false
};
