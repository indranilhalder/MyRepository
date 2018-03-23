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
        {this.state.currentActive === 0 && (
          <PiqPage
            {...this.props}
            addStoreCNC={() => this.setState({ currentActive: 1 })}
          />
        )}
        {this.state.currentActive === 1 && (
          <ReturnBankForm
            onChange={data => this.onChangeBankDetail(data)}
            onContinue={() => this.setState({ currentActive: 2 })}
          />
        )}
        {this.state.currentActive === 2 && (
          <ReturnStoreConfirmation
            onContinue={() => this.setState({ currentActive: 2 })}
          />
        )}
      </div>
    );
  }
}
