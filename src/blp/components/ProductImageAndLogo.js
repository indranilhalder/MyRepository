import React from "react";
import styles from "./ProductImageAndLogo.css";
import Image from "../../xelpmoc-core/Image";
import Logo from "../../general/components/Logo";
import PropTypes from "prop-types";
export default class ProductImageAndLogo extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={styles.imageCircleHolder}>
          <div className={styles.imageHolder}>
            <Image image={this.props.imageUrl} fit="cover" />
          </div>
        </div>
        <div className={styles.logonHolder}>
          <div className={styles.companyIcon}>
            <Logo image={this.props.logo} imageWidth="75%" imageHeight="auto" />
          </div>
        </div>
      </div>
    );
  }
}
ProductImageAndLogo.propTypes = {
  onClick: PropTypes.func,
  imageUrl: PropTypes.string,
  logo: PropTypes.string
};
