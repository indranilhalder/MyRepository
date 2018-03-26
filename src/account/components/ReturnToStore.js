import React from "react";
import { Redirect } from "react-router-dom";
import PiqPage from "../../cart/components/PiqPage";
import MDSpinner from "react-md-spinner";
import ReturnStoreConfirmation from "./ReturnStoreConfirmation.js";
import * as styles from "./ReturnToStore.css";
import {
  RETURNS_PREFIX,
  RETURN_TO_STORE,
  RETURNS_STORE_FINAL,
  RETURN_LANDING,
  RETURNS_REASON,
  QUICK_DROP
} from "../../lib/constants";
const REG_X_FOR_STORE_PICKUP = /storePick/i;
const REG_X_FOR_FINAL_SUBMIT = /submit/i;
export default class ReturnToStore extends React.Component {
  constructor(props) {
    super(props);
    this.orderCode = props.location.pathname.split("/")[2];
    this.state = {
      currentActive: 0,
      storeId: null
    };
  }

  setStore(storeId) {
    this.setState({ storeId }, () => {
      this.props.history.push({
        pathname: `${RETURNS_PREFIX}/${
          this.orderCode
        }${RETURN_TO_STORE}${RETURNS_STORE_FINAL}`,
        state: {
          isRequestFromFlow: true
        }
      });
    });
  }
  // i am using this function becasue of on pincode section i don't have any
  // update button we ll change this function when we ll have update button
  getLocation() {
    if (this.state.pincode && this.state.pincode.length === 6) {
      const ussId = this.props.returnProductDetails.orderProductWsDTO[0].USSID;

      this.props.quickDropStore(this.state.pincode, ussId);
    }
  }

  navigateToFinalSubmit() {
    // submit form here
    const product = this.props.returnProductDetails.orderProductWsDTO[0];
    const productObj = Object.assign(
      {},
      {
        orderCode: this.orderCode,
        transactionId: product.transactionId,
        ussid: product.USSID,
        returnReasonCode: "JEW1S1",
        returnMethod: QUICK_DROP,
        storeIds: this.state.storeId
      }
    );
    if (this.props.bankDetail.accountNumber) {
      Object.assign(productObj, {
        accountNumber: this.props.bankDetail.accountNumber,
        reEnterAccountNumber: this.props.bankDetail.reEnterAccountNumber,
        accountHolderName: this.props.bankDetail.userName,
        bankName: this.props.bankDetail.bankName,
        IFSCCode: this.props.bankDetail.code
      });
    }
    this.props.returnInitialForQuickDrop(productObj);
  }
  renderLoader() {
    return <MDSpinner />;
  }
  navigateToReturnLanding() {
    return (
      <Redirect
        to={`${RETURNS_PREFIX}/${
          this.orderCode
        }${RETURN_LANDING}${RETURNS_REASON}`}
      />
    );
  }
  render() {
    if (!this.props.returnRequest || !this.props.returnProductDetails) {
      return this.renderLoader();
    }
    // Preventing user to open this page direct by hitting URL
    if (
      !this.props.location.state ||
      !this.props.location.state.authorizedRequest
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

    const renderFinalSubmit = (
      <ReturnStoreConfirmation
        {...this.props}
        onContinue={() => this.navigateToFinalSubmit()}
      />
    );
    return (
      <div className={styles.base}>
        {pathname.match(REG_X_FOR_STORE_PICKUP) && renderStoresMap}
        {pathname.match(REG_X_FOR_FINAL_SUBMIT) && renderFinalSubmit}
      </div>
    );
  }
}
