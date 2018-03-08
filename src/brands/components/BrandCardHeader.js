import React from "react";
import { Image } from "xelpmoc-core";
import styles from "./BrandCardHeader.css";
import Logo from "../../general/components/Logo";
import CoreButton from "../../general/components/Button";
import PropTypes from "prop-types";
export default class BrandCardHeader extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      buttonLabel: props.feedComponentData.buttonLabel
    };
  }
  handleClick() {
    if (this.state.buttonLabel === "Unfollow") {
      this.setState({ buttonLabel: "Follow" }, () =>
        this.props.onClickFollow()
      );
    } else {
      this.setState({ buttonLabel: "Unfollow" }, () =>
        this.props.onClickUnfollow()
      );
    }
  }
  render() {
    let { feedComponentData } = this.props;
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <div className={styles.imageHolder}>
            <Image
              image={feedComponentData && feedComponentData.items[0].imageURL}
            />
            <div className={styles.textAndLogoContainer}>
              <div className={styles.logo}>
                <Logo
                  image={
                    feedComponentData && feedComponentData.items[0].brandLogo
                  }
                />
              </div>
              <div className={styles.text}>
                {feedComponentData && feedComponentData.items[0].title}
              </div>
            </div>
            <div className={styles.buttonHolder}>
              {/* Need to be uncommented when the follow and unFollow api will work */}

              {/* <div className={styles.button}>
                <CoreButton
                  width={100}
                  height={36}
                  backgroundColor={"transparent"}
                  borderRadius={100}
                  borderColor={"#FFFFFF"}
                  label={this.state.buttonLabel}
                  textStyle={{
                    color: "#FFFFFF",
                    fontSize: 14,
                    fontFamily: "semibold"
                  }}
                  onClick={() => this.handleClick()}
                />
              </div> */}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
BrandCardHeader.propTypes = {
  backgroundImageURL: PropTypes.string,
  description: PropTypes.string,
  logoImage: PropTypes.string,
  buttonLabel: PropTypes.string,
  onClick: PropTypes.func
};
BrandCardHeader.defaultProps = {
  image: "",
  text: "",
  logo: "",
  buttonLabel: "Unfollow"
};

// const name = {
//   stores: [
//     {
//       active: "Y",
//       address: {
//         city: "Delh",
//         country: {
//           isocode: "IN"
//         },
//         defaultAddress: false,
//         id: "8801080377367",
//         line1: "Mountroad",
//         line2: "Delhi",
//         postalCode: "110022"
//       },
//       clicknCollect: "Y",
//       displayName: "Tamilselvan",
//       email0: "tamilselvan.mp@tcs.com",
//       footFall: "100",
//       geoPoint: {
//         latitude: 28.564058,
//         longitude: 77.1736968
//       },
//       isReturnable: "Y",
//       location: "mall",
//       managerName: "Tamil",
//       managerPhone: "9896545646",
//       mplClosingTime: "22:00",
//       mplOpeningTime: "10:00",
//       mplWorkingDays: "1,2,3,4,5,6",
//       name: "Tamilselvan",
//       normalRetailSalesOfStore: "200000",
//       orderAcceptanceTAT: 0,
//       orderProcessingTAT: 0,
//       ownerShip: "franchise",
//       parkingAvailable: "Y",
//       phoneNo0: "09986955456",
//       returnAddress1: "Mountroad",
//       returnAddress2: "Delhi",
//       returnCity: "Delh",
//       returnPin: "110022",
//       returnState: "30",
//       returnstoreID: "800112-789654",
//       sellerId: "800112",
//       slaveId: "800112-789654",
//       storeContactNumber: "09665869545",
//       storeSize: "1750"
//     },
//     {
//       active: "Y",
//       address: {
//         city: "NEW DELHI",
//         country: {
//           isocode: "IN"
//         },
//         defaultAddress: false,
//         id: "8802784804887",
//         line1: "tower 2, ebs, kurla",
//         line2: "delhi",
//         postalCode: "110049"
//       },
//       clicknCollect: "Y",
//       displayName: "BestSeller New Delhi South Ext",
//       email0: "appstuat4@gmail.com",
//       geoPoint: {
//         latitude: 28.5666032,
//         longitude: 77.2201326
//       },
//       isReturnable: "N",
//       location: "mall",
//       managerName: "Bestseller New Delhi South Ext",
//       mplClosingTime: "21:00",
//       mplOpeningTime: "11:00",
//       mplWorkingDays: "1,2,3,4,5,6,0",
//       name: "800059-851059",
//       orderAcceptanceTAT: 0,
//       orderProcessingTAT: 0,
//       parkingAvailable: "Y",
//       phoneNo0: "98209070004",
//       returnAddress1: "tower 5, ebs, kurla",
//       returnAddress2: "mumbai",
//       returnCity: "MUMBAI",
//       returnPin: "400076",
//       returnState: "13",
//       returnstoreID: "800059-840059",
//       sellerId: "800059",
//       slaveId: "800059-851059",
//       storeContactNumber: "09222222522"
//     },
//     {
//       active: "Y",
//       address: {
//         city: "NEW DELHI",
//         country: {
//           isocode: "IN"
//         },
//         defaultAddress: false,
//         id: "8802784804887",
//         line1: "tower 2, ebs, kurla",
//         line2: "delhi",
//         postalCode: "110049"
//       },
//       clicknCollect: "Y",
//       displayName: "BestSeller New Delhi South Ext",
//       email0: "appstuat4@gmail.com",
//       geoPoint: {
//         latitude: 28.5666032,
//         longitude: 77.2201326
//       },
//       isReturnable: "N",
//       location: "mall",
//       managerName: "Bestseller New Delhi South Ext",
//       mplClosingTime: "21:00",
//       mplOpeningTime: "11:00",
//       mplWorkingDays: "1,2,3,4,5,6,0",
//       name: "800059-851059",
//       orderAcceptanceTAT: 0,
//       orderProcessingTAT: 0,
//       parkingAvailable: "Y",
//       phoneNo0: "98209070004",
//       returnAddress1: "tower 5, ebs, kurla",
//       returnAddress2: "mumbai",
//       returnCity: "MUMBAI",
//       returnPin: "400076",
//       returnState: "13",
//       returnstoreID: "800059-840059",
//       sellerId: "800059",
//       slaveId: "800059-851059",
//       storeContactNumber: "09222222522"
//     }
//   ]
// };
