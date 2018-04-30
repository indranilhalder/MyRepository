import React from "react";
import styles from "./SizeSelector.css";
import SizeSelect from "./SizeSelect";
import DumbCarousel from "../../general/components/DumbCarousel";
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
        return selectedColour ? val.colorlink.color === selectedColour : true;
      })
      .map(val => {
        return val;
      });

    if (sizes.length !== 0) {
      return (
        <div className={styles.base}>
          <div className={styles.header}>
            Select {this.props.headerText}
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
          <DumbCarousel elementWidth="auto">
            {sizes.map((datum, i) => {
              return (
                <SizeSelect
                  key={i}
                  disabled={!datum.sizelink.isAvailable}
                  selected={
                    datum.colorlink.selected &&
                    this.props.history.location.state
                      ? this.props.history.location.state.isSizeSelected
                      : false
                  }
                  size={datum.sizelink.size}
                  value={datum.sizelink.size}
                  fontSize={this.props.textSize}
                  onSelect={() => this.updateSize(datum.sizelink.url)}
                />
              );
            })}
          </DumbCarousel>
        </div>
      );
    } else {
      return null;
    }
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
  productId: PropTypes.string,
  textSize: PropTypes.oneOfType([PropTypes.string, PropTypes.string])
};
SizeSelector.defaultProps = {
  headerText: "Size"
};
