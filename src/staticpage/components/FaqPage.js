import React from "react";
import styles from "./FaqPage.css";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import Accordion from "../../general/components/Accordion.js";
export default class FaqPage extends React.Component {
  renderQuestionAnswer(dautm) {
    const tm = JSON.parse(dautm);
    console.log(tm);
    return (
      tm &&
      tm.map((val, i) => {
        return (
          <Accordion
            faqQuestion={val.question_component}
            activeBackground="#f8f8f8"
          >
            <div dangerouslySetInnerHTML={{ __html: val.answer }} />
          </Accordion>
        );
      })
    );
  }

  render() {
    return (
      <div className={styles.base}>
        {this.props.items &&
          this.props.items.map((val, i) => {
            console.log(this.props.items);
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
                {val.componentName === "cmsTextComponent" &&
                  this.renderQuestionAnswer(val.cmsTextComponent.content)}
              </div>
            );
          })}
      </div>
    );
  }
}
