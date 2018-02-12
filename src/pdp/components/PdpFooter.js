import React from "react";
import styles from "./PdpFooter.css";
import PropTypes from "prop-types";
import FooterButton from "../../general/components/FooterButton.js";
import saveIcon from "./img/Save.svg";
import addToBagIcon from "./img/order-historyWhite.svg";
export default class PdfFooter extends React.Component {
  onSave() {
    if (this.props.onSave) {
      this.props.onSave();
    }
  }
  onAddToBag() {
    if (this.props.onAddToBag) {
      this.props.onAddToBag();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.footerButtonHolder}>
          <FooterButton
            borderColor="#ececec"
            icon={saveIcon}
            label="save"
            onClick={() => this.onSave()}
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
