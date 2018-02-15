import React from "react";
import styles from "./ExperienceRate.css";
import { Icon } from "xelpmoc-core";
import badIcon from "./img/bad.svg";

export default class ExperienceRate extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.retingIconHolder}>
          <div className={styles.iconSet}>
            <Icon image={badIcon} size={36} />
          </div>
        </div>
      </div>
    );
  }
}
