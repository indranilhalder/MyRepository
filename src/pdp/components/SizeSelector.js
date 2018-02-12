import React from "react";
import styles from "./SizeSelector.css";
import SizeSelect from "./SizeSelect";
import CarouselWithSelect from "../../general/components/CarouselWithSelect";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import PropTypes from "prop-types";
const SIZE_GUIDE = "Size guide";
export default class SizeSelector extends React.Component {
  handleShowSize() {
    if (this.props.showSizeGuide) {
      this.props.showSizeGuide();
    }
  }
  render() {
    let data = this.props.data[0];
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          Select a size
          <div className={styles.button}>
            <UnderLinedButton
              label={SIZE_GUIDE}
              onClick={() => {
                this.handleShowSize();
              }}
            />
          </div>
        </div>
        <CarouselWithSelect elementWidthMobile={18} limit={1}>
          {data.map((datum, i) => {
            return (
              <SizeSelect
                key={i}
                selected={this.props.selected}
                size={datum.size}
                value={datum.size}
              />
            );
          })}
        </CarouselWithSelect>
      </div>
    );
  }
}

SizeSelector.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.arrayOf(
      PropTypes.shape({
        size: PropTypes.string,
        selected: PropTypes.bool
      })
    )
  )
};
