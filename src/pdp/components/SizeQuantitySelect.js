import React from "react";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import styles from "./SizeQuantitySelect.css";

export default class SizeQuantitySelect extends React.Component {
  updateSize(productUrl) {
    this.props.history.push(productUrl);
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
          Select a size & quantity
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
          <div className={styles.sizeSelect}>
            <SelectBoxMobile
              options={sizes.map(val => {
                return { label: val.size, value: val.url };
              })}
              onChange={value => this.updateSize(value)}
            />
          </div>

          <div className={styles.sizeQuantity}>
            <SelectBoxMobile
              value="1"
              options={fetchedQuantityList}
              onChange={value => this.updateQuantity(value)}
            />
          </div>
        </div>
      </div>
    );
  }
}
