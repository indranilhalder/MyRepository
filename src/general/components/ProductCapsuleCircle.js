import React from "react";
import styles from "./ProductCapsuleCircle.css";
import PropTypes from "prop-types";

export default class ProductCapsuleCircle extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick}>
        <div
          className={styles.imageHolder}
          style={{
            backgroundImage: `url(${this.props.image})`,
            backgroundSize: "cover",
            backgroundPosition: "top center"
          }}
        />
      </div>
    );
  }
}
ProductCapsuleCircle.propTypes = {
  onClick: PropTypes.func
};
