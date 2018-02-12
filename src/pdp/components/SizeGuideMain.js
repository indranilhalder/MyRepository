import React from "react";
import SizeGuide from "./SizeGuide";
import styles from "./SizeGuideMain.css";
import { Image } from "xelpmoc-core";
import Accordion from "../../general/components/Accordion.js";
export default class SizeGuideMain extends React.Component {
  render() {
    if (this.props.sizeData) {
      return (
        <div className={styles.base}>
          <div className={styles.imageHolder}>
            <div className={styles.image}>
              <Image
                fit="contain"
                image={this.props.sizeData.sizeGuideImageURL}
              />
            </div>
          </div>
          <div className={styles.sizeList}>
            {this.props.sizeData.sizeGuideList.map((list, i) => {
              return (
                <Accordion text={list.dimensionTitle} key={i}>
                  <SizeGuide data={list.dimensionList} />
                </Accordion>
              );
            })}
          </div>
        </div>
      );
    } else {
      return null;
    }
  }
}
