import React from "react";
import ColourButton from "../../general/components/ColourButton";
import PropTypes from "prop-types";
import styles from "./CategoryL1.css";
export default class CategoryL1 extends React.Component {
  handleViewAll() {
    console.log("clicking");
    if (this.props.onViewAll) {
      this.props.onViewAll();
    }
  }
  openItem() {
    if (this.props.children) {
      this.props.openItem();
    } else {
      if (this.props.onClick) {
        this.props.onClick(this.props.url);
      }
    }
  }
  closeItem() {
    if (this.props.closeItem) {
      this.props.closeItem();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {!this.props.isOpen && (
          <div
            className={styles.closedHeader}
            onClick={() => {
              this.openItem();
            }}
          >
            {this.props.label}
            <div className={styles.arrow} />
          </div>
        )}
        {this.props.isOpen && (
          <div className={styles.openHeader}>
            <div
              className={styles.backArrow}
              onClick={() => {
                this.closeItem();
              }}
            />
            {this.props.label}
            <div className={styles.viewButton}>
              <ColourButton
                label="View all"
                colour="#ff1744"
                onClick={() => {
                  this.handleViewAll();
                }}
              />
            </div>
          </div>
        )}
        {this.props.isOpen && (
          <div className={styles.content}>{this.props.children}</div>
        )}
      </div>
    );
  }
}
CategoryL1.propTypes = {
  label: PropTypes.string,
  isOpen: PropTypes.bool
};
