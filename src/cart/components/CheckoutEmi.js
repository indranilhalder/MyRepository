import React from "react";
import emiIcon from "./img/emi.svg";
import PropTypes from "prop-types";
import EmiAccordian from "./EmiAccordian";
import ManueDetails from "../../general/components/MenuDetails.js";

export default class CheckoutEmi extends React.Component {
  render() {
    return (
      <ManueDetails text="Easy monthly installments" icon={emiIcon}>
        <EmiAccordian emiList={[]} />
      </ManueDetails>
    );
  }
}
