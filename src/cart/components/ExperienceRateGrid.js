import React from "react";
import Grid from "../../general/components/Grid";
import ExperienceRate from "./ExperienceRate";
import PropTypes from "prop-types";
import styles from "./ExperienceRateGrid.css";
import ExperienceRatingone from "./bad.svg";
import ExperienceRatingTwo from "./Bad_Red.svg";
import ExperienceRatingThree from "./Ok.svg";
import ExperienceRatingFour from "./Good_Red.svg";
import ExperienceRatingFive from "./Review.svg";
import ExperienceRatingSix from "./Review_Red.svg";
import ExperienceRatingSaven from "./good.svg";
import ExperienceRatingEight from "./Ok_red.svg";
import ExperienceRatingNine from "./love_Red.svg";
import ExperienceRatingTen from "./love.svg";
export default class ExperienceRateGrid extends React.Component {
  render() {
    console.log(this.props.value);
    return (
      <div className={styles.base}>
        <Grid elementWidthMobile={20} offset={20}>
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
        </Grid>
      </div>
    );
  }
}
