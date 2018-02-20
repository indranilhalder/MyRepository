import React from "react";
import Grid from "../../general/components/Grid";
import ExperienceRate from "./ExperienceRate";
import PropTypes from "prop-types";
import styles from "./ExperienceRateGrid.css";
import ExperienceRatingone from "./img/bad.svg";
import ExperienceRatingTwo from "./img/Bad_Red.svg";
import ExperienceRatingThree from "./img/Ok.svg";
import ExperienceRatingFour from "./img/Good_Red.svg";
import ExperienceRatingFive from "./img/Review.svg";
import ExperienceRatingSix from "./img/Review_Red.svg";
import ExperienceRatingSaven from "./img/good.svg";
import ExperienceRatingEight from "./img/Ok_red.svg";
import ExperienceRatingNine from "./img/love_Red.svg";
import ExperienceRatingTen from "./img/love.svg";
export default class ExperienceRateGrid extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <ExperienceRate
          value="1"
          defaultImage={ExperienceRatingone}
          selected
          onClick={() => {
            this.selectedItem();
          }}
        />
        <ExperienceRate
          value="2"
          defaultImage={ExperienceRatingThree}
          selected
          onClick={() => {
            this.selectedItem();
          }}
        />
        <ExperienceRate
          value="3"
          defaultImage={ExperienceRatingFive}
          selected
          onClick={() => {
            this.selectedItem();
          }}
        />
        <ExperienceRate
          value="4"
          defaultImage={ExperienceRatingSaven}
          selected
          onClick={() => {
            this.selectedItem();
          }}
        />
        <ExperienceRate
          value="5"
          selected
          activeImage={ExperienceRatingNine}
          onClick={() => {
            this.selectedItem();
          }}
        />
      </div>
    );
  }
}
