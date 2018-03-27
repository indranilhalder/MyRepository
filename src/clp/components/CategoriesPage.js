import React from "react";
import CategoryL1 from "./CategoryL1";
import CategoryL2 from "./CategoryL2";
import CategoryL3 from "./CategoryL3";
import styles from "./CategoriesPage.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class CategoriesPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      openIndex: null
    };
  }

  componentDidMount() {
    this.props.getCategories();
  }
  handleClick(webURL) {
    const urlSuffix = webURL.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  }
  handleOpenL1(val) {
    console.log(val);

    this.setState({ openIndex: val });
  }
  closeItem() {
    this.setState({ openIndex: null });
  }
  render() {
    let categoriesData = this.props.categories;
    return (
      <div className={styles.base}>
        {categoriesData &&
          categoriesData.subCategories &&
          categoriesData.subCategories.map((categories, i) => {
            if (this.state.openIndex === null || this.state.openIndex === i) {
              return (
                <CategoryL1
                  isOpen={this.state.openIndex === i}
                  label={categories.category_name}
                  key={`${categories.category_name}${i}`}
                  openItem={() => {
                    this.handleOpenL1(i);
                  }}
                  closeItem={() => {
                    this.closeItem();
                  }}
                >
                  {categories.subCategories &&
                    categories.subCategories.map((category, i) => {
                      return (
                        <CategoryL2
                          label={category.category_name}
                          url={category.webURL}
                          key={`${category.category_name}${i}`}
                        >
                          {category.subCategories &&
                            category.subCategories.map((subCategory, i) => {
                              return (
                                <CategoryL3
                                  onClick={val => this.handleClick(val)}
                                  key={`${subCategory.category_name}${i}`}
                                  label={subCategory.category_name}
                                  url={subCategory.webURL}
                                />
                              );
                            })}
                        </CategoryL2>
                      );
                    })}
                </CategoryL1>
              );
            } else {
              return null;
            }
          })}
      </div>
    );
  }
}
