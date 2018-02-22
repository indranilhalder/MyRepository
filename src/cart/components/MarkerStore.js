import React from "react";
import styles from "./MarkerStore.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class MarkerStore extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className="place" lat={this.props.lat} lng={this.props.lng}>
          <Icon image={this.props.image} size={20} />
        </div>
      </div>
    );
  }
}
MarkerStore.propTypes = {
  lat: PropTypes.number,
  lng: PropTypes.number,
  image: PropTypes.string
};
