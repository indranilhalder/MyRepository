import React from "react";
import styles from "./MonoBanner.css";
import ShopCollection from "../../pdp/components/ShopCollection";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class MonoBanner extends React.Component {
  handleClick() {
    const urlSuffix = this.props.feedComponentData.items[0].webURL.replace(
      TATA_CLIQ_ROOT,
      ""
    );
    this.props.history.push(urlSuffix);
  }
  // componentDidUpdate() {
  //   console.log("COMPONENT DID UPDATE");
  //   console.log(this.props.feedComponentData);
  //   if (
  //     this.props.feedComponentData.data &&
  //     this.props.feedComponentData.data.data &&
  //     this.props.feedComponentData.items.length === 0
  //   ) {
  //     const itemIds = this.props.feedComponentData.data.data;

  //     this.props.getItems(this.props.positionInFeed, itemIds);
  //   }
  // }

  render() {
    let feedComponentData = this.props.feedComponentData;
    return (
      <div className={styles.base}>
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
