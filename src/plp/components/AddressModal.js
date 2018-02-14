import React from "react";
import AddressCarousel from "./AddressCarousel";
import BottomSlideModal from "../../general/components/BottomSlideModal";
import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import PropTypes from "prop-types";
import styles from "./AddressModal.css";

export default class AddressModal extends React.Component {
  render() {
    return (
      <BottomSlideModal>
        <div className={styles.base}>
          <div className={styles.searchHolder}>
            <SearchAndUpdate />
          </div>
          {this.props.data && <AddressCarousel data={this.props.data} />}
        </div>
      </BottomSlideModal>
    );
  }
}
AddressModal.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      heading: PropTypes.string,
      address: PropTypes.string,
      value: PropTypes.string
    })
  )
};
AddressModal.defaultProps = {
  data: [
    {
      heading: "Home",
      address:
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ",
      value: "Home"
    },
    {
      heading: "Office",
      address:
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ",
      value: "Office"
    },
    {
      heading: "Home2",
      address:
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ",
      value: "Home2"
    },
    {
      heading: "Home3",
      address:
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ",
      value: "Home3"
    },
    {
      heading: "Home4",
      address:
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ",
      value: "Home4"
    }
  ]
};
