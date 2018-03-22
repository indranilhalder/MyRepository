import React from "react";
import Accordion from "../../general/components/Accordion";
import styles from "./ProductFeatures.css";
export default class PriceBreakUp extends React.Component {
  render() {
    return (
      <Accordion text="Price breakup" headerFontSize={16}>
        <div className={styles.holder}>
          {this.props.data.map(val => {
            return (
              <div className={styles.content}>
                <div className={styles.header}>{val.name}</div>
                <div className={styles.description}>
                  {val.price.formattedValue}
                </div>
              </div>
            );
          })}
        </div>
      </Accordion>
    );
  }
}
