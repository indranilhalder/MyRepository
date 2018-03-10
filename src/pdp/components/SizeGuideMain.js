import React from "react";
import SizeGuideElement from "./SizeGuideElement";
import styles from "./SizeGuideMain.css";
import { Image } from "xelpmoc-core";
import Accordion from "../../general/components/Accordion.js";
import MDSpinner from "react-md-spinner";

export default class SizeGuideMain extends React.Component {
  componentDidMount() {
    this.props.getSizeGuide(this.props.productCode);
  }
  render() {
    if (this.props.loading) {
      return (
        <div className={styles.base}>
          <div className={styles.loadingIndicator}>
            <MDSpinner />
          </div>
        </div>
      );
    }
    if (this.props.sizeData && this.props.sizeData.sizeGuideList) {
      return (
        <div className={styles.base}>
          <div className={styles.imageHolder}>
            <div className={styles.image}>
              <Image fit="contain" image={this.props.sizeData.imageURL} />
            </div>
          </div>
          <div className={styles.sizeList}>
            {this.props.sizeData.sizeGuideList.map((list, i) => {
              return (
                <Accordion text={list.dimension} key={i}>
                  <SizeGuideElement data={list.dimensionList} />
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
