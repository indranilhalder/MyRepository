import React, { Component } from "react";
import { Icon, CircleButton } from "xelpmoc-core";
import PropTypes from "prop-types";
import styles from "./ProductDescription.css";

export default class ProductDescription extends Component {
  handleClick() {
    if (this.props.onDownload) {
      this.props.onDownload();
    }
  }

  render() {
    let headerClass = styles.header;
    let priceClass = styles.priceHolder;
    let headerText = styles.headerText;
    let contentClass = styles.content;
    if (this.props.onDownload) {
      headerClass = styles.hasDownload;
    }

    if (
      this.props.discountPrice &&
      this.props.price !== this.props.discountPrice
    ) {
      priceClass = styles.priceCancelled;
    }
    if (this.props.isWhite) {
      headerText = styles.headerWhite;
      contentClass = styles.contentWhite;
    }

    return (
      <div className={styles.base}>
        <div className={headerClass}>
          <div className={headerText}>{this.props.title}</div>
          {this.props.onDownload && (
            <div className={styles.button}>
              <CircleButton
                size={20}
                color={"transparent"}
                icon={<Icon image={this.props.icon} />}
                onClick={() => this.handleClick()}
              />
            </div>
          )}
        </div>
        <div className={contentClass}>
          {this.props.description && (
            <div className={styles.description}>{this.props.description}</div>
          )}

          {this.props.discountPrice &&
            this.props.discountPrice !== this.props.price && (
              <div className={styles.discount}>
                {`Rs. ${this.props.discountPrice}`}
              </div>
            )}

          {this.props.price && (
            <div className={priceClass}>{`Rs. ${this.props.price}`}</div>
          )}
        </div>
      </div>
    );
  }
}

ProductDescription.propTypes = {
  title: PropTypes.string,
  description: PropTypes.string,
  price: PropTypes.string,
  discountPrice: PropTypes.string,
  icon: PropTypes.string,
  onDownload: PropTypes.func,
  isWhite: PropTypes.bool
};

ProductDescription.defaultProps = {
  title: "",
  icon: "",
  description: "",
  price: "",
  isWhite: false,
  textColor: "#212121"
};
