import React from "react";
import styles from "./CuratedFeature.css";
import PropTypes from "prop-types";

import Grid from "../../general/components/Grid";
export default class CuratedFeature extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headerText}>{this.props.header}</div>
        <div className={styles.featuresCardHolder}>
          <Grid offset={10} elementWidthMobile={50}>
            {this.props.curatedFeature.map((val, i) => {
              return (
                <div
                  className={styles.curatedCard}
                  style={{
                    backgroundImage: `url(${val.imageUrl})`,
                    backgroundSize: "cover"
                  }}
                >
                  <div className={styles.overlay}>
                    <div className={styles.overlayTextHolder}>
                      {val.header && (
                        <div className={styles.featuresHeader}>
                          {val.header}
                        </div>
                      )}
                      {val.text && (
                        <div className={styles.featuresText}>{val.text}</div>
                      )}
                    </div>
                  </div>
                </div>
              );
            })}
          </Grid>
        </div>
      </div>
    );
  }
}
CuratedFeature.propTypes = {
  header: PropTypes.string,
  curatedFeature: PropTypes.arrayOf(
    PropTypes.shape({
      header: PropTypes.string,
      text: PropTypes.string,
      imageUrl: PropTypes.string
    })
  )
};
