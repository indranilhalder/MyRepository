import React from "react";
import Image from "../../xelpmoc-core/Image";
import PropTypes from "prop-types";
import styles from "./BankSelect.css";
export default class BankSelect extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div>
        <div
          className={this.props.selected ? styles.selected : styles.base}
          onClick={() => {
            this.handleClick();
          }}
        >
          <div className={styles.image}>
            <Image image={this.props.image} />
          </div>
        </div>
        {this.props.name && (
          <div className={styles.name}>{this.props.name}</div>
        )}
      </div>
    );
  }
}

BankSelect.propTypes = {
  image: PropTypes.string,
  value: PropTypes.string,
  selected: PropTypes.bool,
  selectItem: PropTypes.func
};
