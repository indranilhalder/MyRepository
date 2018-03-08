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
          bankList={[
            {
              image:
                "https://competitiondigest.com/wp-content/uploads/2014/12/bank-of-1.gif",
              value: "1"
            },
            {
              image:
                "https://competitiondigest.com/wp-content/uploads/2014/12/CBicons_03.png",
              value: "2"
            },
            {
              image:
                "https://competitiondigest.com/wp-content/uploads/2014/12/UNITED.png",
              value: "3"
            },
            {
              image:
                "https://competitiondigest.com/wp-content/uploads/2014/12/UNION.png",
              value: "4"
            },
            {
              image:
                "https://competitiondigest.com/wp-content/uploads/2014/12/UNION.png",
              value: "6"
            },
            {
              image:
                "https://competitiondigest.com/wp-content/uploads/2014/12/UNITED.png",
              value: "7"
            }
          ]}
        />
      </ManueDetails>
    );
  }
}
