import React from "react";
import BrandsCategory from "./BrandsCategory";
import BrandsSubCategory from "./BrandsSubCategory";
import MobileFooter from "../../general/components/MobileFooter";
import styles from "./BrandsLandingPage.css";
export default class BrandsLandingPage extends React.Component {
  render() {
    const catagory = [
      {
        index: "1",
        catagory: "109 F",
        subCatagory: [{ list: "11cent" }]
      },
      {
        index: "9",
        catagory: "9rasa",
        subCatagory: [{ list: "9teenAGAIN", select: true }]
      },
      {
        index: "A",
        catagory: "AND",
        subCatagory: [
          { list: "Amante" },
          { list: "Avengers", select: true },
          { list: "Arkham Asylum" }
        ]
      }
    ];

    return (
      <div className={styles.base}>
        {catagory.map((val, i) => {
          return (
            <BrandsCategory index={val.index} catagory={val.catagory}>
              {val.subCatagory &&
                val.subCatagory.map((data, i) => {
                  return (
                    <BrandsSubCategory label={data.list} select={data.select} />
                  );
                })}
            </BrandsCategory>
          );
        })}
        <MobileFooter selected="brands" />
      </div>
    );
  }
}
