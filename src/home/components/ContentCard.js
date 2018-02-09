import React from "react";
import styles from "./ContentCard.css";
import { Image } from "xelpmoc-core";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import PropTypes from "prop-types";

export default class ContentCard extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.content}>
          <Image image={this.props.image} color="transparent" />
          <div className={styles.overlay}>
            <div className={styles.header}>{this.props.header}</div>
            <div className={styles.label}>
              {this.props.description}
              <div className={styles.buttonBox}>
                <div className={styles.button}>
                  <UnderLinedButton
                    label="Read More"
                    color="#fff"
                    onClick={() => {
                      this.handleClick();
                    }}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
ContentCard.propTypes = {
  image: PropTypes.string,
  header: PropTypes.string,
  description: PropTypes.string,
  onClick: PropTypes.func
};
