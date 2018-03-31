import React from "react";
import styles from "./CartItem.css";
import BagPageItem from "./BagPageItem.js";

import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import BagPageFooter from "../../general/components/BagPageFooter";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import DeliveryInfoSelect from "./DeliveryInfoSelect";
import PropTypes from "prop-types";

export default class CartItem extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showDelivery: this.props.showDelivery ? this.props.showDelivery : false,
      selectedValue: "",
      label: "See all",
      maxQuantityAllowed: 1,
      qtySelectedByUser: 1,
      quantityList: []
    };
  }
  handleSave(product) {
    if (this.props.onSave) {
      this.props.onSave(product);
    }
  }
  handleRemove(index, pinCode) {
    if (this.props.onRemove) {
      this.props.onRemove(index, pinCode);
    }
  }
  selectDeliveryMode(val) {
    if (this.props.selectDeliveryMode) {
      this.props.selectDeliveryMode(val);
    }
  }

  getPickUpDetails = () => {
    this.props.onPiq();
  };
  onHide() {
    this.setState({ showDelivery: !this.state.showDelivery }, () => {
      if (this.state.label === "See all") {
        this.setState({ label: "Hide" });
      } else {
        this.setState({ label: "See all" });
      }
    });
  }

  componentWillMount() {
    this.setQuantity();
  }
  handleQuantityChange(changedValue) {
    this.setState({ selectedValue: changedValue }, () => {
      if (this.props.onQuantityChange) {
        this.props.onQuantityChange(this.props.index, this.state.selectedValue);
      }
    });
  }
  setQuantity = () => {
    this.setState({
      maxQuantityAllowed: parseInt(this.props.maxQuantityAllowed, 10),
      qtySelectedByUser: parseInt(this.props.qtySelectedByUser, 10)
    });

    if (this.state.quantityList.length === 0) {
      let fetchedQuantityList = [];
      for (let i = 1; i <= parseInt(this.props.maxQuantityAllowed, 10); i++) {
        fetchedQuantityList.push({ value: i.toString() });
      }
      this.setState({
        quantityList: fetchedQuantityList
      });
    }
  };
  render() {
    let isServiceAble = false;
    if (this.props.productIsServiceable) {
      if (this.props.productIsServiceable.isServicable === "Y") {
        isServiceAble = true;
      }
    }

    return (
      <div className={styles.base}>
        <div className={styles.productInformation}>
          <BagPageItem
            productImage={this.props.productImage}
            productName={this.props.productName}
            productDetails={this.props.productDetails}
            price={this.props.price}
            isServiceAvailable={isServiceAble}
          />
        </div>
        {this.props.deliveryInformation &&
          this.props.deliveryInfoToggle && (
            <div className={styles.deliverTimeAndButton}>
              <div className={styles.hideButton}>
                <UnderLinedButton
                  size="14px"
                  fontFamily="regular"
                  color="#000"
                  label={this.state.label}
                  onClick={() => this.onHide()}
                />
              </div>
              <span>
                {this.props.deliveryType} :{" "}
                <span>{this.props.deliverTime}</span>
              </span>
            </div>
          )}

        {this.state.showDelivery &&
          this.props.deliveryInformation && (
            <DeliveryInfoSelect
              deliveryInformation={this.props.deliveryInformation}
              onSelect={val => this.selectDeliveryMode(val)}
              onPiq={val => this.getPickUpDetails()}
            />
          )}
        {this.props.hasFooter && (
          <div className={styles.footer}>
            <BagPageFooter
              onSave={() => this.handleSave(this.props.product)}
              onRemove={() =>
                this.handleRemove(this.props.index, this.state.pinCode)
              }
            />
            <div className={styles.dropdown}>
              <div className={styles.dropdownLabel}>
                {this.props.dropdownLabel}
              </div>
              <SelectBoxMobile
                borderNone={true}
                placeholder="1"
                options={this.state.quantityList}
                selected={this.state.selectedValue}
                onChange={val => this.handleQuantityChange(val)}
                value={this.state.qtySelectedByUser}
              />
            </div>
          </div>
        )}
      </div>
    );
  }
}
CartItem.propTypes = {
  onSave: PropTypes.func,
  onRemove: PropTypes.func,
  productImage: PropTypes.string,
  productName: PropTypes.string,
  productDetails: PropTypes.string,
  price: PropTypes.string,
  deliverTime: PropTypes.string,
  dropdownLabel: PropTypes.string,
  deliveryInfoToggle: PropTypes.bool,
  hasFooter: PropTypes.bool,
  onQuantityChange: PropTypes.func,
  deliveryInformation: PropTypes.arrayOf(
    PropTypes.shape({
      type: PropTypes.string,
      header: PropTypes.string,
      placedTime: PropTypes.string
    })
  ),
  product: PropTypes.object,
  pinCode: PropTypes.object,
  maxQuantityAllowed: PropTypes.string,
  qtySelectedByUser: PropTypes.string
};

CartItem.defaultProps = {
  deliveryInfoToggle: true,
  hasFooter: true
};
