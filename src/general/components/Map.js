import React, { Component } from "react";
import GoogleMapReact from "google-map-react";
import styles from "./Map.css";
import Gift from "./img/Gift.svg";
import { Icon } from "xelpmoc-core";
export default class SimpleMap extends React.Component {
  static defaultProps = {
    center: { lat: 59.95, lng: 30.33 },
    zoom: 11
  };
  render() {
    return (
      <div className={styles.base}>
        <GoogleMapReact
          defaultCenter={this.props.center}
          defaultZoom={this.props.zoom}
        />
      </div>
    );
  }
}
