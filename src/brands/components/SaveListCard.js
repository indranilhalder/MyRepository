import React from "react";
import styles from "./SaveListCard.css";
import PropTypes from "prop-types";
import ProductDetailsCard from "../../pdp/components/ProductDetailsCard";
import StarRating from "../../general/components/StarRating.js";
import OrderReturn from "../../account/components/OrderReturn.js";
export default class SaveListCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      openPopup: false
    };
    this.setWrapperRef = this.setWrapperRef.bind(this);
    this.handleClickOutside = this.handleClickOutside.bind(this);
  }
  componentDidMount() {
    document.addEventListener("mousedown", this.handleClickOutside);
  }
  handleClickOutside(event) {
    if (this.wrapperRef && !this.wrapperRef.contains(event.target)) {
      this.setState({ openPopup: false });
    }
  }
  setWrapperRef(node) {
    this.wrapperRef = node;
  }
  getListValue(val) {
    if (this.props.getListValue) {
      this.props.getListValue(val);
    }
  }
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
  render() {
    let ClassName = styles.openPopup;
    if (this.state.openPopup) {
      ClassName = styles.openPopupWithScale;
    }
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <ProductDetailsCard
            productImage={this.props.image}
            productName={this.props.productName}
            productMaterial={this.props.productMaterial}
            price={this.props.price}
          />
          <div className={styles.rating}>
            <StarRating averageRating={this.props.averageRating}>
              {this.props.totalNoOfReviews && (
                <div className={styles.noOfReviews}>{`(${
                  this.props.totalNoOfReviews
                })`}</div>
              )}
            </StarRating>
          </div>
          {!this.state.openPopup && (
            <div
              className={styles.iconContainer}
              onClick={() => this.openPopup()}
            >
              <div className={styles.circleContainer}>
                <div className={styles.circleIcon} />
                <div className={styles.circleIcon} />
                <div className={styles.circleIcon} />
              </div>
            </div>
          )}
          <div className={ClassName} ref={this.setWrapperRef}>
            <div className={styles.composer}>
              {this.props.composer.map((datum, i) => {
                return (
                  <div
                    className={styles.list}
                    key={i}
                    onClick={() => this.getListValue(datum.data)}
                  >
                    {datum.data}
                  </div>
                );
              })}
            </div>
          </div>
        </div>
        <div className={styles.textContainer}>
          <div className={styles.text}>{this.props.text}</div>
          <div className={styles.offers}>{this.props.offers}</div>
        </div>
        <div className={styles.footer}>
          <OrderReturn
            replaceItem={() => this.removeItem()}
            writeReview={() => this.addToBagItem()}
            underlineButtonLabel={this.props.underlineButtonLabel}
            buttonLabel={this.props.buttonLabel}
            underlineButtonColour={this.props.underlineButtonColour}
          />
        </div>
      </div>
    );
  }
}
SaveListCard.propTypes = {
  text: PropTypes.string,
  offers: PropTypes.string,
  image: PropTypes.string,
  productName: PropTypes.string,
  averageRating: PropTypes.number,
  price: PropTypes.string,
  totalNoOfReviews: PropTypes.string,
  productMaterial: PropTypes.string,
  removeItem: PropTypes.func,
  addToBagItem: PropTypes.func,
  underlineButtonColour: PropTypes.string,
  underlineButtonLabel: PropTypes.string,
  buttonLabel: PropTypes.string,
  getListValue: PropTypes.func,
  composer: PropTypes.arrayOf(
    PropTypes.shape({
      data: PropTypes.string
    })
  )
};
SaveListCard.defaultProps = {
  underlineButtonLabel: "Add to bag",
  buttonLabel: "Remove",
  underlineButtonColour: "#ff1744"
};
