import React from "react";
import { Redirect } from "react-router-dom";
import PiqPage from "../../cart/components/PiqPage";
import ReturnBankForm from "./ReturnBankForm";
import MDSpinner from "react-md-spinner";
import ReturnStoreConfirmation from "./ReturnStoreConfirmation.js";
import * as styles from "./ReturnToStore.css";
import {
  RETURNS_PREFIX,
  RETURN_TO_STORE,
  RETURNS_STORE_MAP,
  RETURNS_STORE_BANK_FORM,
  RETURNS_STORE_FINAL,
  RETURN_LANDING,
  RETURNS_REASON,
  QUICK_DROP
} from "../../lib/constants";
import { ObjectUnsubscribedError } from "rxjs";
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
      this.props.history.push({
        pathname: `${RETURNS_PREFIX}/3435345${RETURN_TO_STORE}${RETURNS_STORE_BANK_FORM}`,
        state: {
          isRequestFromFlow: true
        }
      });
    });
  }
  // i am using this function becasue of on pincide section i don't have any
  // update button we ll change this function when we ll have update button
  getLocation() {
    if (this.state.pincode) {
      const ussId = this.props.returnProductDetails.orderProductWsDTO[0].USSID;

      this.props.quickDropStore(this.state.pincode, ussId);
    }
  }
  navigateToShowInitiateReturn() {
    console.log(this.props);
    this.props.history.push({
      pathname: `${RETURNS_PREFIX}/3435345${RETURN_TO_STORE}${RETURNS_STORE_FINAL}`,
      state: {
        isRequestFromFlow: true
      }
    });
  }
  navigateToFinalSubmit() {
    // submit form here
    const product = this.props.returnProductDetails.orderProductWsDTO[0];
    const productObj = Object.assign(
      {},
      {
        orderCode: "100011757",
        transactionId: product.transactionId,
        ussid: product.USSID,
        returnReasonCode: "JEW100",
        returnMethod: QUICK_DROP,
        storeIds: this.state.storeId,
        accountNumber: this.state.accountNumber,
        reEnterAccountNumber: this.state.reEnterAccountNumber,
        accountHolderName: this.state.userName,
        bankName: this.state.bankName,
        IFSCCode: this.state.code
      }
    );
    this.props.returnInitialForQuickDrop(productObj);
  }
  renderLoader() {
    return <MDSpinner />;
  }
  navigateToReturnLanding() {
    return (
      <Redirect
        to={`${RETURNS_PREFIX}/3435345${RETURN_LANDING}${RETURNS_REASON}`}
      />
    );
  }
  render() {
    console.log(this.props);
    if (!this.props.returnRequest || !this.props.returnProductDetails) {
      return this.renderLoader();
    }
    if (
      !this.props.location.state ||
      !this.props.location.state.isRequestFromFlow
    ) {
      return this.navigateToReturnLanding();
    }
    const { pathname } = this.props.location;
    const renderStoresMap = (
      <PiqPage
        {...this.props}
        productName={
          this.props.returnProductDetails.orderProductWsDTO[0].productName
        }
        productColour={
          this.props.returnProductDetails.orderProductWsDTO[0].productColour
        }
        availableStores={this.props.returnRequest.returnStoreDetailsList}
        numberOfStores={this.props.returnRequest.returnStoreDetailsList.length}
        pincode={this.state.pincode}
        addStoreCNC={storeId => this.setStore(storeId)}
        changePincode={pincode => this.setState({ pincode })}
        getLocation={() => this.getLocation()}
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
