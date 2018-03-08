import React from "react";
import emiIcon from "./img/emi.svg";
import PropTypes from "prop-types";
import EmiAccordian from "./EmiAccordian";
import ManueDetails from "../../general/components/MenuDetails.js";

export default class CheckoutEmi extends React.Component {
  render() {
    return (
      <ManueDetails text="Easy monthly installments" icon={emiIcon}>
        <EmiAccordian
          emiList={[
            {
              value: 1,
              title: "Kotak Mahindra",
              options: [
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹23966.24",
                  term: "3",
                  interestPayable: "₹1760.71"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹12206.38",
                  term: "6",
                  interestPayable: "₹3100.30"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹8288.25",
                  term: "9",
                  interestPayable: "₹4456.22"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹6330.54",
                  term: "12",
                  interestPayable: "₹5828.45"
                }
              ]
            },
            {
              value: 2,
              title: "Axis",
              options: [
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹23966.24",
                  term: "3",
                  interestPayable: "₹1760.71"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹12206.38",
                  term: "6",
                  interestPayable: "₹3100.30"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹8288.25",
                  term: "9",
                  interestPayable: "₹4456.22"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹6330.54",
                  term: "12",
                  interestPayable: "₹5828.45"
                }
              ]
            },
            {
              value: 3,
              title: "SBI",
              options: [
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹23966.24",
                  term: "3",
                  interestPayable: "₹1760.71"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹12206.38",
                  term: "6",
                  interestPayable: "₹3100.30"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹8288.25",
                  term: "9",
                  interestPayable: "₹4456.22"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹6330.54",
                  term: "12",
                  interestPayable: "₹5828.45"
                }
              ]
            },
            {
              value: 4,
              title: "ICICI Bank",
              options: [
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹23966.24",
                  term: "3",
                  interestPayable: "₹1760.71"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹12206.38",
                  term: "6",
                  interestPayable: "₹3100.30"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹8288.25",
                  term: "9",
                  interestPayable: "₹4456.22"
                },
                {
                  interestRate: "15.0",
                  monthlyInstallment: "₹6330.54",
                  term: "12",
                  interestPayable: "₹5828.45"
                }
              ]
            }
          ]}
        />
      </ManueDetails>
    );
  }
}
