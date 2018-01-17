import React from "react";
import ProductImage from "./ProductImage";
import styles from "./CustomerQuestion.css";
export default class CustomerQuestion extends React.Component {
  handleClick(val) {
    if (this.props.selectItem) {
      this.props.selectItem(val);
    }
  }
  render() {
    let checkClass = styles.check;
    if (this.props.selected) {
      checkClass = styles.selected;
    }
    return (
      <div className={styles.base}>
        <div
          className={styles.image}
          onClick={val => {
            this.handleClick(val);
          }}
        >
          <ProductImage image={this.props.image} />
          <div className={checkClass} />
        </div>
        <div className={styles.content}>
          <div className={styles.header}>{this.props.header}</div>
          {this.props.description && (
            <div className={styles.description}>{this.props.description}</div>
          )}
        </div>
      </div>
    );
  }
}
