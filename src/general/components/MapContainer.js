import React from "react";
import { Map, InfoWindow, Marker, GoogleApiWrapper } from "google-maps-react";
import { CircleButton } from "xelpmoc-core";
export class MapContainer extends React.Component {
  render() {
    return (
      <Map google={this.props.google} zoom={14}>
        <Marker name={"Current location"} lat={72.2} lng={84.6} />
        {/*

        <InfoWindow onClose={this.onInfoWindowClose}>
          <div>
            <h1>lol</h1>
          </div>
        </InfoWindow> */}
        <CircleButton lat={72.2} lng={84.6} />
      </Map>
    );
  }
}

export default GoogleApiWrapper({
  apiKey: "AIzaSyAqkKpQBlLTJwxAjtSKe3Dz7-GUn9xPfd8"
})(MapContainer);
