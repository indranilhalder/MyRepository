import React from "react";
import Image from "../../xelpmoc-core/Image";
import Button from "../../general/components/Button";
import styles from "./StoryWidget.css";

export default class StoryProduct extends React.Component {
  handleClick() {
    this.props.history.push(`/p-${this.props.productListingId.toLowerCase()}`);
  }
  render() {
    return (
      <div className={styles.content}>
        <div className={styles.productImage}>
          <div className={styles.imageActual}>
            <Image image={this.props.imageUrl} />
          </div>
        </div>
        <div className={styles.productSection}>
          <div className={styles.row}>{this.props.productName}</div>
          <div className={styles.row}>{this.props.mrp} </div>
          <div className={styles.button}>
            <Button
              label="View Product"
              type="secondary"
              onClick={() => this.handleClick()}
            />
          </div>
        </div>
      </div>
    );
  }
}
