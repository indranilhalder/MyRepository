import React from "react";
import styles from "./CuratedFeature.css";
import PropTypes from "prop-types";
import Image from "../../xelpmoc-core/Image";
import Grid from "../../general/components/Grid";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class CuratedFeature extends React.Component {
  handleClick(webURL) {
    let urlSuffix = webURL.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  }
  render() {
    let feedComponentData = this.props.feedComponentData;
    return (
      <div
        className={
          this.props.positionInFeed === 1 ? styles.firstItemBase : styles.base
        }
      >
        <div className={styles.headerText}>{feedComponentData.title}</div>
        <div className={styles.featuresCardHolder}>
          <Grid offset={10} elementWidthMobile={50}>
            {feedComponentData &&
              feedComponentData.items.map((val, i) => {
                return (
                  <div
                    className={styles.curatedCard}
                    onClick={() => this.handleClick(val.webURL)}
                    key={i}
                  >
                    <div className={styles.overlayTextHolder}>
                      {val.title && (
                        <div className={styles.featuresHeader}>{val.title}</div>
                      )}
                      {val.description && (
                        <div className={styles.featuresText}>
                          {val.description}
                        </div>
                      )}
                    </div>

                    <div className={styles.imageHolder}>
                      <Image image={val.imageURL} fit="cover" />
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
  onClick: PropTypes.func,
  curatedFeature: PropTypes.arrayOf(
    PropTypes.shape({
      header: PropTypes.string,
      text: PropTypes.string,
      imageUrl: PropTypes.string
    })
  )
};
