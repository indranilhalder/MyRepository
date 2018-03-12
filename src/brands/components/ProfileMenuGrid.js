import React from "react";
import Grid from "../../general/components/Grid";
import ProfileMenu from "./ProfileMenu";
import PropTypes from "prop-types";
import styles from "./ProfileMenuGrid.css";
import savedList from "../../general/components/img/savewhite.svg";
import addressBook from "../../general/components/img/addressbookwhite.svg";
import brands from "../../general/components/img/brandswhite.svg";
import orderHistory from "../../general/components/img/orderhistorywhite.svg";
import savedPayments from "../../general/components/img/cardwhite.svg";
import alertsCoupons from "../../general/components/img/notification.svg";
import cliqCash from "../../general/components/img/cliqCash.svg";
import giftCards from "../../general/components/img/giftCards.svg";
import settings from "../../general/components/img/settings.svg";
export default class ProfileMenuGrid extends React.Component {
  onSave(value) {
    if (this.props.onSave) {
      this.props.onSave(value);
    }
  }
  render() {
    const data = [
      { image: savedList, text: "Saved List" },
      { image: addressBook, text: "Address Book" },
      { image: brands, text: "Brands" },
      { image: orderHistory, text: "Order History" },
      { image: savedPayments, text: "Saved Payments" },
      { image: alertsCoupons, text: "Alerts & Coupons" },
      { image: giftCards, text: "Gift Card" },
      { image: cliqCash, text: "Cliq Cash" },
      { image: settings, text: "Settings" }
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
                onSave={value => this.onSave(value)}
              />
            );
          })}
        </Grid>
      </div>
    );
  }
}
ProfileMenuGrid.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string
};
