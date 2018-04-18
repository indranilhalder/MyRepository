import React, { Component } from "react";
import SecondaryLoader from "../../general/components/SecondaryLoader.js";
import styles from "./StaticPage.css";
import CMSParagraphComponent from "./CMSParagraphComponent.js";
import SimpleBannerComponent from "./SimpleBannerComponent.js";
import CMSTextComponent from "./CMSTextComponent.js";
import AccountNavigationComponent from "./AccountNavigationComponent.js";
import map from "lodash.map";

const typeComponentMapping = {
  "CMS Paragraph Component": props => <CMSParagraphComponent {...props} />,
  "Simple Banner Component": props => {
    return (
      <div className={styles.simpleBannerHolder}>
        {" "}
        <SimpleBannerComponent {...props} />{" "}
      </div>
    );
  },
  "CMS Text Component": props => {
    console.log("IN CMS TEXT COMPONENT");
    console.log(props.data.content);
    let parsedContent;
    try {
      parsedContent = JSON.parse(props.data.content);
    } catch (e) {
      props.displayToast("JSON Parse error, check static page content");
    }
    return (
      parsedContent &&
      map(parsedContent, content => {
        return <CMSTextComponent data={content} />;
      })
    );
  },
  "Account Navigation Component": props => (
    <AccountNavigationComponent {...props} />
  )
};

export default class StaticPage extends Component {
  componentDidMount() {
    this.props.getStaticPage(this.props.match.params.slug);
  }

  componentDidUpdate(prevProps) {
    if (this.props.match.params.slug !== prevProps.match.params.slug) {
      this.props.getStaticPage(this.props.match.params.slug);
    }
  }

  render() {
    if (this.props.loading || !this.props.data) {
      return <SecondaryLoader />;
    } else {
      return (
        <div className={styles.base}>
          {this.props.data.items &&
            this.props.data.items.map(item => {
              return (
                typeComponentMapping[item.type] &&
                typeComponentMapping[item.type]({
                  data: item,
                  history: this.props.history,
                  displayToast: this.props.displayToast
                })
              );
            })}
        </div>
      );
    }
  }
}
