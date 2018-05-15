import React, { Component } from "react";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import styles from "./AccountNavigationComponent.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class AccountNavigationComponent extends Component {
  handleItemClick = url => {
    const urlSuffix = url.value.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  };
  render() {
    return (
      <div className={styles.navigationHolder}>
        <SelectBoxMobile2
          placeholder={"Select"}
          value={this.props.location.pathname.replace("/", "")}
          label={this.props.feedComponentData.nodeList.linkName}
          height={40}
          options={this.props.feedComponentData.nodeList.map((val, i) => {
            return {
              value: val.url.replace("/", ""),
              label: val.linkName
            };
          })}
          onChange={this.handleItemClick}
        />
      </div>
    );
  }
}
