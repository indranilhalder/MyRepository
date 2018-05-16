import React, { Component } from "react";
import SecondaryLoader from "../../general/components/SecondaryLoader.js";
import Feed from "../../home/components/Feed";
import styles from "./StaticPage.css";

export default class StaticPage extends Component {
  componentDidMount() {
    const slug = this.props.match.params.slug;
    this.props.getStaticPage(slug);
  }

  componentDidUpdate(prevProps) {
    if (this.props.match.params.slug !== prevProps.match.params.slug) {
      const slug = this.props.match.params.slug;
      this.props.getStaticPage(slug);
    }
  }

  render() {
    if (this.props.loading || !this.props.data) {
      return <SecondaryLoader />;
    } else {
      return (
        <div className={styles.base}>
          <Feed
            feedType={this.props.feedType}
            homeFeedData={this.props.data}
            setHeaderText={this.props.setHeaderText}
          />
        </div>
      );
    }
  }
}
