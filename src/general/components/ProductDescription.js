import React, { Component } from "react";
import { Icon, CircleButton } from "xelpmoc-core";
import PropTypes from "prop-types";
import Header from "./Header";
import Para from "./Paragraph";
import styles from "./ProductDescription.css";

export default class ProductDescription extends Component {
  constructor(props) {
    super(props);
    this.styles = this.props.styles ? this.props.styles : styles;
  }
  handleClick() {
    if (this.props.onDownload) {
      this.props.onDownload();
    }
  }

  render() {
    let headerClass = this.styles.header;
    let priceCancel = this.styles.priceHolder;
    if (this.props.onDownload) {
      headerClass = this.styles.hasDownload;
    }

    if (this.props.discountPrice) {
      priceCancel = this.styles.priceCancelled;
    }

    return (
      <div className={styles.base}>
        <div className={headerClass}>
          <Header text={this.props.title} />
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
        <div className={styles.content}>
          {this.props.description && <Para text={this.props.description} />}

          {this.props.discountPrice && (
            <div className={styles.discount}>
              <Para text={`Rs ${this.props.discountPrice}`} />
            </div>
          )}

          {this.props.price && (
            <div className={priceCancel}>
              <Para text={`Rs ${this.props.price}`} />
            </div>
          )}
        </div>
      </div>
    );
  }
}

ProductDescription.propTypes = {
  title: PropTypes.string,
  description: PropTypes.string,
  price: PropTypes.number,
  discountPrice: PropTypes.number,
  icon: PropTypes.string,
  onDownload: PropTypes.func
};

ProductDescription.defaultProps = {
  title: "",
  icon: "",
  description: "",
  price: 0
};
