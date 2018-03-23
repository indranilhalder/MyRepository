import React from "react";
import PiqPage from "../../cart/components/PiqPage";
import ReturnBankForm from "./ReturnBankForm";
import ReturnStoreConfirmation from "./ReturnStoreConfirmation.js";
import * as styles from "./ReturnToStore.css";
import {
  RETURN_PREFIX,
  RETURN_TO_STORE,
  RETURNS_STORE_MAP,
  RETURNS_STORE_BANK_FORM,
  RETURNS_STORE_FINAL
} from "../../lib/constants";
const REG_X_FOR_STORE_PICKUP = /storePick/i;
const REG_X_FOR_BANKING_FORM = /bankDetail/i;
const REG_X_FOR_FINAL_SUBMIT = /submit/i;
export default class ReturnToStore extends React.Component {
  constructor() {
    super();
    this.state = {
      currentActive: 0,
      storeId: null
    };
  }
  onChangeBankDetail(val) {
    this.setState(val);
  }
  setStore(storeId) {
    this.setState({ storeId }, () => {
      this.props.history.push(
        `${RETURN_PREFIX}/3435345${RETURN_TO_STORE}${RETURNS_STORE_BANK_FORM}`
      );
    });
  }
  navigateToShowInitiateReturn() {
    this.props.history.push(
      `${RETURN_PREFIX}/3435345${RETURN_TO_STORE}${RETURNS_STORE_FINAL}`
    );
  }
  navigateToFinalSubmit() {
    // submit form here
  }
  render() {
    const { pathname } = this.props.location;
    const renderStoresMap = (
      <PiqPage
        {...this.props}
        addStoreCNC={storeId => this.setStore(storeId)}
      />
    );
    const renderBankingForm = (
      <ReturnBankForm
        onChange={data => this.onChangeBankDetail(data)}
        onContinue={() => this.navigateToShowInitiateReturn()}
      />
    );
    const renderFinalSubmit = (
      <ReturnStoreConfirmation
        onContinue={() => this.navigateToFinalSubmit()}
      />
    );
    return (
      <div className={styles.base}>
        {pathname.match(REG_X_FOR_STORE_PICKUP) && renderStoresMap}
        {pathname.match(REG_X_FOR_BANKING_FORM) && renderBankingForm}
        {pathname.match(REG_X_FOR_FINAL_SUBMIT) && renderFinalSubmit}
      </div>
    );
  }
}
