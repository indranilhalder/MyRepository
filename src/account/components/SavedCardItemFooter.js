import React from "react";
import styles from "./SavedCardItemFooter.css";
import ColourButton from "../../general/components/ColourButton";
import PropTypes from "prop-types";
export default class SavedCardItemFooter extends React.Component {
  removeSavedCardDetails() {
    if (this.props.removeSavedCardDetails) {
      this.props.removeSavedCardDetails();
    }
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.removeHolder}>
          <div className={styles.remove}>
            <ColourButton
              label={this.props.buttonLabel}
              onClick={() => this.removeSavedCardDetails()}
            />
          </div>
        </div>
      </div>
    );
  }
}
SavedCardItemFooter.propTypes = {
  underlineButtonLabel: PropTypes.string,
  buttonLabel: PropTypes.string
};
SavedCardItemFooter.defaultProps = {
  underlineButtonLabel: "Remove"
};
