import React from "react";
import Image from "../../xelpmoc-core/Image";
import Button from "../../general/components/Button";
import styles from "./StoryWidget.css";

export default class StoryProduct extends React.Component {
  render() {
    return (
      <div className={styles.content}>
        <div className={styles.productImage}>
          <div className={styles.imageActual}>
            <Image image="http://img.tatacliq.com/images/i3/252Wx374H/MP000000002333309_252Wx374H_20180127024115.jpeg" />
          </div>
        </div>
        <div className={styles.productSection}>
          <div className={styles.row}>A product name</div>
          <div className={styles.row}>4095 </div>
          <div className={styles.button}>
            <Button label="View Product" type="secondary" />
          </div>
        </div>
      </div>
    );
  }
}
