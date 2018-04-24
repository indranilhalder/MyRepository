import React from "react";
import styles from "./ReverseSealYesNo.css";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import GridSelect from "../../general/components/GridSelect";
import YesNoQuestion from "./YesNoQuestion";
import PropTypes from "prop-types";
const LABEL = "More Info";
export default class ReverseSealYesNo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      confirmation: null
    };
  }
  moreInfo() {
    if (this.props.moreInfo) {
      this.props.moreInfo();
    }
  }
  onSelectReverseSeal(val) {
    if (this.props.selectReverseSeal) {
      this.props.selectReverseSeal(val);
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
        {this.props.isMoreInfo && (
          <div className={styles.moreInfoButtonHolder}>
            <div className={styles.button}>
              <UnderLinedButton
                size="14px"
                fontFamily="regular"
                color="#000"
                label={LABEL}
                onClick={() => this.moreInfo()}
              />
            </div>
          </div>
        )}

        <div className={styles.yesNoQuestionHolder}>
          {options &&
            options.length > 0 && (
              <GridSelect
                limit={1}
                offset={0}
                elementWidthMobile={100}
                onSelect={val => this.onSelectReverseSeal(val)}
              >
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
  moreInfo: PropTypes.func,
  isMoreInfo: PropTypes.bool
};
ReverseSealYesNo.defaultProps = {
  isMoreInfo: false
};
