import React from "react";
import PropTypes from "prop-types";
import Accordion from "../../general/components/Accordion.js";
import styles from "./ProductFeatures.css";

export default class JewelleryClassification extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <Accordion key={i} text={datum.key} headerFontSize={16}>
              <div className={styles.holder}>
                {datum.value.classificationListJwlry.map(val => {
                  return (
                    <div className={styles.content}>
                      <div className={styles.header}>{val.key}</div>
                      <div className={styles.description}>
                        {val.value.classificationListValueJwlry[0]}
                      </div>
                    </div>
                  );
                })}
              </div>
            </Accordion>
          );
        })}
      </div>
    );
  }
}

JewelleryClassification.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      key: PropTypes.string,
      value: PropTypes.shape({
        classificationListJwlry: PropTypes.arrayOf(
          PropTypes.shape({
            key: PropTypes.string,
            value: PropTypes.shape({
              classificationListValueJwlry: PropTypes.arrayOf(PropTypes.string)
            })
          })
        )
      })
    })
  )
};
