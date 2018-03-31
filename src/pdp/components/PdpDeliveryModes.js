import React from "react";
import DeliveryInformation from "../../general/components/DeliveryInformations";
import { EXPRESS, COLLECT, HOME_DELIVERY } from "../../lib/constants";
import styles from "./PdpDeliveryModes.css";
export default class PdpDeliveryModes extends React.Component {
  render() {
    const eligibleDeliveryModes = this.props.eligibleDeliveryModes;
    const deliveryModesATP = this.props.deliveryModesATP;
    return (
      <div className={styles.base}>
        <DeliveryInformation
          type={HOME_DELIVERY}
          available={eligibleDeliveryModes
            .map(val => {
              return val.code;
            })
            .includes(HOME_DELIVERY)}
          placedTime={
            deliveryModesATP
              .filter(val => {
                return val.key === HOME_DELIVERY;
              })
              .map(val => {
                return val.value;
              })[0]
          }
        />
        <DeliveryInformation
          type={EXPRESS}
          available={eligibleDeliveryModes
            .map(val => {
              return val.code;
            })
            .includes(EXPRESS)}
          placedTime={
            deliveryModesATP
              .filter(val => {
                return val.key === EXPRESS;
              })
              .map(val => {
                return val.value;
              })[0]
          }
        />
        <DeliveryInformation
          type={COLLECT}
          available={eligibleDeliveryModes
            .map(val => {
              return val.code;
            })
            .includes(COLLECT)}
          showCliqAndPiqButton={false}
        />
      </div>
    );
  }
}
