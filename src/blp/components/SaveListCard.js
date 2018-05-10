import React from "react";
import styles from "./SaveListCard.css";
import PropTypes from "prop-types";
import ProductDetailsCard from "../../pdp/components/ProductDetailsCard";
import StarRating from "../../general/components/StarRating.js";
import OrderReturn from "../../account/components/OrderReturn.js";
export default class SaveListCard extends React.Component {
  addToBagItem() {
    if (this.props.addToBagItem) {
      this.props.addToBagItem();
    }
  }

  removeItem() {
    if (this.props.removeItem) {
      this.props.removeItem();
    }
  }
  openPopup() {
    this.setState({ openPopup: true });
  }
  onClickImage() {
    if (this.props.onClickImage) {
      this.props.onClickImage();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <ProductDetailsCard
            productImage={this.props.image}
            outOfStock={this.props.outOfStock}
            productName={this.props.productName}
            productMaterial={this.props.productMaterial}
            price={this.props.price}
            onClickImage={() => this.onClickImage()}
          />
          <div className={styles.rating}>
            {this.props.totalNoOfReviews && (
              <StarRating averageRating={this.props.averageRating}>
                <div className={styles.noOfReviews}>{`(${
                  this.props.totalNoOfReviews
                })`}</div>
              </StarRating>
            )}
          </div>
        </div>
        <div className={styles.textContainer}>
          <div className={styles.text}>{`Get it by ${this.props.day} , ${
            this.props.date
          }`}</div>
          <div className={styles.offers}>{`${this.props.offer} offers from Rs.${
            this.props.offerPrice
          }`}</div>
        </div>
        <div className={styles.footer}>
          <OrderReturn
            replaceItem={() => this.removeItem()}
            writeReview={() => this.addToBagItem()}
            underlineButtonLabel={this.props.underlineButtonLabel}
            buttonLabel={this.props.buttonLabel}
            underlineButtonColour={this.props.underlineButtonColour}
            isEditable={this.props.isEditable}
            outOfStock={this.props.outOfStock}
          />
        </div>
      </div>
    );
  }
}
SaveListCard.propTypes = {
  text: PropTypes.string,
  offers: PropTypes.number,
  image: PropTypes.string,
  productName: PropTypes.string,
  averageRating: PropTypes.number,
  price: PropTypes.number,
  totalNoOfReviews: PropTypes.string,
  productMaterial: PropTypes.string,
  removeItem: PropTypes.func,
  addToBagItem: PropTypes.func,
  underlineButtonColour: PropTypes.string,
  underlineButtonLabel: PropTypes.string,
  buttonLabel: PropTypes.string,
  outOfStock: PropTypes.bool
};
SaveListCard.defaultProps = {
  underlineButtonLabel: "Add to bag",
  buttonLabel: "Remove",
  underlineButtonColour: "#ff1744",
  isEditable: true,
  outOfStock: false
};
