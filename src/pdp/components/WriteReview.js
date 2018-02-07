import React from "react";
import styles from "./writeReview.css";
import PropTypes from "prop-types";
import Input from "../../general/components/Input2";
import TextArea from "../../general/components/TextArea";
import FillupRating from "./FillupRating";
import Button from "../../general/components/Button";
let buttonColor = "#212121";
export default class WriteReview extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      title: "",
      comment: ""
    };
  }
  onChangeTitle(val) {
    if (this.props.onChangeTitle) {
      this.props.onChangeTitle(val);
    }
    this.setState({ title: val });
  }

  onChangeComment(val) {
    if (this.props.onChangeComment) {
      this.props.onChangeComment(val);
    }
    this.setState({ comment: val });
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.ratingContainer}>
          <div className={styles.ratingHeader}>Rate this product</div>
          <div className={styles.ratingBar}>
            <FillupRating rating={5} />
          </div>
        </div>
        <div className={styles.input}>
          <Input
            placeholder={"Title"}
            title={this.props.title ? this.props.title : this.state.title}
            onChange={val => this.onChangeTitle(val)}
          />
        </div>
        <TextArea
          comments={
            this.props.comment ? this.props.comment : this.state.comment
          }
          onChange={val => this.onChangeComment(val)}
        />
        <div className={styles.container}>
          <div className={styles.cancelButton}>cancel</div>
          <div className={styles.submitButton}>
            <Button
              className={styles.ratingBar}
              label={"Submit"}
              type="secondary"
              color={buttonColor}
              width={120}
            />
          </div>
        </div>
      </div>
    );
  }
}
WriteReview.propTypes = {
  onChangeTitle: PropTypes.func,
  title: "",
  comment: ""
};
