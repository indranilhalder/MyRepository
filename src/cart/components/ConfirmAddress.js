import React from "react";
import GridSelect from "../../general/components/GridSelect";
import CheckOutHeader from "./CheckOutHeader.js";
import styles from "./ConfirmAddress.css";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import DeliveryAddressCart from "./DeliveryAddressCart.js";
import PropTypes from "prop-types";
export default class ConfirmAddress extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showAll: false,
      label: "More"
    };
  }
  showMore() {
    this.setState({ showAll: !this.state.showAll }, () => {
      if (this.state.label === "More") {
        this.setState({ label: "Hide" });
      } else {
        this.setState({ label: "More" });
      }
    });
  }
  onNewAddress() {
    if (this.props.onNewAddress) {
      this.props.onNewAddress();
    }
  }
  onSelectAddress(addressId) {
    if (this.props.onSelectAddress) {
      this.props.onSelectAddress(addressId);
    }
  }

  getAddress(address) {}
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <CheckOutHeader
            confirmTitle="Confirm address"
            indexNumber={this.props.indexNumber}
          />
        </div>
        <div className={styles.addressHolder}>
          <GridSelect
            limit={1}
            offset={0}
            elementWidthMobile={100}
            selected={this.props.selected}
            onSelect={addressId => this.onSelectAddress(addressId)}
          >
            {this.props.address &&
              this.props.address
                .filter((val, i) => {
                  return !this.state.showAll ? i < 3 : true;
                })
                .map((val, i) => {
                  return (
                    <DeliveryAddressCart
                      addressTitle={val.addressTitle}
                      addressDescription={val.addressDescription}
                      key={i}
                      value={val.value}
                      selected={val.selected}
                    />
                  );
                })}
          </GridSelect>
          <div className={styles.buttonHolder}>
            {this.props.address &&
              this.props.address.length > 3 && (
                <div className={styles.moreButtonHolder}>
                  <UnderLinedButton
                    size="14px"
                    fontFamily="regular"
                    color="#000"
                    label={this.state.label}
                    onClick={() => this.showMore()}
                  />
                </div>
              )}

            <div className={styles.newAddress}>
              <UnderLinedButton
                size="14px"
                fontFamily="regular"
                color="#ff1744"
                label="Add new address"
                onClick={() => this.onNewAddress()}
              />
            </div>
          </div>
        </div>
      </div>
    );
  }
}
ConfirmAddress.propTypes = {
  onNewAddress: PropTypes.func,
  indexNumber: PropTypes.string,
  address: PropTypes.arrayOf(
    PropTypes.shape({
      addressTitle: PropTypes.string,
      addressDescription: PropTypes.string
    })
  )
};
ConfirmAddress.defaultProps = {
  indexNumber: "1"
};
