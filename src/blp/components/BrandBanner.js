import React from "react";
import Logo from "../../general/components/Logo";
import styles from "./BrandBanner.css";
export default class BrandBanner extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        onClick={() => {
          this.handleClick();
        }}
      >
        <div
          className={styles.image}
          style={{ backgroundImage: `url(${this.props.image})` }}
        />
        <div className={styles.logoHolder}>
          <div className={styles.logo}>
            <Logo image={this.props.logo} />
          </div>
        </div>
      </div>
    );
  }
}
