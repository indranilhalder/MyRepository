import React from "react";
import GridSelect from "../../general/components/GridSelect";
import EmiCartSelect from "./EmiCartSelect";
// import styles from "./EmiAccordian.css";
export default class EmiAccordian extends React.Component {
  render() {
    return (
      <GridSelect elementWidthMobile={100}>
        <EmiCartSelect
          title="KOTAK MAHINDRA BANK, LTD."
          options={[
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
          ]}
        />
      </GridSelect>
    );
  }
}
