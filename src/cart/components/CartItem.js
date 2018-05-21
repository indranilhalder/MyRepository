import React from "react";
import styles from "./CartItem.css";
import BagPageItem from "./BagPageItem.js";

import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import BagPageFooter from "../../general/components/BagPageFooter";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import DeliveryInfoSelect from "./DeliveryInfoSelect";
import PropTypes from "prop-types";
import {
  HOME_DELIVERY,
  EXPRESS,
  COLLECT,
  EXPRESS_TEXT,
  STANDARD_SHIPPING,
  COLLECT_TEXT,
  YES,
  NO
} from "../../lib/constants";

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
  getDeliveryName = type => {
    if (type === HOME_DELIVERY) {
      return STANDARD_SHIPPING;
    }
    if (type === EXPRESS) {
      return EXPRESS_TEXT;
    } else if (type === COLLECT) {
      return COLLECT_TEXT;
    }
  };

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
            offerPrice={this.props.offerPrice}
            color={this.props.color}
            size={this.props.size}
            isGiveAway={this.props.isGiveAway}
            isOutOfStock={this.props.isOutOfStock}
            isServiceAvailable={this.props.productIsServiceable}
            onClickImage={() => this.onClick()}
            index={this.props.index}
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
                {this.getDeliveryName(this.props.deliveryType)}{" "}
                {this.props.deliverTime && (
                  <span>{`: ${this.props.deliverTime}`}</span>
                )}
              </span>
            </div>
          )}

        {this.props.isGiveAway === NO &&
          this.state.showDelivery &&
          this.props.deliveryInformation && (
            <DeliveryInfoSelect
              deliveryInformation={this.props.deliveryInformation}
              selected={this.props.selected}
              onSelect={val => this.selectDeliveryMode(val)}
              onPiq={val => this.getPickUpDetails()}
              isClickable={this.props.isClickable}
            />
          )}
        {this.props.isGiveAway === NO &&
          this.props.hasFooter && (
            <div className={styles.footer}>
              <BagPageFooter
                productCode={this.props.product.productcode}
                winningUssID={this.props.product.USSID}
                onRemove={() => this.handleRemove(this.props.index)}
                index={this.props.index}
              />
              <div className={styles.dropdown}>
                <div className={styles.dropdownLabel}>
                  {this.props.dropdownLabel}
                </div>
                <SelectBoxMobile2
                  disabled={this.props.isOutOfStock}
                  theme="hollowBox"
                  options={fetchedQuantityList}
                  onChange={val => this.handleQuantityChange(val)}
                  value={this.props.qtySelectedByUser}
                  label={this.props.qtySelectedByUser}
                />
              </div>
            </div>
          )}
        {this.props.isGiveAway === YES && (
          <div className={styles.footerForFreeProduct}>
            <div className={styles.footerText}>
              {this.props.product &&
                this.props.product.qtySelectedByUser &&
                `Qty :  ${this.props.product.qtySelectedByUser}`}
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
  hasFooter: true,
  dropdownLabel: "Qty :"
};
