import React from "react";
import styles from "./AllDescription.css";
import ElectronicsDescription from "./ElectronicsDescription";
import PropTypes from "prop-types";
import Button from "../../general/components/Button.js";
export default class AllDescription extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showAll: false,
      label: "View More"
    };
  }
  showMore() {
    this.setState({ showAll: !this.state.showAll }, () => {
      if (this.state.label === "View More") {
        this.setState({ label: "View Less" });
      } else {
        this.setState({ label: "View More" });
      }
    });
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.descriptionList}>
          {this.props.descriptionList &&
            this.props.descriptionList
              .filter((val, i) => {
                return !this.state.showAll ? i < 1 : true;
              })
              .map((val, i) => {
                return (
                  <ElectronicsDescription
                    imageUrl={val.imageUrl}
                    descriptionHeader={val.descriptionHeader}
                    description={val.description}
                    value={i}
                  />
                );
              })}
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              type="hollow"
              height={40}
              label={this.state.label}
              width={140}
              textStyle={{ color: "#212121", fontSize: 14 }}
              onClick={() => this.showMore()}
            />
          </div>
        </div>
      </div>
    );
  }
}
AllDescription.propTypes = {
  onNewAddress: PropTypes.func,
  descriptionList: PropTypes.arrayOf(
    PropTypes.shape({
      imageUrl: PropTypes.string,
      descriptionHeader: PropTypes.string,
      description: PropTypes.string
    })
  )
};
