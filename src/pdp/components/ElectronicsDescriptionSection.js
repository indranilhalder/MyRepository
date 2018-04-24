import React from "react";
import ElectronicsDescription from "./ElectronicsDescription";
import styles from "./ElectronicsDescriptionSection.css";
export default class ElectronicsDescriptionSection extends React.Component {
  renderSectionImage = () => {
    if (this.props.sectionData) {
      if ("imageList" in this.props.sectionData[0].value) {
        return (
          <ElectronicsDescription value={this.props.sectionData[0].value} />
        );
      } else if ("imageList" in this.props.sectionData[1].value) {
        return (
          <ElectronicsDescription value={this.props.sectionData[1].value} />
        );
      }
    }
  };
  renderSectionText = () => {
    if (this.props.sectionData) {
      if ("textList" in this.props.sectionData[0].value) {
        return (
          <ElectronicsDescription value={this.props.sectionData[0].value} />
        );
      } else if ("textList" in this.props.sectionData[1].value) {
        return (
          <ElectronicsDescription value={this.props.sectionData[1].value} />
        );
      } else {
        return null;
      }
    } else {
      return null;
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.topSection}>
          <div
            className={
              this.props.offsetImage ? styles.offsetImage : styles.imageHolder
            }
          >
            {this.renderSectionImage()}
          </div>
          {this.props.offsetImage && (
            <div className={styles.floatingText}>
              {this.renderSectionText()}
            </div>
          )}
        </div>
        <div
          className={
            this.props.offsetImage ? styles.noHeader : styles.description
          }
        >
          {this.renderSectionText()}
        </div>
      </div>
    );
  }
}
