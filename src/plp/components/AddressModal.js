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

          <AddressCarousel
            data={[
              {
                heading: "Home",
                address:
                  "Ranka Colony Rd, Munivenkatppa Layout, Bilekahalli, Bengaluru, Karnataka 560076",
                value: "one"
              },
              {
                heading: "Office",
                address:
                  "4th Floor, Agies Building, #17, 1st A Cross, Koramangala 5th Block, Bengaluru, Karnataka 560095",
                value: "two"
              },
              {
                heading: "Hotel",
                address:
                  "4th Floor, Agies Building, #17, 1st A Cross, Koramangala 5th Block, Bengaluru, Karnataka 560095",
                value: "three"
              }
            ]}
          />
        </div>
      </BottomSlideModal>
    );
  }
}
