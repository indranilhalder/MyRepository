import React from "react";
import styles from "./ShippingProcess.css";
import BagPageItem from "./BagPageItem.js";
import DeliveryInformation from "../../general/components/DeliveryInformations.js";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import BagPageFooter from "../../general/components/BagPageFooter";
import SelectBox from "../../general/components/SelectBox.js";
import PropTypes from "prop-types";
export default class ShippingProcess extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      hide: false,
      selectedValue: "",
      label: "See all"
    };
  }
  onSave() {
    if (this.props.onSave) {
      this.props.onSave();
    }
  }
  onRemove() {
    if (this.props.onRemove) {
      this.props.onRemove();
    }
  }
  onHide() {
    this.setState({ hide: !this.state.hide });
    if (!this.state.hide) {
      this.setState({ label: "Hide" });
    } else {
      this.setState({ label: "See all" });
    }
  }
  handleChange(changedValue) {
    this.setState({ selectedValue: changedValue }, () => {
      this.setState({ selectedValue: changedValue });
    });
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.productInformation}>
          <BagPageItem
            productImage={this.props.productImage}
            productName={this.props.productName}
            productDetails={this.props.productDetails}
            price={this.props.price}
          />
        </div>
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
            Express : Delivers in <span>{this.props.deliverTime}</span>
          </span>
        </div>
        {this.state.hide && (
          <div className={styles.shippingStep}>
            {this.props.DeliveryInformation.map((datum, i) => {
              return (
                <DeliveryInformation
                  key={i}
                  type={datum.type}
                  header={datum.header}
                  placedTime={datum.placedTime}
                  label={datum.label}
                />
              );
            })}
          </div>
        )}
        <div className={styles.footer}>
          <BagPageFooter
            onSave={() => this.onSave()}
            onRemove={() => this.onRemove()}
          />
          <div className={styles.dropdown}>
            <SelectBox
              borderNone={true}
              placeholder="1"
              options={this.props.option}
              selected={this.state.selectedValue}
              onChange={Value => this.handleChange(Value)}
            />
          </div>
        </div>
      </div>
    );
  }
}
ShippingProcess.propTypes = {
  onSave: PropTypes.func,
  onRemove: PropTypes.func,
  productImage: PropTypes.string,
  productName: PropTypes.string,
  productDetails: PropTypes.string,
  price: PropTypes.string,
  deliverTime: PropTypes.string,
  DeliveryInformation: PropTypes.arrayOf(
    PropTypes.shape({
      type: PropTypes.string,
      header: PropTypes.string,
      placedTime: PropTypes.string
    })
  )
};
