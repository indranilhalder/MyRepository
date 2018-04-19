import React from "react";
import styles from "./CMSParagraphComponent.css";

export default class CMSParagraphComponent extends React.Component {
  render() {
    return (
      <div
        className={styles.contentHolder}
        dangerouslySetInnerHTML={{
          __html: this.props.data.content
        }}
      />
    );
  }
}
