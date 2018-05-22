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
    let particularPage;
    this.props.feedComponentData &&
      this.props.feedComponentData.nodeList &&
      this.props.feedComponentData.nodeList.forEach(status => {
        if (status.url.trim() === this.props.location.pathname.trim()) {
          particularPage = status;
        }
      });
    return (
      <div className={styles.navigationHolder}>
        {particularPage ? (
          <SelectBoxMobile2
            value={particularPage.url.replace("/", "")}
            label={particularPage.linkName}
            height={40}
            options={this.props.feedComponentData.nodeList.map((val, i) => {
              return {
                value: val.url.replace("/", ""),
                label: val.linkName
              };
            })}
            backgroundColor="#fff"
            onChange={this.handleItemClick}
          />
        ) : (
          <SelectBoxMobile2
            placeholder={"Select"}
            label={this.props.feedComponentData.nodeList.linkName}
            height={40}
            options={this.props.feedComponentData.nodeList.map((val, i) => {
              return {
                value: val.url.replace("/", ""),
                label: val.linkName
              };
            })}
            backgroundColor="#fff"
            onChange={this.handleItemClick}
          />
        )}
      </div>
    );
  }
}
