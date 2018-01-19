import React from "react";
import styles from "./SingleSelect.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";

export default class SingleSelect extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick(this.props.text);
    }
  }
  render() {
    return (
      <div>
        <div className={styles.base} onClick={() => this.handleClick()}>
          <div className={styles.imageHolder}>
            <Image image={this.props.image} color="transparent" />
          </div>
        </div>
        <div className={styles.text}>{this.props.text}</div>
      </div>
    );
  }
}
SingleSelect.propTypes = {
  text: PropTypes.string,
  image: PropTypes.string,
  onClick: PropTypes.func
};
