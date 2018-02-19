import React from "react";
import CartItem from "./CartItem";
import Checkout from "./Checkout";
import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import styles from "./CartPage.css";
import PropTypes from "prop-types";
class CartPage extends React.Component {
  componentWillMount() {
    this.props.getCartDetails();
  }

  render() {
    if (this.props.cart.cartDetails) {
      console.log(this.props.cart.cartDetails);
      const cartDetails = this.props.cart.cartDetails;
      return (
        <div className={styles.base}>
          <div className={styles.content}>
            <div className={styles.search}>
              <SearchAndUpdate />
            </div>
            <div className={styles.offer}>
              <div className={styles.offerText}>{this.props.cartOfferText}</div>
              <div className={styles.offerName}>{this.props.cartOffer}</div>
            </div>

            {cartDetails.products.map((val, i) => {
              return (
                <div className={styles.cartItem}>
                  <CartItem
                    key={i}
                    productImage={val.imageURL}
                    productDetails={val.description}
                    productName={val.productName}
                    price={val.priceValue.sellingPrice.formattedValue}
                    deliveryInformation={val.elligibleDeliveryMode}
                    deliverTime={val.elligibleDeliveryMode[0].desc}
                    option={[
                      {
                        value: val.qtySelectedByUser,
                        label: val.qtySelectedByUser
                      }
                    ]}
                  />
                </div>
              );
            })}
          </div>
          <Checkout
            amount={cartDetails.cartAmount.bagTotal.formattedValue}
            bagTotal={cartDetails.cartAmount.bagTotal.formattedValue}
            tax={this.props.cartTax}
            offers={this.props.offers}
            delivery={this.props.delivery}
            payable={cartDetails.cartAmount.paybleAmount.formattedValue}
          />
        </div>
      );
    } else {
      return null;
    }
  }
}

CartPage.propTypes = {
  cartOfferText: PropTypes.string,
  cartOffer: PropTypes.string,
  cartTax: PropTypes.string,
  delivery: PropTypes.string,
  offers: PropTypes.string
};

CartPage.defaultProps = {
  cartOfferText: "Congratulations your cart qualifies for.",
  cartOffer: "FREE shipping",
  cartTax: "included",
  delivery: "Free",
  offers: "Apply"
};

export default CartPage;
