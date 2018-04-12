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
                  val.cmsParagraphComponent && (
                    <div className={styles.header}>
                      {val.cmsParagraphComponent.content}
                    </div>
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
              </div>
            );
          })}
      </div>
    );
  }
}
