import React from "react";
import GoogleMap from "react-google-maps";
const DEFAULT_LATITUDE = 59.95;
const DEFAULT_LONGITUDE = 30.33;
const DEFAULT_ZOOM = 20;
export default class Map extends React.Component {
  static defaultProps = {
    center: { lat: DEFAULT_LATITUDE, lng: DEFAULT_LONGITUDE },
    zoom: DEFAULT_ZOOM
  };

  render() {
    return (
      <GoogleMap
        googleMapUrl="https://maps.googleapis.com/maps/api/js?key=AIzaSyAqkKpQBlLTJwxAjtSKe3Dz7-GUn9xPfd8.exp&libraries=geometry,drawing,places"
        bootstrapURLKeys={{ key: ["AIzaSyAqkKpQBlLTJwxAjtSKe3Dz7-GUn9xPfd8"] }}
        defaultCenter={this.props.center}
        defaultZoom={this.props.zoom}
        center={{ lat: this.props.lat, lng: this.props.lng }}
        loadingElement={<div style={{ height: `100%` }} />}
        containerElement={<div style={{ height: `400px` }} />}
        mapElement={<div style={{ height: `100%` }} />}
      >
        {this.props.children}
      </GoogleMap>
    );
  }
}
