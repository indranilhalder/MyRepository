import React from "react";
import PropTypes from "prop-types";
import CategoryL1 from "./CategoryL1";
import CategoryL2 from "./CategoryL2";
import CategoryL3 from "./CategoryL3";
import InformationHeader from "../../general/components/InformationHeader";
import styles from "./CategoriesPage.css";
export default class CategoriesPage extends React.Component {
  render() {
    const data = {
      type: "departmentListHierarchyWsDTO",
      shopbydepartment: [
        {
          subCategories: [
            {
              sub_category_id: "MSH1710",
              sub_category_name: "Travel Bags",
              supersubCategories: [
                {
                  super_sub_category_id: "MSH1710102",
                  super_sub_category_name: "Duffle Bags"
                },
                {
                  super_sub_category_id: "MSH1710103",
                  super_sub_category_name: "Luggage Sets"
                },
                {
                  super_sub_category_id: "MSH1710100",
                  super_sub_category_name: "Suitcase"
                },
                {
                  super_sub_category_id: "MSH1710101",
                  super_sub_category_name: "Trolley"
                }
              ]
            },
            {
              sub_category_id: "MSH1711",
              sub_category_name: "Office Bags",
              supersubCategories: [
                {
                  super_sub_category_id: "MSH1711101",
                  super_sub_category_name: "Briefcases"
                },
                {
                  super_sub_category_id: "MSH1711102",
                  super_sub_category_name: "Messenger Bags"
                },
                {
                  super_sub_category_id: "MSH1711103",
                  super_sub_category_name: "Satchels"
                },
                {
                  super_sub_category_id: "MSH1711100",
                  super_sub_category_name: "Laptop Bags"
                }
              ]
            },
            {
              sub_category_id: "MSH1712",
              sub_category_name: "Outdoor & Sport Bags",
              supersubCategories: [
                {
                  super_sub_category_id: "MSH1712101",
                  super_sub_category_name: "Gym Bags"
                },
                {
                  super_sub_category_id: "MSH1712102",
                  super_sub_category_name: "Rucksacks"
                },
                {
                  super_sub_category_id: "MSH1712103",
                  super_sub_category_name: "Knapsacks"
                },
                {
                  super_sub_category_id: "MSH1712100",
                  super_sub_category_name: "Backpacks"
                }
              ]
            },
            {
              sub_category_id: "MSH1714",
              sub_category_name: "Accessories",
              supersubCategories: [
                {
                  super_sub_category_id: "MSH1714100",
                  super_sub_category_name: "Toiletry Kit"
                },
                {
                  super_sub_category_id: "MSH1714101",
                  super_sub_category_name: "Passport Holder"
                },
                {
                  super_sub_category_id: "MSH1714102",
                  super_sub_category_name: "Makeup Box"
                },
                {
                  super_sub_category_id: "MSH1714103",
                  super_sub_category_name: "Travel Pouches"
                },
                {
                  super_sub_category_id: "MSH1714104",
                  super_sub_category_name: "Makeup Pouches"
                },
                {
                  super_sub_category_id: "MSH1714105",
                  super_sub_category_name: "Document Holder"
                },
                {
                  super_sub_category_id: "MSH1714106",
                  super_sub_category_name: "Waist Pouches"
                },
                {
                  super_sub_category_id: "MSH1714107",
                  super_sub_category_name: "Shoe Bags"
                }
              ]
            },
            {
              sub_category_id: "MSH1713",
              sub_category_name: "Kid Bags",
              supersubCategories: [
                {
                  super_sub_category_id: "MSH1713100",
                  super_sub_category_name: "School Bags"
                },
                {
                  super_sub_category_id: "MSH1713102",
                  super_sub_category_name: "Diaper Bags "
                }
              ]
            },
            {
              sub_category_id: "MSH1710",
              sub_category_name: "Test L2",
              supersubCategories: [
                {
                  super_sub_category_id: "MSH1710101",
                  super_sub_category_name: "Test Test L3 2 "
                },
                {
                  super_sub_category_id: "MSH17",
                  super_sub_category_name: "CLP Luggage"
                }
              ]
            }
          ],
          super_category_name: "Travel and luggage"
        },
        {
          super_category_name: "Home"
        }
      ]
    };
    return (
      <div className={styles.base}>
        <div className={styles.header} />
        {data.shopbydepartment.map((categories, i) => {
          return (
            <CategoryL1 label={categories.super_category_name}>
              {categories.subCategories &&
                categories.subCategories.map((category, i) => {
                  return (
                    <CategoryL2
                      label={category.sub_category_name}
                      value={category.sub_category_id}
                    >
                      {category.supersubCategories &&
                        category.supersubCategories.map((subCategory, i) => {
                          return (
                            <CategoryL3
                              label={subCategory.super_sub_category_name}
                              value={subCategory.super_sub_category_id}
                            />
                          );
                        })}
                    </CategoryL2>
                  );
                })}
            </CategoryL1>
          );
        })}
        {/* <CategoryL1 label="Gadgets">
          <CategoryL2 label="Mobile Phones">
            <CategoryL3 label="Andriod phones" />
            <CategoryL3 label="iPhones" />
            <CategoryL3 label="Feature Phones" />
            <CategoryL3 label="Best Camera Phones" />
            <CategoryL3 label="Best Camera Phones" />
          </CategoryL2>
          <CategoryL2 label="Mobile Phones">
            <CategoryL3 label="Andriod phones" />
            <CategoryL3 label="iPhones" />
            <CategoryL3 label="Feature Phones" />
            <CategoryL3 label="Best Camera Phones" />
            <CategoryL3 label="Best Camera Phones" />
          </CategoryL2>
          <CategoryL2 label="Mobile Phones">
            <CategoryL3 label="Andriod phones" />
            <CategoryL3 label="iPhones" />
            <CategoryL3 label="Feature Phones" />
            <CategoryL3 label="Best Camera Phones" />
            <CategoryL3 label="Best Camera Phones" />
          </CategoryL2>
        </CategoryL1>
        <CategoryL1 label="Gadgets" />
        <CategoryL1 label="Gadgets" /> */}
      </div>
    );
  }
}
