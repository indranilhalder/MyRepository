import React from "react";
import styles from "./FaqPage.css";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import Accordion from "../../general/components/Accordion.js";
import { FAQ_PAGE } from "../../lib/constants";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
export default class FaqPage extends React.Component {
  componentDidMount() {
    this.props.getFaqDetails();
  }
  componentDidUpdate() {
    this.props.setHeaderText(FAQ_PAGE);
  }
  handleItemClick = url => {
    const urlSuffix = url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  };

  renderQuestionAnswer(dautm) {
    const tm = JSON.parse(dautm);

    return (
      tm &&
      tm.map((val, i) => {
        return (
          <div className={styles.questionAnswerHolder}>
            <Accordion
              headerElement={true}
              faqQuestion={val.question_component}
              activeBackground="#f8f8f8"
            >
              <div
                className={styles.answer}
                dangerouslySetInnerHTML={{ __html: val.answer }}
              />
            </Accordion>
          </div>
        );
      })
    );
  }

  render() {
    return (
      <div className={styles.base}>
        {this.props.faq &&
          this.props.faq.items &&
          this.props.faq.items.map((val, i) => {
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
                        height={40}
                        options={val.accountNavigationComponent.nodeList.map(
                          (val, i) => {
                            return {
                              value: val.url,
                              label: val.linkName
                            };
                          }
                        )}
                        onChange={ik => {
                          this.handleItemClick(ik);
                        }}
                      />
                    </div>
                  )}
                {val.componentName === "cmsTextComponent" &&
                  val.cmsTextComponent.content &&
                  this.renderQuestionAnswer(val.cmsTextComponent.content)}
              </div>
            );
          })}
      </div>
    );
  }
}
