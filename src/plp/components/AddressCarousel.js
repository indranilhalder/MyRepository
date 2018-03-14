import React from "react";
import styles from "./AddressCarousel.css";
import PropTypes from "prop-types";
import Address from "./Address.js";
import CarouselWithSelect from "../../general/components/CarouselWithSelect";

export default class AddressCarousel extends React.Component {
  render() {
    let data = this.props.data;
    return (
      <div className={styles.base}>
        <CarouselWithSelect
          limit={1}
          headerComponent={
            <div className={styles.header}>{this.props.text}</div>
          }
          elementWidthDesktop={20}
          elementWidthMobile={48}
          onSelect={pincode => this.props.selectAddress(pincode[0])}
        >
          {data &&
            data.length > 1 &&
            data.map((datum, i) => {
              return (
                <Address
                  key={i}
                  heading={datum.addressType}
                  address={`${datum.line1} ${datum.town} ${datum.city}, ${
                    datum.state
                  } ${datum.postalCode}`}
                  value={datum.postalCode}
                />
              );
            })}
        </CarouselWithSelect>
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
  text: "Select from your saved list."
};
