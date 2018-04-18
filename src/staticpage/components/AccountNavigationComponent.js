import React, { Component } from "react";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import styles from "./AccountNavigationComponent.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class AccountNavigationComponent extends Component {
  handleItemClick = url => {
    const urlSuffix = url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  };
  render() {
    return (
      <div className={styles.navigationHolder}>
        <SelectBoxMobile
          label="Select"
          height={40}
          options={this.props.data.nodeList.map((val, i) => {
            return {
              value: val.url,
              label: val.linkName
            };
          })}
          onChange={this.handleItemClick}
        />
      </div>
    );
  }
}
