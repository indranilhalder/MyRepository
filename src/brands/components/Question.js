import React from "react";
import styles from "./Question.css";
import PropTypes from "prop-types";
import GridSelect from "../../general/components/GridSelect";
import CheckBoxPoint from "../../general/components/CheckBoxPoint";
export default class Question extends React.Component {
  render() {
    const label = [{ label: "Yes, sure" }, { label: "No thanks" }];
    return (
      <div className={styles.base}>
        <div className={styles.questionText}>{this.props.question}</div>
        <div className={styles.answerHolder}>
          <GridSelect limit={1} offset={0} elementWidthMobile={100}>
            {label.map((val, i) => {
              return <CheckBoxPoint label={val.label} value={i} />;
            })}
          </GridSelect>
        </div>
      </div>
    );
  }
}
Question.propTypes = {
  question: PropTypes.string
};
Question.defaultProps = {
  question:
    "Are you  interested in receiving emails about new products and collections from westside?"
};
