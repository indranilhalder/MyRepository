import React from "react";
import Filter from "./Filter";
import ProductGrid from "./ProductGrid";
import styles from "./Plp.css";
export default class Plp extends React.Component {
  render() {
    const searchresult = [
      {
        type: "product",
        brandname: "AND",
        compareProductType: "MSH1016102",
        cumulativeStockLevel: true,
        discountPercent: "0",
        galleryImagesList: [
          {
            galleryImages: [
              {
                key: "product",
                value:
                  "https://img.tatacliq.com/images/i2/437Wx649H/MP000000002000654_437Wx649H_20171108184732.jpeg"
              }
            ],
            mediaType: "Image"
          }
        ],
        imageURL:
          "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
        isOfferExisting: false,
        newProduct: true,
        onlineExclusive: false,
        isCliqAndPiQ: true,
        isSponsored: false,
        addedInWishList: true,
        price: {
          isRange: true,
          mrpPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          sellingPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          minPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          maxPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          }
        },
        bestDeliveryInfo: "Tuesday, Sept 12",
        offerText: "25% offers from Rs. 35,000",
        averageRating: 4,
        totalNoOfReviews: 25,
        productId: "MP000000002000759",
        productname: "AND Black Pin Striped Top",
        ussid: "1000048907641086467"
      },
      {
        type: "product",
        brandname: "AND",
        compareProductType: "MSH1016102",
        cumulativeStockLevel: true,
        discountPercent: "0",
        galleryImagesList: [
          {
            galleryImages: [
              {
                key: "product",
                value:
                  "https://img.tatacliq.com/images/i2/437Wx649H/MP000000002000654_437Wx649H_20171108184732.jpeg"
              }
            ],
            mediaType: "Image"
          }
        ],
        imageURL:
          "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
        isOfferExisting: false,
        newProduct: true,
        onlineExclusive: false,
        isCliqAndPiQ: true,
        isSponsored: false,
        addedInWishList: true,
        price: {
          isRange: true,
          mrpPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          sellingPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          minPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          maxPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          }
        },
        bestDeliveryInfo: "Tuesday, Sept 12",
        offerText: "25% offers from Rs. 35,000",
        averageRating: 4,
        totalNoOfReviews: 25,
        productId: "MP000000002000759",
        productname: "AND Black Pin Striped Top",
        ussid: "1000048907641086467"
      },
      {
        type: "product",
        brandname: "AND",
        compareProductType: "MSH1016102",
        cumulativeStockLevel: true,
        discountPercent: "0",
        galleryImagesList: [
          {
            galleryImages: [
              {
                key: "product",
                value:
                  "https://img.tatacliq.com/images/i2/437Wx649H/MP000000002000654_437Wx649H_20171108184732.jpeg"
              }
            ],
            mediaType: "Image"
          }
        ],
        imageURL:
          "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
        isOfferExisting: false,
        newProduct: true,
        onlineExclusive: false,
        isCliqAndPiQ: true,
        isSponsored: true,
        addedInWishList: false,
        price: {
          isRange: true,
          mrpPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          sellingPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          minPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          maxPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          }
        },
        bestDeliveryInfo: "Tuesday, Sept 12",
        offerText: "25% offers from Rs. 35,000",
        averageRating: 4,
        totalNoOfReviews: 25,
        productId: "MP000000002000759",
        productname: "AND Black Pin Striped Top",
        ussid: "1000048907641086467"
      },
      {
        type: "plpAd",
        imageURL:
          "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
        trackingValue: "PLPAd",
        webURL:
          "https://www.tatacliq.com/electronics-large-appliances-washing-machine/c-msh1214103?q=%3Arelevance%3Acategory%3AMSH1214103%3Afunction-classification%3ASemi%20Automatic&isFacet=true&facetValue=Semi%20Automatic"
      },
      {
        type: "product",
        brandname: "AND",
        compareProductType: "MSH1016102",
        cumulativeStockLevel: true,
        discountPercent: "0",
        galleryImagesList: [
          {
            galleryImages: [
              {
                key: "product",
                value:
                  "https://img.tatacliq.com/images/i2/437Wx649H/MP000000002000654_437Wx649H_20171108184732.jpeg"
              }
            ],
            mediaType: "Image"
          }
        ],
        imageURL:
          "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
        isOfferExisting: false,
        newProduct: true,
        onlineExclusive: false,
        isCliqAndPiQ: true,
        isSponsored: false,
        addedInWishList: true,
        price: {
          isRange: true,
          mrpPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          sellingPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          minPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          maxPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          }
        },
        bestDeliveryInfo: "Tuesday, Sept 12",
        offerText: "25% offers from Rs. 35,000",
        averageRating: 4,
        totalNoOfReviews: 25,
        productId: "MP000000002000759",
        productname: "AND Black Pin Striped Top",
        ussid: "1000048907641086467"
      },
      {
        type: "iconicFilter",
        filterTitle: "What size of tops do you wear?",
        filterValue: [
          {
            name: "XS",
            searchURL: "top:releavance:size:xs",
            isSelected: false
          },
          {
            name: "S",
            searchURL: "top:releavance:size:s",
            isSelected: false
          },
          {
            name: "M",
            searchURL: "top:releavance:size:m",
            isSelected: false
          },
          {
            name: "L",
            searchURL: "top:releavance:size:l",
            isSelected: false
          },
          {
            name: "XL",
            searchURL: "top:releavance:size:xl",
            isSelected: false
          },
          {
            name: "XX",
            searchURL: "top:releavance:size:xl",
            isSelected: false
          }
        ]
      },
      {
        type: "product",
        brandname: "AND",
        compareProductType: "MSH1016102",
        cumulativeStockLevel: true,
        discountPercent: "0",
        galleryImagesList: [
          {
            galleryImages: [
              {
                key: "product",
                value:
                  "https://img.tatacliq.com/images/i2/437Wx649H/MP000000002000654_437Wx649H_20171108184732.jpeg"
              }
            ],
            mediaType: "Image"
          }
        ],
        imageURL:
          "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
        isOfferExisting: false,
        newProduct: true,
        onlineExclusive: false,
        isCliqAndPiQ: true,
        isSponsored: false,
        addedInWishList: true,
        price: {
          isRange: true,
          mrpPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          sellingPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          minPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          maxPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          }
        },
        bestDeliveryInfo: "Tuesday, Sept 12",
        offerText: "25% offers from Rs. 35,000",
        averageRating: 4,
        totalNoOfReviews: 25,
        productId: "MP000000002000759",
        productname: "AND Black Pin Striped Top",
        ussid: "1000048907641086467"
      },
      {
        type: "pictorialFilter",
        filterTitle: "Filter by collor type",
        filterValue: [
          {
            name: "Polos",
            imageURL:
              "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
            searchURL: "top:releavance:collorType:polos",
            isSelected: true
          },
          {
            name: "V Neck",
            imageURL:
              "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
            searchURL: "top:releavance:collorType:v neck",
            isSelected: true
          },
          {
            name: "Round Neck",
            imageURL:
              "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
            searchURL: "top:releavance:collorType:round neck",
            isSelected: true
          }
        ]
      },
      {
        type: "product",
        brandname: "AND",
        compareProductType: "MSH1016102",
        cumulativeStockLevel: true,
        discountPercent: "0",
        galleryImagesList: [
          {
            galleryImages: [
              {
                key: "product",
                value:
                  "https://img.tatacliq.com/images/i2/437Wx649H/MP000000002000654_437Wx649H_20171108184732.jpeg"
              }
            ],
            mediaType: "Image"
          }
        ],
        imageURL:
          "https://img.tatacliq.com/images/i2/252Wx374H/MP000000002000654_252Wx374H_20171108184732.jpeg",
        isOfferExisting: false,
        newProduct: true,
        onlineExclusive: false,
        isCliqAndPiQ: true,
        isSponsored: false,
        addedInWishList: true,
        price: {
          isRange: true,
          mrpPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          sellingPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          minPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          },
          maxPrice: {
            currencyIso: "INR",
            doubleValue: 1899.01,
            formattedValue: "1899.01",
            currencySymbol: "₹"
          }
        },
        bestDeliveryInfo: "Tuesday, Sept 12",
        offerText: "25% offers from Rs. 35,000",
        averageRating: 4,
        totalNoOfReviews: 25,
        productId: "MP000000002000759",
        productname: "AND Black Pin Striped Top",
        ussid: "1000048907641086467"
      }
    ];
    const data = [
      {
        search: ["three", "two"],
        productImage:
          "https://i.pinimg.com/564x/f6/d2/0d/f6d20d8e57e6a5d58639c0eb898e259c.jpg",
        title: "Three Two",
        description: "Lorem Ipsum Sit amet normalweise nicht so lang"
      },

      {
        search: ["three", "twooo"],
        productImage:
          "https://i.pinimg.com/564x/f6/d2/0d/f6d20d8e57e6a5d58639c0eb898e259c.jpg",
        title: "Three Twooo",
        description: "Lorem Ipsum Sit amet normalweise nicht so lang"
      },

      {
        search: ["three"],
        productImage:
          "https://i.pinimg.com/564x/f6/d2/0d/f6d20d8e57e6a5d58639c0eb898e259c.jpg",
        title: "Three",
        description: "Lorem Ipsum Sit amet normalweise nicht so lang"
      },

      {
        search: ["one"],
        productImage:
          "https://i.pinimg.com/564x/f6/d2/0d/f6d20d8e57e6a5d58639c0eb898e259c.jpg",
        title: "One",
        description: "Lorem Ipsum Sit amet normalweise nicht so lang"
      }
    ];
    return (
      <div className={styles.base}>
        <div className={styles.main}>
          <ProductGrid data={searchresult} />
        </div>
        <div className={styles.filter}>
          <Filter />
        </div>
      </div>
    );
  }
}
