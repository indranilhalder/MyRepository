import React from "react";
import Map from "../../general/components/Map";
import MarkerStore from "./MarkerStore";
import InformationHeader from "../../general/components/InformationHeader";
import BannerMobile from "../../general/components/BannerMobile";
import PickUpLocation from "./PickUpLocation";
import SearchLocationByPincode from "./SearchLocationByPincode";
import styles from "./PiqPage.css";

export default class PiqPage extends React.Component {
  render() {
    const data = {
      stores: [
        {
          active: "Y",
          address: {
            city: "Delh",
            country: {
              isocode: "IN"
            },
            defaultAddress: false,
            id: "8801080377367",
            line1: "Mountroad",
            line2: "Delhi",
            postalCode: "110022"
          },
          clicknCollect: "Y",
          displayName: "Tamilselvan",
          email0: "tamilselvan.mp@tcs.com",
          footFall: "100",
          geoPoint: {
            latitude: 28.564058,
            longitude: 77.1736968
          },
          isReturnable: "Y",
          location: "mall",
          managerName: "Tamil",
          managerPhone: "9896545646",
          mplClosingTime: "22:00",
          mplOpeningTime: "10:00",
          mplWorkingDays: "1,2,3,4,5,6",
          name: "Tamilselvan",
          normalRetailSalesOfStore: "200000",
          orderAcceptanceTAT: 0,
          orderProcessingTAT: 0,
          ownerShip: "franchise",
          parkingAvailable: "Y",
          phoneNo0: "09986955456",
          returnAddress1: "Mountroad",
          returnAddress2: "Delhi",
          returnCity: "Delh",
          returnPin: "110022",
          returnState: "30",
          returnstoreID: "800112-789654",
          sellerId: "800112",
          slaveId: "800112-789654",
          storeContactNumber: "09665869545",
          storeSize: "1750"
        },
        {
          active: "Y",
          address: {
            city: "NEW DELHI",
            country: {
              isocode: "IN"
            },
            defaultAddress: false,
            id: "8802784804887",
            line1: "tower 2, ebs, kurla",
            line2: "delhi",
            postalCode: "110049"
          },
          clicknCollect: "Y",
          displayName: "BestSeller New Delhi South Ext",
          email0: "appstuat4@gmail.com",
          geoPoint: {
            latitude: 28.5666032,
            longitude: 77.2201326
          },
          isReturnable: "N",
          location: "mall",
          managerName: "Bestseller New Delhi South Ext",
          mplClosingTime: "21:00",
          mplOpeningTime: "11:00",
          mplWorkingDays: "1,2,3,4,5,6,0",
          name: "800059-851059",
          orderAcceptanceTAT: 0,
          orderProcessingTAT: 0,
          parkingAvailable: "Y",
          phoneNo0: "98209070004",
          returnAddress1: "tower 5, ebs, kurla",
          returnAddress2: "mumbai",
          returnCity: "MUMBAI",
          returnPin: "400076",
          returnState: "13",
          returnstoreID: "800059-840059",
          sellerId: "800059",
          slaveId: "800059-851059",
          storeContactNumber: "09222222522"
        },
        {
          active: "Y",
          address: {
            city: "NEW DELHI",
            country: {
              isocode: "IN"
            },
            defaultAddress: false,
            id: "8802784804887",
            line1: "tower 2, ebs, kurla",
            line2: "delhi",
            postalCode: "110049"
          },
          clicknCollect: "Y",
          displayName: "BestSeller New Delhi South Ext",
          email0: "appstuat4@gmail.com",
          geoPoint: {
            latitude: 28.5666032,
            longitude: 77.2201326
          },
          isReturnable: "N",
          location: "mall",
          managerName: "Bestseller New Delhi South Ext",
          mplClosingTime: "21:00",
          mplOpeningTime: "11:00",
          mplWorkingDays: "1,2,3,4,5,6,0",
          name: "800059-851059",
          orderAcceptanceTAT: 0,
          orderProcessingTAT: 0,
          parkingAvailable: "Y",
          phoneNo0: "98209070004",
          returnAddress1: "tower 5, ebs, kurla",
          returnAddress2: "mumbai",
          returnCity: "MUMBAI",
          returnPin: "400076",
          returnState: "13",
          returnstoreID: "800059-840059",
          sellerId: "800059",
          slaveId: "800059-851059",
          storeContactNumber: "09222222522"
        }
      ]
    };

    return (
      <div className={styles.base}>
        <div className={styles.map}>
          <Map lat={20.5937} lng={78.9629} zoom={9}>
            <MarkerStore
              lat={59.955413}
              lng={30.337844}
              image="https://lh3.googleusercontent.com/UMB2HRRRAAzXAEaCM9Gg-baCaDx_1RTXHscW5k2Ge3P4KP4mwTt2m6oyEHBWex3c4SxU=w300"
            />
            <MarkerStore
              lat={60.1}
              lng={30.437844}
              image="https://lh3.googleusercontent.com/UMB2HRRRAAzXAEaCM9Gg-baCaDx_1RTXHscW5k2Ge3P4KP4mwTt2m6oyEHBWex3c4SxU=w300"
            />
            <MarkerStore
              lat={60.2}
              lng={30.437844}
              image="https://lh3.googleusercontent.com/UMB2HRRRAAzXAEaCM9Gg-baCaDx_1RTXHscW5k2Ge3P4KP4mwTt2m6oyEHBWex3c4SxU=w300"
            />
            <MarkerStore
              lat={60.3}
              lng={30.437844}
              image="https://lh3.googleusercontent.com/UMB2HRRRAAzXAEaCM9Gg-baCaDx_1RTXHscW5k2Ge3P4KP4mwTt2m6oyEHBWex3c4SxU=w300"
            />
            <MarkerStore
              lat={60.4}
              lng={30.437844}
              image="https://lh3.googleusercontent.com/UMB2HRRRAAzXAEaCM9Gg-baCaDx_1RTXHscW5k2Ge3P4KP4mwTt2m6oyEHBWex3c4SxU=w300"
            />
          </Map>
        </div>
        <div className={styles.header}>
          <InformationHeader text="CLiQ and PiQ" />
        </div>
        <div className={styles.location}>
          <SearchLocationByPincode
            header="Iphone 7 Plus 128GB Jet Black "
            pincode="400240"
          />
        </div>
        <div className={styles.bannerMobileHolder}>
          <BannerMobile>
            {data.stores.map((val, i) => {
              console.log(val);
              return (
                <PickUpLocation
                  key={i}
                  address={`${val.returnAddress1} ${val.returnAddress2}, `}
                  PickUpKey="Store Timings"
                  //  PickUpValue={val.PickUpValue}
                  workingDays={val.mplWorkingDays}
                  openingTime={val.mplOpeningTime}
                  closingTime={val.mplClosingTime}
                  address2={`${val.returnCity} ${val.returnPin}`}
                  iconText="C"
                  headingText={val.displayName}
                  buttonText="Select"
                  // onClick={() => {
                  //   this.onMoveToNextPage(val.index);
                  // }}
                />
              );
            })}
          </BannerMobile>
        </div>
      </div>
    );
  }
}
