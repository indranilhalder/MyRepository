import React from "react";
import GoogleMapReact from "google-map-react";

export default class Map extends React.Component {
  static defaultProps = {
    center: { lat: 59.95, lng: 30.33 },
    zoom: 20
  };

  render() {
    return (
      <GoogleMapReact
        bootstrapURLKeys={{ key: ["AIzaSyAqkKpQBlLTJwxAjtSKe3Dz7-GUn9xPfd8"] }}
        defaultCenter={this.props.center}
        defaultZoom={this.props.zoom}
        center={{ lat: this.props.lat, lng: this.props.lng }}
      >
        {/* <AnyReactComponent
          lat={59.955413}
          lng={30.337844}
          text={"Kreyser Amapvrora"}
        /> */}
        {this.props.children}
      </GoogleMapReact>
    );
  }
}
