import React from "react";
import FilterWithMultiSelect from "./FilterWithMultiSelect";
import FilterSelect from "./FilterSelect";
import FilterCategories from "./FilterCategories";
import styles from "./Filter.css";

export default class Filter extends React.Component {
  render() {
    const filterData = [
      {
        key: "brand",
        multiSelect: true,
        isGlobalFilter: true,
        name: "Brand",
        selectedFilterCount: 2,
        values: [
          {
            count: 10,
            name: "109 F",
            selected: true,
            value: "MBH11A00014"
          },
          {
            count: 1,
            name: "AMZER",
            selected: true,
            value: "MBH12E00443"
          },
          {
            count: 19,
            name: "AND",
            selected: false,
            value: "MBH11A00015"
          }
        ]
      },
      {
        key: "price",
        multiSelect: true,
        isGlobalFilter: true,
        name: "Price",
        customeRange: true,
        rangeApplied: true,
        minPrice: "500",
        maxPrice: "9999999",
        values: [
          {
            count: 360,
            name: "₹0-₹500",
            selected: false,
            value: "₹0-₹500"
          },
          {
            count: 4340,
            name: "₹501-₹1,000",
            selected: false,
            value: "₹501-₹1,000"
          },
          {
            count: 4762,
            name: "₹1,001-₹1,500",
            selected: false,
            value: "₹1,001-₹1,500"
          },
          {
            count: 3227,
            name: "₹1,501-₹2,000",
            selected: false,
            value: "₹1,501-₹2,000"
          }
        ]
      },
      {
        key: "colour",
        multiSelect: true,
        name: "Colour",
        selectedFilterCount: 2,
        values: [
          {
            count: 16,
            name: "Beige",
            selected: true,
            value: "Beige_F5F5DC",
            hexColor: "#F5F5DC"
          },
          {
            count: 32,
            name: "Black",
            selected: true,
            value: "Black_000000",
            hexColor: "#FF878B"
          }
        ]
      },
      {
        key: "Occasion",
        multiSelect: true,
        name: "Occasion",
        selectedFilterCount: 2,
        values: [
          {
            count: 149,
            name: "Casual Wear",
            selected: true,
            value: "casualwear"
          },
          {
            count: 29,
            name: "College Look",
            selected: true,
            value: "collegelook"
          },
          {
            count: 95,
            name: "Work Wear",
            selected: false,
            value: "workwear"
          }
        ]
      }
    ];

    return (
      <div className={styles.base}>
        <div className={styles.tabs}>
          <FilterCategories data={filterData} />
        </div>
        <div className={styles.options}>
          {filterData.map(datum => {
            return (
              <FilterWithMultiSelect>
                {datum.values.map(value => {
                  return (
                    <FilterSelect
                      label={value.name}
                      value={value.value}
                      count={value.count}
                    />
                  );
                })}
              </FilterWithMultiSelect>
            );
          })}
        </div>
      </div>
    );
  }
}
