import React from "react";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import styles from "./SizeQuantitySelect.css";

export default class SizeQuantitySelect extends React.Component {
  updateSize(productUrl) {
    if (this.props.updateSize) {
      this.props.updateSize();
      this.props.history.push({
        pathname: `${productUrl}`,
        state: { isSizeSelected: true }
      });
    }
  }
  updateQuantity(quantity) {
    if (this.props.updateQuantity) {
      this.props.updateQuantity(quantity);
      if (this.props.checkIfSizeSelected()) {
        this.props.history.push({
          state: { isQuantitySelected: true, isSizeSelected: true }
        });
      } else {
        this.props.history.push({
          state: { isQuantitySelected: true }
        });
      }
    }
  }
  handleShowSize() {
    if (this.props.showSizeGuide) {
      this.props.showSizeGuide();
    }
  }
  render() {
    const selectedVariant = this.props.data.filter(val => {
      return val.colorlink.selected;
    })[0];
    const selectedColour = selectedVariant.colorlink.color;
    const selectedUrl = selectedVariant.sizelink.url;
    const selectedSize = selectedVariant.sizelink.size;
    const sizes = this.props.data

      .filter(val => {
        return selectedColour ? val.colorlink.color === selectedColour : true;
      })
      .map(val => {
        return val.sizelink;
      });
    if (!this.props.checkIfSizeSelected())
      sizes.unshift({ size: "Size", value: "size" });
    let fetchedQuantityList = [];
    if (
      !this.props.checkIfQuantitySelected() ||
      !this.props.productQuantity.label ||
      this.props.productQuantity.label === undefined
    )
      fetchedQuantityList = [{ value: "quantity", label: "Quantity" }];
    if (this.props.maxQuantity) {
      for (let i = 1; i <= parseInt(this.props.maxQuantity, 10); i++) {
        fetchedQuantityList.push({ value: i.toString(), label: i });
      }
    } else {
      fetchedQuantityList = ["1"];
    }
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          Select a size & quantity
          <div className={styles.button}>
            <UnderLinedButton
              disabled={!this.props.showSizeGuide}
              label="Size Guide"
              onClick={() => {
                this.handleShowSize();
              }}
            />
          </div>
        </div>
        <div className={styles.selectHolder}>
          <div className={styles.sizeSelect}>
            <div
              className={
                this.props.sizeError ? styles.errorBoundary : styles.boundary
              }
            >
              <SelectBoxMobile2
                theme="hollowBox"
                value={this.props.checkIfSizeSelected() ? selectedUrl : "size"}
                label={this.props.checkIfSizeSelected() ? selectedSize : "Size"}
                options={sizes.map(val => {
                  return { label: val.size, value: val.url };
                })}
                onChange={value => this.updateSize(value.value)}
              />
            </div>
          </div>

          <div className={styles.sizeQuantity}>
            <div
              className={
                this.props.quantityError
                  ? styles.errorBoundary
                  : styles.boundary
              }
            >
              <SelectBoxMobile2
                theme="hollowBox"
                value={
                  this.props.checkIfQuantitySelected()
                    ? this.props.productQuantity.value
                    : "quantity"
                }
                label={
                  this.props.checkIfQuantitySelected() &&
                  this.props.productQuantity.label &&
                  this.props.productQuantity.label !== undefined
                    ? this.props.productQuantity.label
                    : "Quantity"
                }
                options={fetchedQuantityList}
                onChange={value => this.updateQuantity(value)}
              />
            </div>
          </div>
        </div>
      </div>
    );
  }
}
