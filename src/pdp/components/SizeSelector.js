import React from "react";
import styles from "./SizeSelector.css";
import SizeSelect from "./SizeSelect";
import Carousel from "../../general/components/Carousel";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import PropTypes from "prop-types";
const SIZE_GUIDE = "Size guide";
export default class SizeSelector extends React.Component {
  handleShowSize() {
    if (this.props.showSizeGuide) {
      this.props.showSizeGuide();
    }
  }
  updateSize(productUrl) {
    this.props.history.push({
      pathname: `${productUrl}`,
      state: { isSizeSelected: true }
    });
    if (this.props.closeModal) {
      this.props.closeModal();
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
        return val.colorlink.color === selectedColour;
      })
      .map(val => {
        return val.sizelink;
      });
    //return val.isAvailable
    console.log(sizes);
    let data = this.props.data;
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          {this.props.headerText}
          <div className={styles.button}>
            <UnderLinedButton
              disabled={!this.props.hasSizeGuide}
              label={SIZE_GUIDE}
              onClick={() => {
                this.handleShowSize();
              }}
            />
          </div>
        </div>
        <Carousel elementWidthMobile="auto" limit={1}>
          {sizes.map((datum, i) => {
            return (
              <SizeSelect
                key={i}
                selected={
                  this.props.sizeSelected
                    ? datum.productCode === this.props.productId
                    : false
                }
                size={datum.size}
                value={datum.size}
                fontSize={this.props.textSize}
                onSelect={() => this.updateSize(datum.url)}
              />
            );
          })}
        </Carousel>
      </div>
    );
  }
}

SizeSelector.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      size: PropTypes.string,
      selected: PropTypes.bool
    })
  ),
  headerText: PropTypes.string,
  textSize: PropTypes.oneOfType([PropTypes.string, PropTypes.string])
};
SizeSelector.defaultProps = {
  headerText: "Select Size"
};
