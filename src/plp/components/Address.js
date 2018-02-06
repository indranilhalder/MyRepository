import React from "react";
import styles from "./Address.css";
import PropTypes from "prop-types";
export default class Address extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let classActive = styles.addressHolder;
    if (this.props.selected) {
      classActive = styles.addressHolderActive;
    }
    let data = this.props;
    return (
      <div className={styles.base}>
        <div className={classActive} onClick={() => this.handleClick()}>
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
  onClick: PropTypes.func
};
