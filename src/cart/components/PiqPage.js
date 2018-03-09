import React from "react";
import Map from "../../general/components/Map";
import MarkerStore from "./MarkerStore";
import InformationHeader from "../../general/components/InformationHeader";
import BannerMobile from "../../general/components/BannerMobile";
import PickUpLocation from "./PickUpLocation";
import GetLocationDetails from "./GetLocationDetails";
import PickUpDetails from "./PickUpDetails";
import SearchLocationByPincode from "./SearchLocationByPincode";
import styles from "./PiqPage.css";
import WestSideIcon from "./img/westside.svg";

export default class PiqPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      lat: this.props.availableStores[0].geoPoint.latitude,
      lng: this.props.availableStores[0].geoPoint.longitude,
      position: 0,
      selected: false,
      selectedAddress: "",
      selectedStoreTime: "",
      workingDays: "",
      openingTime: "",
      closingTime: "",
      headingText: "",
      displayName: ""
    };
  }
  handleSwipe(val) {
    const lat = this.props.availableStores[val % this.props.numberOfStores]
      .geoPoint.latitude;
    const lng = this.props.availableStores[val % this.props.numberOfStores]
      .geoPoint.longitude;
    this.setState({ lat, lng });
  }
  getValue(val) {
    if (this.props.getValue) {
      this.props.getValue();
    }
  }
  selectStore(val) {
    this.setState({
      selectedAddress: `${this.props.availableStores[val].returnAddress1} ${
        this.props.availableStores[val].returnAddress2
      } ${this.props.availableStores[val].returnCity}`,
      selected: true,
      workingDays: this.props.availableStores[val].mplWorkingDays,
      openingTime: this.props.availableStores[val].mplOpeningTime,
      closingTime: this.props.availableStores[val].mplClosingTime,
      displayName: this.props.availableStores[val].displayName
    });
  }
  changeStore() {
    this.setState({ selected: false });
  }
  handleSubmit() {
    if (this.props.onSubmit) {
      this.props.onSubmit();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.map}>
          <Map lat={this.state.lat} lng={this.state.lng} zoom={18}>
            {this.props.availableStores.map((val, i) => {
              return (
                <MarkerStore
                  lat={val.geoPoint.latitude}
                  lng={val.geoPoint.longitude}
                  image={WestSideIcon}
                />
              );
            })}
          </Map>
        </div>
        <div className={styles.header}>
          <InformationHeader text="CLiQ and PiQ" />
        </div>
        <div className={styles.location}>
          <SearchLocationByPincode
            header={`${this.props.productName} ${this.props.productColour}`}
            pincode={this.props.pincode}
          />
        </div>
        <div className={styles.bannerMobileHolder}>
          {!this.state.selected && (
            <BannerMobile
              onSwipe={val => this.handleSwipe(val)}
              bannerHeight="55vw"
            >
              {this.props.availableStores.map((val, i) => {
                return (
                  <PickUpLocation
                    key={i}
                    address={`${val.returnAddress1} ${val.returnAddress2}, `}
                    PickUpKey="Open on: "
                    workingDays={val.mplWorkingDays}
                    openingTime={val.mplOpeningTime}
                    closingTime={val.mplClosingTime}
                    address2={`${val.returnCity} ${val.returnPin}`}
                    iconText="C"
                    headingText={val.displayName}
                    buttonText="Select"
                    onClick={() => {
                      this.selectStore(i);
                    }}
                  />
                );
              })}
            </BannerMobile>
          )}
          {this.state.selected && (
            <div className={styles.getLocationDetailsHolder}>
              <div className={styles.getLocationDetails}>
                <GetLocationDetails
                  changeLocation={() => {
                    this.changeStore();
                  }}
                  headingText={this.state.displayName}
                  address={this.state.selectedAddress}
                  pickUpKey="Open on: "
                  pickUpValue={this.state.selectedStoreTime}
                  workingDays={this.state.workingDays}
                  openingTime={this.state.openingTime}
                  closingTime={this.state.closingTime}
                />
              </div>
              <div className={styles.pickUpDetails}>
                <PickUpDetails
                  getValue={val => this.getValue(val)}
                  onSubmit={() => this.handleSubmit()}
                />
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}
