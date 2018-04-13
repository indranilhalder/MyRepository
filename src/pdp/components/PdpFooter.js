import React from "react";
import styles from "./PdpFooter.css";
import PropTypes from "prop-types";
import FooterButton from "../../general/components/FooterButton.js";
import addToBagIcon from "./img/order-historyWhite.svg";
import { WISHLIST_FOOTER_BUTTON_TYPE } from "../../wishlist/components/AddToWishListButton";
import AddToWishListButtonContainer from "../../wishlist/containers/AddToWishListButtonContainer";
export default class PdfFooter extends React.Component {
  onAddToBag() {
    if (this.props.onAddToBag) {
      this.props.onAddToBag();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.footerButtonHolder}>
          <AddToWishListButtonContainer
            type={WISHLIST_FOOTER_BUTTON_TYPE}
            productListingId={this.props.productListingId}
            winningUssID={this.props.winningUssID}
            setDataLayerOnSelect={true} // this is using for setting data layer on pdp page
          />
        </div>
        <div className={styles.footerButtonHolder}>
          <FooterButton
            icon={addToBagIcon}
            backgroundColor="#ff1744"
            label="Add to bag"
            onClick={() => this.onAddToBag()}
            labelStyle={{
              color: "#fff",
              fontSize: 14,
              fontFamily: "semibold"
            }}
          />
        </div>
      </div>
    );
  }
}
PdfFooter.propTyes = {
  onSave: PropTypes.func,
  onAddToBag: PropTypes.func
};
