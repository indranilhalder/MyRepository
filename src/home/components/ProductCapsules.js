import React from "react";
import Carousel from "../../general/components/Carousel";
import ProductCapsuleCircle from "../../general/components/ProductCapsuleCircle";
import styles from "./ProductCapsules.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  MY_ACCOUNT_PAGE,
  SAVE_LIST_PAGE
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";

import Loader from "../../general/components/Loader";
export default class ProductCapsules extends React.Component {
  handleClick() {
    this.props.history.push(`${MY_ACCOUNT_PAGE}${SAVE_LIST_PAGE}`);
  }

  handleProductClick = val => {
    if (val) {
      const urlSuffix = val.replace(TATA_CLIQ_ROOT, "$1");
      this.props.history.push(urlSuffix);
    }
  };

  componentDidMount() {
    this.props.getProductCapsules(this.props.positionInFeed);
  }

  render() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (!userDetails || !customerCookie) {
      return null;
    }
    if (this.props.productCapsulesLoading) {
      return <Loader />;
    }

    if (!this.props.feedComponentData) {
      return null;
    }

    const productCapsulesData = this.props.feedComponentData;

    const data = this.props.feedComponentData.data;

    if (!data) {
      return null;
    }
    let subHeader;

    if (
      data &&
      data.wishlistData &&
      data.wishlistData[0] &&
      data.wishlistData[0].items
    ) {
      subHeader = `You have ${
        data.wishlistData[0].items.length
      } products in your list`;
    }

    return (
      <div className={styles.base}>
        <Carousel
          id={this.props.id}
          header={productCapsulesData.title}
          subheader={subHeader}
          buttonText={productCapsulesData.btnText}
          seeAll={() => this.handleClick()}
          elementWidthMobile={30}
          withFooter={false}
        >
          {data &&
            data.wishlistData &&
            data.wishlistData[0] &&
            data.wishlistData[0].items &&
            data.wishlistData[0].items.map((datum, i) => {
              return (
                <ProductCapsuleCircle
                  image={datum.imageURL}
                  label={datum.label}
                  key={i}
                  url={datum.webURL}
                  onClick={this.handleProductClick}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
