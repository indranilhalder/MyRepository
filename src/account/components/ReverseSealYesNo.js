import React from "react";
import styles from "./ReverseSealYesNo.css";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import GridSelect from "../../general/components/GridSelect";
import YesNoQuestion from "./YesNoQuestion";
import PropTypes from "prop-types";
export default class ReverseSealYesNo extends React.Component {
  moreInfo() {
    if (this.props.moreInfo) {
      this.props.moreInfo();
    }
  }
  render() {
    const options = [
      {
        confirmation: "Yes",
        text: "I have the seal"
      },
      {
        confirmation: "No",
        text: "I don't have the seal"
      }
    ];
    return (
      <div className={styles.base}>
        <div className={styles.headerText}>
          Do You have the reverse seal with you?
        </div>
        <div className={styles.moreInfoButtonHolder}>
          <div className={styles.button}>
            <UnderLinedButton
              size="14px"
              fontFamily="regular"
              color="#000"
              label="More Info"
              onClick={() => this.moreInfo()}
            />
          </div>
        </div>
        <div className={styles.yesNoQuestionHolder}>
          {options &&
            options.length > 0 && (
              <GridSelect limit={1} offset={0} elementWidthMobile={100}>
                {options.map((val, i) => {
                  return (
                    <YesNoQuestion
                      text={val.text}
                      confirmation={val.confirmation}
                      value={val.confirmation}
                    />
                  );
                })}
              </GridSelect>
            )}
        </div>
      </div>
    );
  }
}
ReverseSealYesNo.propTypes = {
  moreInfo: PropTypes.func
};
