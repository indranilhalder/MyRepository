import React from "react";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import styles from "./SizeQuantitySelect.css";

export default class SizeQuantitySelect extends React.Component {
  updateSize(productUrl) {
    this.props.history.push({
      pathname: `${productUrl}`,
      state: { isSizeSelected: true }
    });
  }
  updateQuantity(quantity) {
    if (this.props.updateQuantity) {
      this.props.updateQuantity(quantity);
    }
  }

  handleShowSize() {
    if (this.props.showSizeGuide) {
      this.props.showSizeGuide();
    }
  }
  render() {
    const selectedColour = this.props.data.filter(val => {
      return val.colorlink.selected;
    })[0].colorlink.color;
    const sizes = this.props.data
      .filter(val => {
        return val.sizelink.isAvailable;
      })
      .filter(val => {
        return selectedColour ? val.colorlink.color === selectedColour : true;
      })
      .map(val => {
        return val.sizelink;
      });
    sizes.unshift({ size: "Size", value: "size" });
    console.log(sizes);

    let fetchedQuantityList = [{ value: "quantity", label: "Quantity" }];
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
                value="Size"
                label="Size"
                options={sizes.map(val => {
                  return { label: val.size, value: val.url };
                })}
                onChange={value => this.updateSize(value)}
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
                value="Quantity"
                label="Quantity"
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
