import React, { Component } from "react";
import { Icon, CircleButton } from "xelpmoc-core";
import PropTypes from "prop-types";
import Header from "./Header";
import Para from "./Paragraph";
import styles from "./ProductDescription.css";

export default class ProductDescription extends Component {
  handleClick() {
    if (this.props.onIconPress) {
      this.props.onIconPress();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <Header text={this.props.title} />
          <CircleButton
            size={20}
            color={"transparent"}
            icon={<Icon image={this.props.icon} />}
            onClick={() => this.handleClick()}
          />
        </div>
        <div className={styles.content}>
          <Para text={this.props.description} />
          <Para text={`Rs ${this.props.price}`} />
        </div>
      </div>
    );
  }
}

ProductDescription.propTypes = {
  title: PropTypes.string,
  description: PropTypes.string,
  price: PropTypes.string,
  icon: PropTypes.string,
  onIconPress: PropTypes.func
};

ProductDescription.defaultProps = {
  title: "",
  icon: "",
  description: "",
  price: ""
};
