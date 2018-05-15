import React from "react";
import styles from "./BagPageFooter.css";
import PropTypes from "prop-types";
import AddToWishListButtonContainer from "../../wishlist/containers/AddToWishListButtonContainer";
import { WISHLIST_BUTTON_TEXT_TYPE } from "../../wishlist/components/AddToWishListButton";
import { ADOBE_DIRECT_CALL_FOR_SAVE_ITEM_ON_CART } from "../../lib/adobeUtils";
export default class BagPageFooter extends React.Component {
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
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.wrapper}>
          <AddToWishListButtonContainer
            type={WISHLIST_BUTTON_TEXT_TYPE}
            productListingId={this.props.productCode}
            winningUssID={this.props.winningUssID}
            setDataLayerType={ADOBE_DIRECT_CALL_FOR_SAVE_ITEM_ON_CART}
            index={this.props.index}
          />
          <div className={styles.removeLabel} onClick={() => this.onRemove()}>
            {this.props.removeText}
          </div>
        </div>
      </div>
    );
  }
}
BagPageFooter.propTypes = {
  image: PropTypes.string,
  onRemove: PropTypes.func
};
BagPageFooter.defaultProps = {
  removeText: "Remove"
};
