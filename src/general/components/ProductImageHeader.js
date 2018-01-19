import React from "react";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import MediaQuery from "react-responsive";
import styles from "./ProductImageHeader.css";
export default class ProductImageHeader extends React.Component {
  render() {
    let className = styles.base;
    if (this.props.description) {
      className = styles.hasDescription;
    }
    return (
      <div className={className}>
        <div className={styles.content}>
          <Image image={this.props.image} />
          <MediaQuery query="(max-device-width:1023px)">
            {this.props.logo && (
              <div className={styles.logo}>{this.props.logo}</div>
            )}
          </MediaQuery>
          <div className={styles.imageOverlay}>
            {this.props.name && (
              <div className={styles.name}>{this.props.name}</div>
            )}
            {this.props.label && (
              <div className={styles.label}>{this.props.label}</div>
            )}
            {this.props.description && (
              <div className={styles.description}>
                <MediaQuery query="(min-device-width:1024px)">
                  {this.props.logo && (
                    <div className={styles.logo}>{this.props.logo}</div>
                  )}
                </MediaQuery>
                {this.props.description}
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
}

ProductImageHeader.propTypes = {
  name: PropTypes.string,
  label: PropTypes.string,
  description: PropTypes.string,
  image: PropTypes.string,
  logo: PropTypes.element
};
