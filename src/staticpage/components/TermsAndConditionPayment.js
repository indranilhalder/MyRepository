import React from "react";
import styles from "./TermsAndConditionPayment.css";
import PropTypes from "prop-types";
import { TERMS_CONDITION } from "../../lib/constants";
export default class TermsAndConditionPayment extends React.Component {
  componentDidMount() {
    this.props.getTermsAndConditionDetails();
  }
  componentDidUpdate() {
    this.props.setHeaderText(TERMS_CONDITION);
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.termsAndCondition &&
          this.props.termsAndCondition.items &&
          this.props.termsAndCondition.items.map((val, i) => {
            return (
              <div>
                {val.componentName === "cmsParagraphComponent" &&
                  val.cmsParagraphComponent && (
                    <div
                      className={styles.dataHolder}
                      dangerouslySetInnerHTML={{
                        __html: val.cmsParagraphComponent.content
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
TermsAndConditionPayment.propTypes = {
  items: PropTypes.arrayOf(
    PropTypes.shape({
      cmsParagraphComponent: PropTypes.shape({
        content: PropTypes.string,
        componentName: PropTypes.string
      })
    })
  )
};
