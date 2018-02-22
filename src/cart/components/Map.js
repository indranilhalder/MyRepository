import React from "react";
import GoogleMapReact from "google-map-react";
import styles from "./Map.css";
import MarkerStore from "./MarkerStore";
import MarkerStoreCenter from "./MarkerStoreCenter";
import PropTypes from "prop-types";
import PickUpLocation from "./PickUpLocation";
import BannerMobile from "../../general/components/BannerMobile";
export default class SimpleMap extends React.Component {
  state = {
    center: [20.5937, 78.9629],
    zoom: 9
  };
  _onChange = ({ center, zoom }) => {
    this.setState({
      center: center,
      zoom: zoom
    });
  };
  onClick = (lat, lng) => {
    this.setState({
      center: [lat, lng],
      zoom: 6
    });
  };

  render() {
    return (
      <div className={styles.base}>
        <GoogleMapReact
          onChange={this._onChange}
          center={this.state.center}
          zoom={this.state.zoom}
        >
          <MarkerStore
            lat={this.props.lat}
            lng={this.props.lng}
            image={this.props.image}
          />
          <MarkerStoreCenter
            lat={this.props.lat1}
            lng={this.props.lng1}
            image={this.props.image}
          />
        </GoogleMapReact>
        <div className={styles.bannerMobileHolder}>
          <BannerMobile>
            <div className={styles.PickUpLocation}>
              <PickUpLocation
                address="Infiniti Mall, Oshiwara, Phase D, Shastri Nagar, Andheri West, Mumbai, Maharashtra 400058"
                PickUpKey="Pick up by:"
                PickUpValue="Today-10 August, by 8 PM"
                iconText="C"
                headingText="Westside South Extension"
                buttonText="Select "
                onClick={() => this.onClick(this.props.lat, this.props.lng)}
              />
            </div>
            <div className={styles.PickUpLocation}>
              <PickUpLocation
                address="Infiniti Mall, Oshiwara, Phase D, Shastri Nagar, Andheri West, Mumbai, Maharashtra 400058"
                PickUpKey="Pick up by:"
                PickUpValue="Today-10 August, by 8 PM"
                iconText="C"
                headingText="Westside South Extension"
                buttonText="Select "
                onClick={() => this.onClick(this.props.lat1, this.props.lng1)}
              />
            </div>
            <div className={styles.PickUpLocation}>
              <PickUpLocation
                address="Infiniti Mall, Oshiwara, Phase D, Shastri Nagar, Andheri West, Mumbai, Maharashtra 400058"
                PickUpKey="Pick up by:"
                PickUpValue="Today-10 August, by 8 PM"
                iconText="C"
                headingText="Westside South Extension"
                buttonText="Select "
                onClick={() => this.onClick(this.props.lat, this.props.lng)}
              />
            </div>
          </BannerMobile>
        </div>
      </div>
    );
  }
}
SimpleMap.propTypes = {
  lat: PropTypes.number,
  lng: PropTypes.number,
  image: PropTypes.string,
  center: PropTypes.array,
  zoom: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
};
