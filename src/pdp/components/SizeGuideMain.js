import React from "react";
import SizeGuideElement from "./SizeGuideElement";
import styles from "./SizeGuideMain.css";
import { Image } from "xelpmoc-core";
import Accordion from "../../general/components/Accordion.js";
import Loader from "../../general/components/Loader";

export default class SizeGuideMain extends React.Component {
  componentDidMount() {
    this.props.getSizeGuide(this.props.productCode);
  }
  render() {
    if (this.props.loading) {
      return <Loader />;
    }
    if (this.props.sizeData && this.props.sizeData.sizeGuideList) {
      return (
        <div className={styles.base}>
          <div className={styles.imageHolder}>
            <div className={styles.image}>
              <Image fit="contain" image={this.props.sizeData.imageURL} />
            </div>
          </div>
          {this.props.sizeData.sizeGuideList && (
            <div className={styles.sizeList}>
              {this.props.sizeData.sizeGuideList.map((list, i) => {
                return (
                  <Accordion
                    text={list.dimensionSize}
                    key={i}
                    offset={20}
                    activeBackground="#f8f8f8"
                  >
                    <SizeGuideElement data={list.dimensionList} />
                  </Accordion>
                );
              })}
            </div>
          )}
        </div>
      );
    } else {
      return (
        <div className={styles.noSizeGuideHolder}>
          <div className={styles.noSizeGuide}>No Size Guide Available</div>
        </div>
      );
    }
  }
}
