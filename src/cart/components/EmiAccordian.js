import React from "react";
import GridSelect from "../../general/components/GridSelect";
import EmiCartSelect from "./EmiCartSelect";
// import styles from "./EmiAccordian.css";
export default class EmiAccordian extends React.Component {
  render() {
    return (
      <GridSelect elementWidthMobile={100} offset={0} limit={1}>
        {this.props.emiList &&
          this.props.emiList.map((val, i) => {
            return (
              <EmiCartSelect
                key={i}
                value={val.value}
                title={val.title}
                options={val.options}
              />
            );
          })}
      </GridSelect>
    );
  }
}
