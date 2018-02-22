import React from "react";
import styles from "./MarkerStoreCenter.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class MarkerStoreCenter extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        c
        <div className={styles.image} lat={this.props.lat} lng={this.props.lng}>
          <Icon image={this.props.image} size={20} />
        </div>
      </div>
    );
  }
}

MarkerStoreCenter.propTypes = {
  lat: PropTypes.number,
  lng: PropTypes.number,
  image: PropTypes.string
};
