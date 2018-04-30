import React from "react";
import styles from "./AllDescription.css";
import ElectronicsDescription from "./ElectronicsDescription";
import ElectronicsDescriptionSection from "./ElectronicsDescriptionSection";

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
    const data = {};

    const dataList = this.props.productContent
      .sort((a, b) => {
        if (a.key < b.key) {
          return -1;
        }
        if (a.key > b.key) {
          return 1;
        }
        return 0;
      })
      .map(val => {
        return val;
      });
    const templateName = this.props.templateName
      ? this.props.templateName[this.props.templateName.length - 1]
      : 0;

    dataList.forEach((val, i) => {
      if (val.key.slice(0, -1) in data) {
        data[val.key.slice(0, -1)].push(val);
      } else {
        data[val.key.slice(0, -1)] = [];
        data[val.key.slice(0, -1)].push(val);
      }
    });
    return (
      <div className={styles.base}>
        {templateName !== "3" && (
          <div className={styles.descriptionList}>
            {data.Section1 && (
              <ElectronicsDescriptionSection sectionData={data.Section1} />
            )}
            {this.state.showAll && (
              <React.Fragment>
                {data.Section2 && (
                  <ElectronicsDescriptionSection
                    sectionData={data.Section2}
                    offsetImage={templateName === "1"}
                  />
                )}

                {data.Section3 && (
                  <ElectronicsDescriptionSection sectionData={data.Section3} />
                )}
                {data.Section4 && (
                  <ElectronicsDescriptionSection sectionData={data.Section4} />
                )}
                {data.Section5 && (
                  <ElectronicsDescriptionSection sectionData={data.Section5} />
                )}
                {data.Section6 && (
                  <ElectronicsDescriptionSection sectionData={data.Section6} />
                )}
                {templateName === "1" && (
                  <React.Fragment>
                    {data.Section5 &&
                      data.Section5[1].value && (
                        <ElectronicsDescription
                          value={data.Section5[1].value}
                        />
                      )}
                    {data.Section6 &&
                      data.Section6[1].value && (
                        <ElectronicsDescription
                          value={data.Section6[1].value}
                        />
                      )}
                    {data.Section5 &&
                      data.Section5[2].value && (
                        <ElectronicsDescription
                          value={data.Section5[2].value}
                        />
                      )}
                    {data.Section6 &&
                      data.Section6[2].value && (
                        <ElectronicsDescription
                          value={data.Section6[2].value}
                        />
                      )}
                    {data.Section7 &&
                      data.Section7[0].value && (
                        <ElectronicsDescription
                          value={data.Section7[0].value}
                        />
                      )}
                    {data.Section8 &&
                      data.Section8[0].value && (
                        <ElectronicsDescription
                          value={data.Section8[0].value}
                        />
                      )}
                    {data.Section9 &&
                      data.Section9[0].value && (
                        <ElectronicsDescription
                          value={data.Section9[0].value}
                        />
                      )}
                    {data.Section8 &&
                      data.Section8[1].value && (
                        <ElectronicsDescription
                          value={data.Section8[1].value}
                        />
                      )}
                    {data.Section9 &&
                      data.Section9[1].value && (
                        <ElectronicsDescription
                          value={data.Section9[1].value}
                        />
                      )}
                    {data.Section8 &&
                      data.Section8[2].value && (
                        <ElectronicsDescription
                          value={data.Section8[2].value}
                        />
                      )}
                    {data.Section9 &&
                      data.Section9[2].value && (
                        <ElectronicsDescription
                          value={data.Section9[2].value}
                        />
                      )}
                  </React.Fragment>
                )}
              </React.Fragment>
            )}
          </div>
        )}
        {templateName === "3" && (
          <React.Fragment>
            {data.Section1 &&
              data.Section1[0].value && (
                <ElectronicsDescription value={data.Section1[0].value} />
              )}
            {data.Section2 &&
              data.Section2[0].value && (
                <ElectronicsDescription value={data.Section2[0].value} />
              )}
            {this.state.showAll && (
              <React.Fragment>
                {data.Section1 &&
                  data.Section1[1].value && (
                    <ElectronicsDescription value={data.Section1[1].value} />
                  )}
                {data.Section2 &&
                  data.Section2[1].value && (
                    <ElectronicsDescription value={data.Section2[1].value} />
                  )}

                {data.Section3 &&
                  data.Section3[0].value && (
                    <ElectronicsDescription value={data.Section3[0].value} />
                  )}
                {data.Section4 &&
                  data.Section4[0].value && (
                    <ElectronicsDescription value={data.Section4[0].value} />
                  )}
                {data.Section5 &&
                  data.Section5[0].value && (
                    <ElectronicsDescription value={data.Section5[0].value} />
                  )}
                {data.Section4 &&
                  data.Section4[1].value && (
                    <ElectronicsDescription value={data.Section4[1].value} />
                  )}
                {data.Section5 &&
                  data.Section5[1].value && (
                    <ElectronicsDescription value={data.Section5[1].value} />
                  )}
                {data.Section4 &&
                  data.Section4[2].value && (
                    <ElectronicsDescription value={data.Section4[2].value} />
                  )}
                {data.Section5 &&
                  data.Section5[2].value && (
                    <ElectronicsDescription value={data.Section5[2].value} />
                  )}
                {data.Section6 &&
                  data.Section6[0].value && (
                    <ElectronicsDescription value={data.Section6[0].value} />
                  )}
                {data.Section7 &&
                  data.Section7[0].value && (
                    <ElectronicsDescription value={data.Section7[0].value} />
                  )}
              </React.Fragment>
            )}
          </React.Fragment>
        )}
        <div
          className={
            !this.state.showAll ? styles.buttonHolder : styles.noBackground
          }
        >
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
