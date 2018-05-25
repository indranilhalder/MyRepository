import React from "react";
import ContentWidget from "./ContentWidget";
import Loader from "../../general/components/Loader";
import styles from "./ContentWidgetWrapper.css";
export default class ContentWidgetWrapper extends React.Component {
  renderLoader() {
    return <Loader />;
  }
  render() {
    if (
      this.props.loading &&
      this.props.feedComponentData &&
      this.props.feedComponentData.items &&
      this.props.feedComponentData.items.length < 1
    ) {
      return this.renderLoader();
    } else if (
      this.props.feedComponentData &&
      this.props.feedComponentData.items &&
      this.props.feedComponentData.items.length > 1
    ) {
      return (
        <div className={styles.base}>
          <div className={styles.header}>
            {this.props.feedComponentData.title}
          </div>
          <ContentWidget
            allData={this.props.feedComponentData.items}
            history={this.props.history}
            setClickedElementId={this.props.setClickedElementId}
          />
        </div>
      );
    } else {
      return null;
    }
  }
}
