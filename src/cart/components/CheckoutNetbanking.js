import React from "react";
import netBankingIcon from "./img/netBanking.svg";
import PropTypes from "prop-types";
import NetBanking from "./NetBanking.js";
import ManueDetails from "../../general/components/MenuDetails.js";

export default class CheckoutNetBanking extends React.Component {
  render() {
    return (
      <ManueDetails text="Net banking" icon={netBankingIcon}>
        <NetBanking
          onSelect={val => console.log(val)}
          selected={["1"]}
          bankList={this.props.cart.netBankDetails.bankList}
        />
      </ManueDetails>
    );
  }
}
