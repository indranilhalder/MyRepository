import React from "react";
import styles from "./LocationIconCard.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";

export default class LocationIconCard extends React.Component {
  render() {
    console.log(this.props.ImageUrl);
    return (
      <div className={styles.base}>
        <div className={styles.iconHolder}>
          <Icon image={this.props.image} />
        </div>
        <div className={styles.textHolder}>{this.props.text}</div>
        {this.props.children}
      </div>
    );
  }
}
LocationIconCard.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string
};
