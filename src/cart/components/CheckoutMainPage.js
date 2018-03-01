import React from "react";
import Checkout from "./Checkout";
import PropTypes from "prop-types";
import ConfirmAddress from "./ConfirmAddress";
import AddDeliveryAddress from "./AddDeliveryAddress";
import CheckOutHeader from "./CheckOutHeader";
import CartItem from "./CartItem";
import PaymentMethodCard from "./PaymentMethodCard";
import DummyTab from "./DummyTab";
import DeliveryModeSet from "./DeliveryModeSet";
import styles from "./CheckoutMainPage.css";
const data = {
  addresses: [
    {
      addressType: "home",
      city: "Mumbai",
      country: {
        isocode: "IN"
      },
      defaultAddress: true,
      firstName: "Chiran",
      id: "8815360868375",
      lastName: "Doshi",
      line1:
        "L.B.S. Marg, Opposite Richardson and Cruddas, Mulund West, Lal Bahadur Shastri Rd",
      phone: "9769344954",
      postalCode: "110044",
      state: "Maharashtra",
      town: "Mumbai"
    },
    {
      addressType: "office",
      city: "Mumbai",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      firstName: "Chiran",
      id: "8815360868378",
      lastName: "Doshi",
      line1:
        "Lorem Ipsum sit amit dolor em imperator furiosa, Oberkommando der whermacht",
      phone: "9769344954",
      postalCode: "110046",
      state: "Maharashtra",
      town: "Mumbai"
    }
  ],
  status: "Success"
};
export default class CheckoutMainPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showPayment: true
    };
  }
  handleCancel = () => {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  };
  handleSubmit = () => {
    if (this.props.onSubmit) {
      this.props.onSubmit();
    }
  };
  onSelectAddress(addressId) {
    if (this.props.onSelectAddress) {
      this.props.onSelectAddress(addressId);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <div className={styles.cross} onClick={this.handleCancel} />
          Checkout
        </div>
        <div className={styles.content}>
          {data.addresses && (
            <ConfirmAddress
              address={data.addresses.map(address => {
                return {
                  addressTitle: address.addressType,
                  addressDescription: `${address.line1} ${address.town} ${
                    address.city
                  }, ${address.state} ${address.postalCode}`,
                  value: address.id
                };
              })}
              onSelectAddress={addressId => this.onSelectAddress(addressId)}
            />
          )}
          {!data.addresses && (
            <AddDeliveryAddress
              {...this.state}
              onChange={val => this.onChange(val)}
            />
          )}
          {/* <div className={styles.products}>
            {!this.state.deliveryModeSet && (
              <React.Fragment>
                <div className={styles.header}>
                  <CheckOutHeader
                    indexNumber="2"
                    confirmTitle="Choose delivery mode"
                  />
                </div>
                {this.props.cartDetails.products &&
                  this.props.cartDetails.products.map((val, i) => {
                    return (
                      <div className={styles.row}>
                        <CartItem
                          key={i}
                          productImage={val.imageURL}
                          hasFooter={false}
                          productDetails={val.description}
                          productName={val.productName}
                          price={val.offerPrice}
                          deliveryInformation={val.elligibleDeliveryMode}
                          showDelivery={true}
                          deliveryInfoToggle={false}
                          selectDeliveryMode={code =>
                            this.handleSelectDeliveryMode(
                              code,
                              val.USSID,
                              this.state.cartId
                            )
                          }
                        />
                      </div>
                    );
                  })}
              </React.Fragment>
            )}
            {this.state.deliveryModeSet && <DeliveryModeSet />}
          </div> */}
          {this.state.showPayment && (
            <div className={styles.section}>
              <PaymentMethodCard
                cashText="Use My CLiQ Cash Balance"
                price="400"
                onToggle={i => this.testClick(i)}
                active={true}
                hasCashBalance={true}
                saveCardDetails={[
                  {
                    cardNumber: "4211",
                    cardImage:
                      "https://upload.wikimedia.org/wikipedia/commons/4/41/Visa_Logo.png"
                  },
                  {
                    cardNumber: "4311",
                    cardImage:
                      "http://www.oplata.com/static/v1/img/logos/mastercard-brand.png"
                  }
                ]}
              />
            </div>
          )}
          {!this.state.showPayment && (
            <div className={styles.section}>
              <DummyTab number="3" title="Payment" />
            </div>
          )}
        </div>
        <Checkout
          amount={this.props.amount}
          totalDiscount={this.props.totalDiscount}
          bagTotal={this.props.bagTotal}
          tax={this.props.tax}
          offers={this.props.offers}
          delivery={this.props.delivery}
          payable={this.props.payable}
          onCheckout={this.handleSubmit}
        />
      </div>
    );
  }
}

CheckoutMainPage.propTypes = {
  amount: PropTypes.string,
  totalDiscount: PropTypes.string,
  bagTotal: PropTypes.string,
  tax: PropTypes.string,
  delivery: PropTypes.string,
  onCheckout: PropTypes.func,
  offers: PropTypes.string,
  payable: PropTypes.string
};
