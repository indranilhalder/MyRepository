import React from "react";
import GridSelect from "../../general/components/GridSelect";
import BankSelect from "./BankSelect";

export default class NetBanking extends React.Component {
  render() {
    console.log(this.props);
    return (
      <GridSelect limit={1} offset={30} gridWidthMobile={33.3333}>
        <BankSelect
          image="https://competitiondigest.com/wp-content/uploads/2014/12/bank-of-1.gif"
          value="1"
        />
        <BankSelect
          image="https://competitiondigest.com/wp-content/uploads/2014/12/CBicons_03.png"
          value="2"
        />
        <BankSelect
          image="https://competitiondigest.com/wp-content/uploads/2014/12/UNITED.png"
          value="3"
        />
        <BankSelect
          image="https://competitiondigest.com/wp-content/uploads/2014/12/UNION.png"
          value="4"
        />
      </GridSelect>
    );
  }
}
