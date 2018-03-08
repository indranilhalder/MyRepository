import React from "react";
import ExperienceRate from "./ExperienceRate";
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
        <ExperienceRate value="1" defaultImage={ExperienceRatingone} />
        <ExperienceRate value="2" defaultImage={ExperienceRatingThree} />
        <ExperienceRate value="3" defaultImage={ExperienceRatingFive} />
        <ExperienceRate value="4" defaultImage={ExperienceRatingSaven} />
        <ExperienceRate value="5" activeImage={ExperienceRatingNine} />
      </div>
    );
  }
}
