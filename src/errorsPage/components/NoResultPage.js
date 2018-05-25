import React from "react";
import styles from "./NoResultPage.css";
import Button from "../../general/components/Button.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import { HOME_ROUTER } from "../../lib/constants";
import { setDataLayerForNotFound } from "../../lib/adobeUtils";
export default class NoResultPage extends React.Component {
  redirectToHome() {
    this.props.history.push(HOME_ROUTER);
  }
  componentDidMount() {
    setDataLayerForNotFound();
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headerWithTextHolder}>
          <div className={styles.pageTitle}>404</div>
          <div className={styles.noResultText}>
            The page you are looking for can't be found
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
              onClick={() => this.redirectToHome()}
            />
          </div>
        </div>
      </div>
    );
  }
}
