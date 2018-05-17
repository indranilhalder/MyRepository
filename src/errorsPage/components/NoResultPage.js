import React from "react";
import styles from "./NoResultPage.css";
import Button from "../../general/components/Button.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import { HOME_ROUTER } from "../../lib/constants";
export default class NoResultPage extends React.Component {
  redirectToHelp = url => {
    const urlSuffix = url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headerWithTextHolder}>
          <div className={styles.pageTitle}>404</div>
          <div className={styles.infoText}>
            We weren't expecting this error.
          </div>
          <div className={styles.infoText}>
            Continue CLiQing as we get it fixed.
          </div>
        </div>
        <div className={styles.buttonFooter}>
          <div className={styles.buttonHolder}>
            <Button
              type="primary"
              backgroundColor="#ff1744"
              height={50}
              label="Continue Shopping"
              width={210}
              textStyle={{ color: "#FFF", fontSize: 16 }}
              onClick={() => this.redirectToHome(HOME_ROUTER)}
            />
          </div>
        </div>
      </div>
    );
  }
}
