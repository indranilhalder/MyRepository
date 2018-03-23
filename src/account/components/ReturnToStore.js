import React from "react";
import PiqPage from "../../cart/components/PiqPage";
import ReturnBankForm from "./ReturnBankForm";
import ReturnStoreConfirmation from "./ReturnStoreConfirmation.js";
import * as styles from "./ReturnToStore.css";
import { Route } from "react-router-dom";
const REG_X_FOR_STORE_PICKUP = /storePick/;
const REG_X_FOR_BANKING_FORM = /bankDetail/;
const REG_X_FOR_FINAL_SUBMIT = /submit/;
export default class ReturnToStore extends React.Component {
  constructor() {
    super();
    console.log("REBUILDING");
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
      this.props.history.replace("/returns/543454jkl345/store/bankDetail");
    });
  }
  render() {
    console.log(this.state);
    console.log(this.props.history);
    const { pathname } = this.props.location;
    console.log(pathname.match(REG_X_FOR_STORE_PICKUP));
    const test = (
      <PiqPage
        {...this.props}
        addStoreCNC={storeId => this.setStore(storeId)}
      />
    );
    const test2 = (
      <ReturnBankForm
        onChange={data => this.onChangeBankDetail(data)}
        onContinue={() => this.setState({ currentActive: 2 })}
      />
    );
    return (
      <div className={styles.base}>
        <Route path="/storepick" component={test} />
        <Route path="/bankingForm" component={test2} />
        {pathname.match(REG_X_FOR_FINAL_SUBMIT) && (
          <ReturnStoreConfirmation
            onContinue={() => this.setState({ currentActive: 2 })}
          />
        )}
      </div>
    );
  }
}
