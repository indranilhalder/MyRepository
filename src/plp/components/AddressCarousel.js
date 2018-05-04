import React from "react";
import styles from "./AddressCarousel.css";
import PropTypes from "prop-types";
import Address from "./Address.js";
import DumbCarousel from "../../general/components/DumbCarousel";

export default class AddressCarousel extends React.Component {
  render() {
    let data = this.props.data;
    return (
      <div className={styles.base}>
        <DumbCarousel
          limit={1}
          headerComponent={
            <div className={styles.header}>{this.props.text}</div>
          }
          elementWidth={48}
        >
          {data &&
            data.length > 0 &&
            data.map((datum, i) => {
              return (
                <Address
                  key={i}
                  heading={datum.addressType}
                  address={`${datum.line1 ? datum.line1 : ""} ${
                    datum.town ? datum.town : ""
                  } ${datum.city ? datum.city : ""}, ${
                    datum.state ? datum.state : ""
                  } ${datum.postalCode ? datum.postalCode : ""}`}
                  value={datum.postalCode}
                  selectItem={pincode =>
                    this.props.selectAddress(datum.postalCode)
                  }
                />
              );
            })}
        </DumbCarousel>
      </div>
    );
  }
}
AddressCarousel.propTypes = {
  text: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      heading: PropTypes.string,
      address: PropTypes.string,
      value: PropTypes.string
    })
  )
};
AddressCarousel.defaultProps = {
  text: "Select from your saved addresses."
};
