import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandsIconWithToolTip.css";
import Logo from "../../general/components/Logo";
export default class BrandsIconWithToolTip extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={styles.brandsIconHolder}>
          <Logo image={this.props.logo} />
        </div>
      </div>
    );
  }
}
