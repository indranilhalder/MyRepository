import React from "react";
import AddressCarousel from "./AddressCarousel";
import BottomSlideModal from "../../general/components/BottomSlideModal";
import PinCodeUpdate from "./PinCodeUpdate";
import PropTypes from "prop-types";
import styles from "./AddressModal.css";

export default class AddressModal extends React.Component {
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
AddressModal.propTypes = {
  data: PropTypes.shape({
    heading: PropTypes.string,
    address: PropTypes.string,
    value: PropTypes.string
  })
};
