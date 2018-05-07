import React from "react";
import InformationHeader from "../../general/components/InformationHeader";
import PiqPage from "../../cart/components/PiqPage";
import styles from "./PiqPageForPdp.css";
export default class PiqPageForPdp extends React.Component {
  render() {
    const firstSlaveData = this.props.productDetails.slaveData;
    const someData = firstSlaveData
      .map(slaves => {
        return (
          slaves.CNCServiceableSlavesData &&
          slaves.CNCServiceableSlavesData.map(slave => {
            return (
              slave &&
              slave.serviceableSlaves.map(serviceableSlave => {
                return serviceableSlave;
              })
            );
          })
        );
      })
      .map(val => {
        return (
          val &&
          val.map(v => {
            return v;
          })
        );
      });

    const allStoreIds = [].concat
      .apply([], [].concat.apply([], someData))
      .map(store => {
        return store && store.slaveId;
      });
    const availableStores = this.props.stores
      ? this.props.stores.filter(val => {
          return allStoreIds.includes(val.slaveId);
        })
      : [];
    console.log(availableStores);
    return (
      <div className={styles.piqPageHolder}>
        <div className={styles.piqHeaderHolder}>
          <InformationHeader
            goBack={() => {
              this.props.hidePdpPiqPage();
            }}
            text="CLiQ & PiQ"
          />
        </div>
        <PiqPage
          availableStores={availableStores}
          numberOfStores={availableStores.length}
          showPickupPerson={false}
          productName={this.props.productDetails.productName}
          canSelectStore={false}
          pinCodeUpdateDisabled={true}
          changePincode={pincode =>
            this.props.getAllStoresForCliqAndPiq(pincode)
          }
          goBack={() => this.props.removeCliqAndPiq()}
        />
        {this.props.loadingForCliqAndPiq && (
          <div className={styles.loaderSection}>
            <div className={styles.loader} />
          </div>
        )}
      </div>
    );
  }
}
