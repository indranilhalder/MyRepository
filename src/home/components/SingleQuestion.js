import React from "react";
import Carousel from "../../general/components/Carousel";
import SingleSelect from "../../general/components/SingleSelect";
import PropTypes from "prop-types";
import styles from "./SingleQuestion.css";
export default class SingleQuestion extends React.Component {
  handleClick(val) {
    if (this.props.onApply) {
      this.props.onApply(val);
    }
  }
  render() {
    const feedComponentData = this.props.feedComponentData;
    let singleQuestionData = this.props.feedComponentData.data.items;

    return (
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
                value={datum.description}
                image={datum.imageURL}
                onClick={val => {
                  this.handleClick(val);
                }}
              />
            );
          })}
      </Carousel>
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
