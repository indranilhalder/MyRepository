import React from "react";
import Carousel from "../../general/components/Carousel";
import SingleSelect from "../../general/components/SingleSelect";
import PropTypes from "prop-types";
import styles from "./SingleQuestion.css";
import LoadingScreen from "../../general/components/LoadingScreen.js";
import {
  SINGLE_SELECT_DESCRIPTION_COPY,
  SINGLE_SELECT_HEADING_COPY
} from "../../lib/constants.js";
export default class SingleQuestion extends React.Component {
  handleClick(val) {
    if (this.props.onApply) {
      this.props.onApply(
        val,
        this.props.feedComponentData.data.questionId,
        this.props.positionInFeed
      );
    }
  }
  render() {
    let singleQuestionData = this.props.feedComponentData.data.items;
    if (this.props.loading) {
      return (
        <LoadingScreen
          body={SINGLE_SELECT_DESCRIPTION_COPY}
          header={SINGLE_SELECT_HEADING_COPY}
        />
      );
    }

    return (
      <div className={styles.base}>
        <Carousel
          headerComponent={
            <div className={styles.header}>
              {this.props.feedComponentData.data.title}
            </div>
          }
          elementWidthDesktop={20}
          elementWidthMobile={30}
        >
          {singleQuestionData &&
            singleQuestionData.map((datum, i) => {
              return (
                <SingleSelect
                  key={i}
                  text={datum.title}
                  value={datum.optionId}
                  image={datum.imageURL}
                  onClick={val => {
                    this.handleClick(val);
                  }}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
SingleQuestion.propTypes = {
  header: PropTypes.string,
  onApply: PropTypes.func,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      text: PropTypes.string,
      value: PropTypes.string,
      image: PropTypes.string
    })
  )
};
