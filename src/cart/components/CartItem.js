import React from "react";
import styles from "./CartItem.css";
import BagPageItem from "./BagPageItem.js";

import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import BagPageFooter from "../../general/components/BagPageFooter";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import DeliveryInfoSelect from "./DeliveryInfoSelect";
import PropTypes from "prop-types";

export default class CartItem extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showDelivery: this.props.showDelivery ? this.props.showDelivery : false,
      label: "See all"
    };
  }
  onClick() {
    if (this.props.onClickImage) {
      this.props.onClickImage();
    }
  }
  handleRemove(index) {
    if (this.props.onRemove) {
      this.props.onRemove(index);
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

  handleQuantityChange(changedValue) {
    const updatedQuantity = parseInt(changedValue.value, 10);
    if (this.props.onQuantityChange) {
      this.props.onQuantityChange(this.props.entryNumber, updatedQuantity);
    }
  }
  render() {
    let fetchedQuantityList = [];
    if (this.props.isOutOfStock) {
      fetchedQuantityList = [{}];
    } else {
      for (let i = 1; i <= this.props.maxQuantityAllowed; i++) {
        fetchedQuantityList.push({ value: i.toString(), label: i.toString() });
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
            isOutOfStock={this.props.isOutOfStock}
            isServiceAvailable={this.props.productIsServiceable}
            onClickImage={() => this.onClick()}
          />
        </div>
        {this.props.deliveryInformation &&
          this.props.deliveryInfoToggle && (
            <div className={styles.deliverTimeAndButton}>
              {this.props.deliveryInformation.length > 1 && (
                <div className={styles.hideButton}>
                  <UnderLinedButton
                    size="14px"
                    fontFamily="regular"
                    color="#000"
                    label={this.state.label}
                    onClick={() => this.onHide()}
                  />
                </div>
              )}
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
              selected={this.props.selected}
              onSelect={val => this.selectDeliveryMode(val)}
              onPiq={val => this.getPickUpDetails()}
              isClickable={this.props.isClickable}
            />
          )}
        {this.props.hasFooter && (
          <div className={styles.footer}>
            <BagPageFooter
              productCode={this.props.product.productcode}
              winningUssID={this.props.product.USSID}
              onRemove={() => this.handleRemove(this.props.index)}
            />
            <div className={styles.dropdown}>
              <div className={styles.dropdownLabel}>
                {this.props.dropdownLabel}
              </div>
              <SelectBoxMobile2
                disabled={this.props.isOutOfStock}
                borderNone={true}
                options={fetchedQuantityList}
                onChange={val => this.handleQuantityChange(val)}
                value={this.props.qtySelectedByUser}
                label={this.props.qtySelectedByUser}
              />
            </div>
          </div>
        )}
      </div>
    );
  }
}
CartItem.propTypes = {
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
