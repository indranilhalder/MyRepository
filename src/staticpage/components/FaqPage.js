import React from "react";
import styles from "./FaqPage.css";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
export default class FaqPage extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.items &&
          this.props.items.map((val, i) => {
            return (
              <div>
                {val.componentName === "cmsParagraphComponent" &&
                  !val.cmsParagraphComponent.content.includes(
                    "collapsible-panels"
                  ) && (
                    <div
                      dangerouslySetInnerHTML={{
                        __html: val.cmsParagraphComponent.content
                      }}
                    />
                  )}

                {val.componentName === "accountNavigationComponent" &&
                  val.accountNavigationComponent && (
                    <div className={styles.navigationHolder}>
                      <SelectBoxMobile
                        label="Select"
                        options={val.accountNavigationComponent.nodeList.map(
                          (val, i) => {
                            return {
                              value: val.url,
                              label: val.linkName
                            };
                          }
                        )}
                        onChange={ik => {
                          this.props.history.push(ik);
                        }}
                      />
                    </div>
                  )}
                {val.componentName === "cmsParagraphComponent" &&
                  val.cmsParagraphComponent.content.includes(
                    "collapsible-panels"
                  ) && (
                    <div
                      dangerouslySetInnerHTML={{
                        __html: val.cmsParagraphComponent.content.search(
                          "collapsible-panels"
                        )
                      }}
                    />
                  )}
              </div>
            );
          })}
      </div>
    );
  }
}
