import React from "react";
import styles from "./SearchPage.css";
import BrandHeader from "../../brands/components/BrandHeader";
import SearchResult from "./SearchResult";

export default class SearchPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showResults: false
    };
  }
  handleSearchClick(val) {
    console.log(val);
    this.setState({ showResults: val });
  }
  render() {
    const data = {
      type: "mplAutoCompleteResultWsData",
      status: "Success",
      popularProducts: [
        {
          imageURL:
            "//pcmuat.tataunistore.com/images/252Wx374H/MP000000000015978_252Wx374H_20160219202527.jpeg",
          isOfferExisting: false,
          name: "BCK Boys Casual Ankle Boots Tan",
          productTag: {},
          url: "/bck-boys-casual-ankle-boots-tan/p-mp000000000015978"
        },
        {
          imageURL:
            "//pcmuat.tataunistore.com/images/252Wx374H/MP000000000015977_252Wx374H_20160219202608.jpeg",
          isOfferExisting: false,
          name: "BCK Boys Casual Ankle Boots Brown",
          productTag: {},
          url: "/bck-boys-casual-ankle-boots-brown/p-mp000000000015977"
        }
      ],
      suggestionText: ["casual"],
      topBrands: [
        {
          categoryCode: "MBH13F00009",
          categoryName: "Adidas"
        },
        {
          categoryCode: "MBH13F00017",
          categoryName: "BCK"
        },
        {
          categoryCode: "MBH13F00086",
          categoryName: "Woodland"
        }
      ],
      topCategories: [
        {
          categoryCode: "MSH1310",
          categoryName: "Women"
        },
        {
          categoryCode: "MSH1313",
          categoryName: "Boys"
        },
        {
          categoryCode: "MSH1311",
          categoryName: "Men"
        }
      ]
    };
    return (
      <div className={styles.base}>
        <div className={styles.searchBar}>
          <BrandHeader
            onSearchClick={val => {
              this.handleSearchClick(val);
            }}
          />
        </div>
        {this.state.showResults && (
          <div className={styles.searchResults}>
            {data.popularProducts.map((val, i) => {
              return <SearchResult text={val.name} value={val.url} />;
            })}
            {data.topBrands.map((val, i) => {
              return (
                <SearchResult
                  text={val.categoryName}
                  value={val.categoryCode}
                />
              );
            })}
            {data.topCategories.map((val, i) => {
              return (
                <SearchResult
                  text={val.categoryName}
                  value={val.categoryCode}
                />
              );
            })}
          </div>
        )}
      </div>
    );
  }
}
