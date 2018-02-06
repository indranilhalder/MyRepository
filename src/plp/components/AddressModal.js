import React from "react";
import AddressCarousel from "./AddressCarousel";
import BottomSlideModal from "../../general/components/BottomSlideModal";
import PinCodeUpdate from "./PinCodeUpdate";
import styles from "./AddressModal.css";

export default class Address extends React.Component {
  render() {
    return (
      <BottomSlideModal>
        <div className={styles.base}>
          <div className={styles.searchHolder}>
            <PinCodeUpdate />
          </div>
          {this.props.data && <AddressCarousel data={this.props.data} />}
        </div>
      </BottomSlideModal>
    );
  }
}
