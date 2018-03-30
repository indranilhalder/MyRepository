import React from "react";
import ContentWidget from "./ContentWidget";
import MDSpinner from "react-md-spinner";
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
        <ContentWidget
          allData={this.props.feedComponentData.items}
          history={this.props.history}
        />
      );
    } else {
      return this.renderLoader();
    }
  }
}
