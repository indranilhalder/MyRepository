import React from "react";
import styles from "./ColourSelect.css";
import PropTypes from "prop-types";

export default class ColourAdd extends React.Component {
  handleClick = () => {
    if (this.props.onSelect) {
      this.props.onSelect();
    }
  };
  render() {
    let classActive = styles.textHolder;
    if (this.props.selected === "true") {
      classActive = styles.textHolderActive;
    }
    return (
      <div className={styles.base}>
        <div className={styles.content}>
          <div
            className={styles.color}
            style={{ background: this.props.backgroundColor }}
          />
          <div className={styles.ovalImage}>
            <div className={classActive} onClick={() => this.handleClick()}>
              {this.props.colour}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
ColourAdd.propTypes = {
  colour: PropTypes.string,
  selected: PropTypes.bool
};
