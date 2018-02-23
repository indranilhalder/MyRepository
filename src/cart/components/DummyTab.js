import React from "react";
import CheckOutHeader from "./CheckOutHeader";
import styles from "./DummyTab.css";

export default class DummyTab extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <CheckOutHeader
          confirmTitle={this.props.title}
          indexNumber={this.props.number}
        />
      </div>
    );
  }
}
