import React from "react";
import styles from "./AccountFooter.css";
import PropTypes from "prop-types";
import FooterButton from "../../general/components/FooterButton.js";

export default class AccountFooter extends React.Component {
  cancel() {
    if (this.props.cancel) {
      this.props.cancel();
    }
  }
  update() {
    if (this.props.update) {
      this.props.update();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.footerButtonHolder}>
          <FooterButton
            borderColor="#ececec"
            label="Cancel"
            onClick={() => this.cancel()}
          />
        </div>
        <div className={styles.footerButtonHolder}>
          <FooterButton
            backgroundColor="#ff1744"
            label="Update"
            onClick={() => this.update()}
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
AccountFooter.propTyes = {
  cancel: PropTypes.func,
  update: PropTypes.func
};
