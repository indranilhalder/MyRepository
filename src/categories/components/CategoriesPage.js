import React from "react";
import CategoryL1 from "./CategoryL1";
import CategoryL2 from "./CategoryL2";
import CategoryL3 from "./CategoryL3";
import SearchContainer from "../../search/SearchContainer";
import MobileFooter from "../../general/components/MobileFooter";
import styles from "./CategoriesPage.css";
export default class CategoriesPage extends React.Component {
  render() {
    const data = {
      type: "categoryListHierarchyWSData",
      subCategories: [
        {
          categoryImageURl:
            "/medias/sys_master/root/8949121187870/sitelogo.jpg",
          category_name: "Travel and luggage",
          subCategories: [
            {
              category_name: "Travel Bags",
              subCategories: [
                {
                  category_name: "Travel Bags",
                  webURL: "/travel-and-luggage-travel-bags/c-msh1710"
                },
                {
                  category_name: "Duffle Bags",
                  webURL: "/c-msh1710102"
                },
                {
                  category_name: "Luggage Sets",
                  webURL: "/c-msh1710103"
                },
                {
                  category_name: "Suitcase",
                  webURL: "/c-msh1710100"
                },
                {
                  category_name: "Trolley",
                  webURL: "/c-msh1710101"
                }
              ],
              webURL: "/travel-and-luggage-travel-bags/c-msh1710"
            },
            {
              category_name: "Office Bags",
              subCategories: [
                {
                  category_name: "Office Bags",
                  webURL: "/travel-and-luggage-office-bags/c-msh1711"
                },
                {
                  category_name: "Briefcases",
                  webURL: "/c-msh1711101"
                },
                {
                  category_name: "Messenger Bags",
                  webURL: "/c-msh1711102"
                },
                {
                  category_name: "Satchels",
                  webURL: "/c-msh1711103"
                },
                {
                  category_name: "Laptop Bags",
                  webURL: "/c-msh1711100"
                }
              ],
              webURL: "/travel-and-luggage-office-bags/c-msh1711"
            },
            {
              category_name: "Outdoor & Sport Bags",
              subCategories: [
                {
                  category_name: "Outdoor & Sport Bags",
                  webURL: "/travel-and-luggage-outdoor-sport-bags/c-msh1712"
                },
                {
                  category_name: "Gym Bags"
                },
                {
                  category_name: "Rucksacks"
                },
                {
                  category_name: "Knapsacks"
                },
                {
                  category_name: "Backpacks"
                }
              ],
              webURL: "/travel-and-luggage-outdoor-sport-bags/c-msh1712"
            },
            {
              category_name: "Accessories",
              subCategories: [
                {
                  category_name: "Accessories",
                  webURL: "/travel-and-luggage-accessories/c-msh1714"
                },
                {
                  category_name: "Toiletry Kit"
                },
                {
                  category_name: "Passport Holder"
                },
                {
                  category_name: "Makeup Box"
                },
                {
                  category_name: "Travel Pouches"
                },
                {
                  category_name: "Makeup Pouches"
                },
                {
                  category_name: "Document Holder"
                },
                {
                  category_name: "Waist Pouches"
                },
                {
                  category_name: "Shoe Bags"
                }
              ],
              webURL: "/travel-and-luggage-accessories/c-msh1714"
            },
            {
              category_name: "Kid Bags",
              subCategories: [
                {
                  category_name: "Kid Bags",
                  webURL: "/travel-and-luggage-kids-bags/c-msh1713"
                },
                {
                  category_name: "School Bags"
                },
                {
                  category_name: "Diaper Bags "
                }
              ],
              webURL: "/travel-and-luggage-kids-bags/c-msh1713"
            },
            {
              category_name: "Test L2",
              subCategories: [
                {
                  category_name: "Travel Test L3 1",
                  webURL: "/c-msh1710"
                },
                {
                  category_name: "Test Test L3 2 "
                },
                {
                  category_name: "CLP Luggage"
                }
              ],
              webURL: "/c-msh1710"
            }
          ],
          webURL: "https://uat2.tataunistore.com/travel-and-luggage/c-msh17"
        },
        {
          categoryImageURl:
            "//assetsuat2.tataunistore.com/medias/sys_master/images/8896490799134.jpg",
          category_name: "Home",
          subCategories: [
            {
              category_name: "Test L2",
              subCategories: [
                {
                  category_name: "Travel Test L3 1",
                  webURL: "/c-msh1710"
                },
                {
                  category_name: "Test Test L3 2 "
                },
                {
                  category_name: "CLP Luggage"
                }
              ],
              webURL: "/c-msh1710"
            },
            {
              category_name: "Test L2",
              subCategories: [
                {
                  category_name: "Travel Test L3 1",
                  webURL: "/c-msh1710"
                },
                {
                  category_name: "Test Test L3 2 "
                },
                {
                  category_name: "CLP Luggage"
                }
              ],
              webURL: "/c-msh1710"
            },
            {
              category_name: "Test L2",
              subCategories: [
                {
                  category_name: "Travel Test L3 1",
                  webURL: "/c-msh1710"
                },
                {
                  category_name: "Test Test L3 2 "
                },
                {
                  category_name: "CLP Luggage"
                }
              ],
              webURL: "/c-msh1710"
            },
            {
              category_name: "Test L2",
              subCategories: [
                {
                  category_name: "Travel Test L3 1",
                  webURL: "/c-msh1710"
                },
                {
                  category_name: "Test Test L3 2 "
                },
                {
                  category_name: "CLP Luggage"
                }
              ],
              webURL: "/c-msh1710"
            }
          ],
          webURL: "/"
        }
      ]
    };
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <SearchContainer />
        </div>
        {data.subCategories.map((categories, i) => {
          return (
            <CategoryL1 label={categories.category_name}>
              {categories.subCategories &&
                categories.subCategories.map((category, i) => {
                  return (
                    <CategoryL2
                      label={category.category_name}
                      url={category.webURL}
                    >
                      {category.subCategories &&
                        category.subCategories.map((subCategory, i) => {
                          return (
                            <CategoryL3
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
        <MobileFooter selected={"categories"} history={this.props.history} />
      </div>
    );
  }
}
