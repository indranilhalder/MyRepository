import React from "react";
import MobileSelectWithError from "../../general/components/MobileSelectWithError";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import styles from "./SizeQuantitySelect.css";
import _ from "lodash";
export default class SizeQuantitySelect extends React.Component {
  updateSize(productUrl) {
    this.props.history.push(productUrl);
  }
  handleShowSize() {
    if (this.props.showSizeGuide) {
      this.props.showSizeGuide();
    }
  }
  render() {
    console.log(_.findIndex(this.props.sizes, { size: "No Size" }));
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
          {this.props.showSizeGuide && (
            <div className={styles.button}>
              <UnderLinedButton
                label="Size Guide"
                onClick={() => {
                  this.handleShowSize();
                }}
              />
            </div>
          )}
        </div>
        <div className={styles.selectHolder}>
          {_.findIndex(this.props.sizes, { size: "No Size" }) !== 0 && (
            <div className={styles.sizeSelect}>
              <MobileSelectWithError
                value="Size"
                options={this.props.sizes.map(val => {
                  return { label: val.size, value: val.url };
                })}
                onChange={value => this.updateSize(value)}
              />
            </div>
          )}
          <div className={styles.sizeQuantity}>
            <MobileSelectWithError value="1" options={fetchedQuantityList} />
          </div>
        </div>
      </div>
    );
  }
}
