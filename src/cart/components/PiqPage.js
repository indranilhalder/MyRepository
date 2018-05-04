import React from "react";
import Map from "../../general/components/Map";
import MarkerStore from "./MarkerStore";
import BannerMobile from "../../general/components/BannerMobile";
import PickUpLocation from "./PickUpLocation";
import GetLocationDetails from "./GetLocationDetails";
import PickUpDetails from "./PickUpDetails";
import SearchLocationByPincode from "./SearchLocationByPincode";
import styles from "./PiqPage.css";
import WestSideIcon from "./img/googleSearch.png";

export default class PiqPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      lat:
        props.availableStores && props.availableStores[0]
          ? props.availableStores[0].geoPoint.latitude
          : 22.575229,
      lng:
        props.availableStores && props.availableStores[0]
          ? props.availableStores[0].geoPoint.longitude
          : 88.468341,
      position: 0,
      selected: false,
      selectedAddress: "",
      selectedStoreTime: "",
      workingDays: "",
      openingTime: "",
      closingTime: "",
      headingText: "",
      displayName: "",
      name: "",
      mobile: ""
    };
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.userDetails) {
      this.setState({
        name: nextProps.userDetails && nextProps.userDetails.firstName,
        mobile: nextProps.userDetails && nextProps.userDetails.mobileNumber
      });
    }
  }
  componentDidMount = () => {
    if (this.props.getUserDetails) {
      this.props.getUserDetails();
    }
  };
  handleSwipe(val) {
    const lat = this.props.availableStores[val % this.props.numberOfStores]
      .geoPoint.latitude;
    const lng = this.props.availableStores[val % this.props.numberOfStores]
      .geoPoint.longitude;
    this.setState({ lat, lng });
  }
  getValue(val) {
    this.setState(val);
  }
  selectStore(slaveId) {
    this.props.addStoreCNC(slaveId);
  }
  changeStore() {
    this.props.hidePickupPersonDetail();
  }
  handleSubmit() {
    if (this.props.addPickupPersonCNC) {
      this.props.addPickupPersonCNC(this.state.mobile, this.state.name);
    }
  }
  render() {
    let selectedStore = {};
    if (this.props.availableStores) {
      selectedStore = this.props.availableStores.find(store => {
        return store.slaveId === this.props.selectedSlaveId;
      });
    }

    return (
      <div className={styles.base}>
        <div className={styles.map}>
          <Map lat={this.state.lat} lng={this.state.lng} zoom={16}>
            {this.props.availableStores &&
              this.props.availableStores.map((val, i) => {
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

        <div className={styles.location}>
          <SearchLocationByPincode
            header={`${this.props.productName} ${this.props.productColour}`}
            pincode={this.props.pincode}
            changePincode={pincode => this.props.changePincode(pincode)}
          />
        </div>
        <div className={styles.bannerMobileHolder}>
          {this.props.availableStores &&
            this.props.availableStores.length > 1 && (
              <React.Fragment>
                {!this.props.showPickupPerson &&
                  this.props.availableStores && (
                    <BannerMobile
                      onSwipe={val => this.handleSwipe(val)}
                      bannerHeight="55vw"
                    >
                      {this.props.availableStores.map((val, i) => {
                        return (
                          <PickUpLocation
                            key={i}
                            address={`${val.address.line1} ${
                              val.address.line2
                            }, `}
                            PickUpKey="Open on: "
                            workingDays={val.mplWorkingDays}
                            openingTime={val.mplOpeningTime}
                            closingTime={val.mplClosingTime}
                            address2={`${val.returnCity} ${val.returnPin}`}
                            iconText="C"
                            headingText={val.displayName}
                            buttonText="Select"
                            onClick={() => {
                              this.selectStore(val.slaveId);
                            }}
                          />
                        );
                      })}
                    </BannerMobile>
                  )}
              </React.Fragment>
            )}
          {this.props.availableStores &&
            this.props.availableStores.length === 1 &&
            !this.props.showPickupPerson && (
              <div className={styles.singleCardHolder}>
                {this.props.availableStores &&
                  this.props.availableStores.map((val, i) => {
                    return (
                      <PickUpLocation
                        key={i}
                        address={`${val.address.line1} ${val.address.line2}, `}
                        PickUpKey="Open on: "
                        workingDays={val.mplWorkingDays}
                        openingTime={val.mplOpeningTime}
                        closingTime={val.mplClosingTime}
                        address2={`${val.returnCity} ${val.returnPin}`}
                        iconText="C"
                        headingText={val.displayName}
                        buttonText="Select"
                        onClick={() => {
                          this.selectStore(val.slaveId);
                        }}
                      />
                    );
                  })}
              </div>
            )}
          {this.props.showPickupPerson && (
            <div className={styles.getLocationDetailsHolder}>
              <div className={styles.getLocationDetails}>
                <GetLocationDetails
                  changeLocation={() => {
                    this.changeStore();
                  }}
                  headingText={selectedStore.displayName}
                  address={`${selectedStore.returnAddress1} ${
                    selectedStore.returnAddress2
                  } ${selectedStore.returnCity}`}
                  pickUpKey="Open on: "
                  pickUpValue={selectedStore.selectedStoreTime}
                  workingDays={selectedStore.mplWorkingDays}
                  openingTime={selectedStore.mplOpeningTime}
                  closingTime={selectedStore.mplClosingTime}
                />
              </div>
              <div className={styles.pickUpDetails}>
                <PickUpDetails
                  getValue={val => this.getValue(val)}
                  onSubmit={() => this.handleSubmit()}
                  name={this.state.name}
                  mobile={this.state.mobile}
                />
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}
