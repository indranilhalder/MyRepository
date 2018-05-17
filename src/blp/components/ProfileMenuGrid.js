import React from "react";
import Grid from "../../general/components/Grid";
import ProfileMenu from "./ProfileMenu";
import PropTypes from "prop-types";
import {
  MY_ACCOUNT_PAGE,
  MY_ACCOUNT_ORDERS_PAGE,
  MY_ACCOUNT_GIFT_CARD_PAGE,
  MY_ACCOUNT_SAVED_CARDS_PAGE,
  MY_ACCOUNT_ADDRESS_PAGE,
  MY_ACCOUNT_BRANDS_PAGE,
  MY_ACCOUNT_UPDATE_PROFILE_PAGE,
  MY_ACCOUNT_ALERTS_PAGE,
  MY_ACCOUNT_CLIQ_CASH_PAGE,
  SAVE_LIST_PAGE
} from "../../lib/constants";
import styles from "./ProfileMenuGrid.css";
import savedList from "../../general/components/img/downloadWhite.svg";
import addressBook from "../../general/components/img/addressbookwhite.svg";
import brands from "../../general/components/img/brandswhite.svg";
import orderHistory from "../../general/components/img/orderhistorywhite.svg";
import savedPayments from "../../general/components/img/cardwhite.svg";
import alertsCoupons from "../../general/components/img/notification.svg";
import cliqCash from "../../general/components/img/cliqCash.svg";
import giftCards from "../../general/components/img/giftCards.svg";
import settings from "../../general/components/img/settings.svg";
export default class ProfileMenuGrid extends React.Component {
  onRender(value) {
    if (value) {
      this.props.history.push(`${MY_ACCOUNT_PAGE}${value}`);
    }
  }
  render() {
    const data = [
      {
        image: savedList,
        text: "My Wish List",
        url: SAVE_LIST_PAGE
      },
      {
        image: addressBook,
        text: "Address Book",
        url: MY_ACCOUNT_ADDRESS_PAGE
      },
      { image: brands, text: "Brands", url: MY_ACCOUNT_BRANDS_PAGE },
      {
        image: orderHistory,
        text: "Order History",
        url: MY_ACCOUNT_ORDERS_PAGE
      },
      {
        image: savedPayments,
        text: "Saved Cards",
        url: MY_ACCOUNT_SAVED_CARDS_PAGE
      },
      {
        image: alertsCoupons,
        text: "Alerts & Coupons",
        url: MY_ACCOUNT_ALERTS_PAGE
      },
      { image: giftCards, text: "Gift Card", url: MY_ACCOUNT_GIFT_CARD_PAGE },
      { image: cliqCash, text: "Cliq Cash", url: MY_ACCOUNT_CLIQ_CASH_PAGE },
      { image: settings, text: "Settings", url: MY_ACCOUNT_UPDATE_PROFILE_PAGE }
    ];

    return (
      <div className={styles.base}>
        <Grid elementWidthMobile={33.33}>
          {data.map((datum, i) => {
            return (
              <ProfileMenu
                image={datum.image}
                text={datum.text}
                key={i}
                onSave={() => this.onRender(datum.url)}
              />
            );
          })}
        </Grid>
      </div>
    );
  }
}
