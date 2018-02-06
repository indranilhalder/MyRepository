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
        >
          {data.map((datum, i) => {
            return (
              <Address
                key={i}
                heading={datum.heading}
                address={datum.address}
                value={datum.value}
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
  data: PropTypes.shape({
    heading: PropTypes.string,
    addres: PropTypes.string,
    value: PropTypes.string
  })
};
AddressCarousel.defaultProps = {
  text: "Select from your saved list."
};
