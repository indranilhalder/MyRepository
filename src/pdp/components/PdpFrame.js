import React from "react";
import PdpFooter from "./PdpFooter";
import PropTypes from "prop-types";
import styles from "./PdpFrame.css";
export default class PdpFrame extends React.Component {
  onSave() {
    if (this.props.onSave) {
      this.props.onSave();
    }
  }
  onAddToBag() {
    if (this.props.onAddToBag) {
      this.props.onAddToBag();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.children}
        <PdpFooter
          onSave={() => this.onSave()}
          onAddToBag={() => this.onAddToBag()}
        />
      </div>
    );
  }
}
