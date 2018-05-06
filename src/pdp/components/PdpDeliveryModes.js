import React from "react";
import DeliveryInformation from "../../general/components/DeliveryInformations";
import { EXPRESS, COLLECT, HOME_DELIVERY } from "../../lib/constants";
import PropTypes from "prop-types";
import styles from "./PdpDeliveryModes.css";
export default class PdpDeliveryModes extends React.Component {
  render() {
    const eligibleDeliveryModes = this.props.eligibleDeliveryModes;
    const deliveryModesATP = this.props.deliveryModesATP;
    if (!eligibleDeliveryModes || !deliveryModesATP) {
      return null;
    }
    return (
      <div className={styles.base}>
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
          onPiq={this.props.getAllStoreForCliqAndPiq}
          type={COLLECT}
          available={eligibleDeliveryModes
            .map(val => {
              return val.code;
            })
            .includes(COLLECT)}
          showCliqAndPiqButton={false}
          isClickable={true}
        />
      </div>
    );
  }
}
PdpDeliveryModes.propTypes = {
  eligibleDeliveryModes: PropTypes.arrayOf(
    PropTypes.shape({
      code: PropTypes.oneOf([EXPRESS, COLLECT, HOME_DELIVERY]),
      name: PropTypes.string
    })
  ),
  deliveryModesATP: PropTypes.arrayOf(
    PropTypes.shape({
      key: PropTypes.oneOf([EXPRESS, COLLECT, HOME_DELIVERY]),
      value: PropTypes.string
    })
  )
};
