import React from "react";
import PiqPage from "../../cart/components/PiqPage";
import ReturnBankForm from "./ReturnBankForm";
import ReturnStoreConfirmation from "./ReturnStoreConfirmation.js";
import * as styles from "./ReturnToStore.css";
export default class ReturnToStore extends React.Component {
  constructor() {
    super();
    this.state = {
      currentActive: 0
    };
  }
  onChangeBankDetail(val) {
    this.setState(val);
  }
  render() {
    return (
      <div className={styles.base}>
        {/* <PiqPage /> */}
        <ReturnBankForm onChange={data => this.onChangeBankDetail(data)} />
        <ReturnStoreConfirmation />
      </div>
    );
  }
}
