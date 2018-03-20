import React from "react";
import CategoryL1 from "./CategoryL1";
import CategoryL2 from "./CategoryL2";
import CategoryL3 from "./CategoryL3";
import styles from "./CategoriesPage.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class CategoriesPage extends React.Component {
  componentDidMount() {
    this.props.getCategories();
  }
  handleClick(webURL) {
    const urlSuffix = webURL.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  }

  render() {
    let categoriesData = this.props.categories;
    return (
      <div className={styles.base}>
        {categoriesData &&
          categoriesData.subCategories &&
          categoriesData.subCategories.map((categories, i) => {
            return (
              <CategoryL1
                label={categories.category_name}
                key={`${categories.category_name}${i}`}
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
          })}
      </div>
    );
  }
}
