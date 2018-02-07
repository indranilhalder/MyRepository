import React from "react";
import styles from "./Address.css";
import PropTypes from "prop-types";

export default class Address extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let classActive = styles.addressHolder;
    if (this.props.selected) {
      classActive = styles.addressHolderActive;
    }

    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={classActive}>
          <div className={styles.heading}>{this.props.heading}</div>
          <div className={styles.text}>{this.props.address}</div>
        </div>
      </div>
    );
  }
}
Address.propTypes = {
  text: PropTypes.string,
  heading: PropTypes.string,
  address: PropTypes.string,
  selectItem: PropTypes.func,
  selected: PropTypes.bool
};
