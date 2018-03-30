import React from "react";
import ContentWidget from "./ContentWidget";
import MDSpinner from "react-md-spinner";
import styles from "./ContentWidgetWrapper.css";
export default class ContentWidgetWrapper extends React.Component {
  renderLoader() {
    return <MDSpinner />;
  }
  render() {
    if (
      this.props.loading &&
      this.props.feedComponentData &&
      this.props.feedComponentData.items &&
      this.props.feedComponentData.items.length < 1
    ) {
      return this.renderLoader();
    } else if (this.props.feedComponentData.items.length > 1) {
      return (
        <React.Fragment>
          <div className={styles.header}>
            {this.props.feedComponentData.title}
          </div>
          <ContentWidget
            allData={this.props.feedComponentData.items}
            history={this.props.history}
          />
        </React.Fragment>
      );
    } else {
      return this.renderLoader();
    }
  }
}
