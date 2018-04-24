import React, { Component } from "react";
import SecondaryLoader from "../../general/components/SecondaryLoader.js";
import Feed from "../../home/components/Feed";
import styles from "./StaticPage.css";

export default class StaticPage extends Component {
  componentDidMount() {
    const slug = this.props.match.params.slug;
    this.props.getStaticPage(slug);
    this.props.setHeaderText(slug.charAt(0).toUpperCase() + slug.slice(1));
  }

  componentDidUpdate(prevProps) {
    if (this.props.match.params.slug !== prevProps.match.params.slug) {
      const slug = this.props.match.params.slug;
      this.props.getStaticPage(slug);
      this.props.setHeaderText(slug.charAt(0).toUpperCase() + slug.slice(1));
    }
  }

  render() {
    if (this.props.loading || !this.props.data) {
      return <SecondaryLoader />;
    } else {
      return (
        <div className={styles.base}>
          <Feed
            homeFeedData={this.props.data}
            setHeaderText={this.props.setHeaderText}
          />
        </div>
      );
    }
  }
}
