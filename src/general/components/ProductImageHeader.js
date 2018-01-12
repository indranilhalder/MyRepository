import React from "react";
import { Image } from "xelpmoc-core";
import styles from "./ProductImageHeader.css";
export default class ProductImageHeader extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.content}>
          <Image image={this.props.image} />
          {this.props.logo && (
            <div className={styles.logo}>{this.props.logo}</div>
          )}
          {this.props.name && (
            <div className={styles.name}>{this.props.name}</div>
          )}
          {this.props.label && (
            <div className={styles.label}>{this.props.label}</div>
          )}
          {this.props.description && (
            <div className={styles.description}>{this.props.description}</div>
          )}
        </div>
      </div>
    );
  }
}
