import React from "react";
import MobileSelectWithError from "../../general/components/MobileSelectWithError";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import styles from "./SizeQuantitySelect.css";

export default class SizeQuantitySelect extends React.Component {
  updateSize(productUrl) {
    this.props.history.push(productUrl);
  }
  render() {
    let fetchedQuantityList = [];
    if (this.props.maxQuantity) {
      for (let i = 1; i <= parseInt(this.props.maxQuantity, 10); i++) {
        fetchedQuantityList.push({ value: i.toString() });
      }
    } else {
      fetchedQuantityList = ["1"];
    }
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          Select a size
          <div className={styles.button}>
            <UnderLinedButton
              label="Size Guide"
              onClick={() => {
                this.handleShowSize();
              }}
            />
          </div>
        </div>
        <div className={styles.selectHolder}>
          <div className={styles.sizeSelect}>
            <MobileSelectWithError
              value="Size"
              options={this.props.sizes.map(val => {
                return { label: val.size, value: val.url };
              })}
              onChange={value => this.updateSize(value)}
            />
          </div>
          <div className={styles.sizeQuantity}>
            <MobileSelectWithError
              value="Quantity"
              options={fetchedQuantityList}
            />
          </div>
        </div>
      </div>
    );
  }
}
