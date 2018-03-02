import React from "react";
import CircleProductImage from "./CircleProductImage";
import PropTypes from "prop-types";
import styles from "./CategoryWithName.css";

export default class CategoryWithName extends React.Component {
  onClick = () => {
    if (this.props.onClick) {
      this.props.onClick(this.props.value);
    }
  };
  render() {
    return (
      <div className={styles.circleWithText} onClick={this.onClick}>
        <CircleProductImage image={this.props.image} />
        <div className={styles.label}>{this.props.label}</div>
      </div>
    );
  }
}
CategoryWithName.propTypes = {
  label: PropTypes.string,
  image: PropTypes.string,
  value: PropTypes.string,
  onClick: PropTypes.func
};
