import React from "react";
import styles from "./MonoBanner.css";
import ShopCollection from "../../pdp/components/ShopCollection";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class MonoBanner extends React.Component {
  handleClick() {
    if (
      this.props.feedComponentData.items &&
      this.props.feedComponentData.items[0] &&
      this.props.feedComponentData.items[0].webURL
    ) {
      const urlSuffix = this.props.feedComponentData.items[0].webURL.replace(
        TATA_CLIQ_ROOT,
        "$1"
      );
      if (this.props.feedComponentData.items[0].webURL.includes("/que")) {
        window.open(urlSuffix, "_blank");
        window.focus();
      }
      if (
        this.props.feedComponentData.items[0].webURL.includes(
          "/luxury.tatacliq.com"
        )
      ) {
        window.open(this.props.feedComponentData.items[0].webURL, "_blank");
        window.focus();
      } else {
        this.props.history.push(urlSuffix);
        if (this.props.setClickedElementId) {
          this.props.setClickedElementId();
        }
      }
    }
  }

  render() {
    let feedComponentData = this.props.feedComponentData;
    return (
      <div
        className={
          this.props.positionInFeed === 1 ? styles.firstItemBase : styles.base
        }
      >
        <div className={styles.shopeRangeHeader}>{feedComponentData.title}</div>
        <ShopCollection
          image={feedComponentData.items[0].imageURL}
          title={feedComponentData.items[0].title}
          btnText={feedComponentData.items[0].btnText}
          backgroundColor={feedComponentData.items[0].backgroundColor}
          onClick={() => this.handleClick()}
        />
      </div>
    );
  }
}
