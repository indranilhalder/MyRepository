import React from "react";
import PropTypes from "prop-types";
import styles from "./MoreBrands.css";
import Button from "../../general/components/Button.js";
import { Icon } from "xelpmoc-core";
import plusIcon from "../../general/components/img/circle_plus_white.svg";
import ButtonWithIcon from "../../general/components/ButtonWithIcon.js";
export default class MoreBrands extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headerText}>
          See the latest products from your favourite Brand.
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <ButtonWithIcon
              backgroundColor="#ff1744"
              height={40}
              label="Checkout"
              width={150}
              textStyle={{ color: "#FFF", fontSize: 14 }}
              onClick={() => this.handleClick()}
              icon={{
                height: 20,
                width: 20,
                offset: 5,
                element: <Icon image={plusIcon} size={20} />
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}
