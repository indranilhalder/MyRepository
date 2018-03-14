import React from "react";
import styles from "./CategoryTotal.css";
import ShopByBrands from "./ShopByBrands.js";
import LatestCollections from "./LatestCollections.js";
import ProductModule from "../../general/components/ProductModule.js";
import Carousel from "../../general/components/Carousel";
import StoresLocationBanner from "./StoresLocationBanner.js";
import ShopCollection from "../../pdp/components/ShopCollection.js";
import MobileFooterBase from "../../general/components/MobileFooter.js";
import CuratedFeature from "./CuratedFeature.js";
import SelectColourWithCarousel from "./SelectColourWithCarousel.js";
import ThemeProductWidget from "../../home/components/ThemeProductWidget";
export default class CategoryTotal extends React.Component {
  selectBrandList(val) {
    console.log(val);
  }
  seeAllExclusiveBrand() {
    console.log("mt");
  }
  CuratedFeatureSelect() {
    console.log("mt");
  }
  seeAllAccessoriesBrand() {
    console.log("mt");
  }
  arrowNextClick() {
    console.log("lbmk");
  }
  seeAllProduct() {
    console.log("mt");
  }
  CuratedSmartPhone() {
    console.log("mt");
  }
  render() {
    const newBrandName = "All new iPhone 7 from Apple";
    const newBrand = "Shop iPhone 7";
    const feedComponentData = {
      backgroundImageURL:
        "https://www.mobitabspecs.com/wp-content/uploads/2016/07/Xiaomi-Mi-5S-Mobile-full-Specifications.jpg",
      brandLogo:
        "http://u01.appmifile.com/images/2017/07/12/41649e29-dfd0-469d-b685-4f05a4d6c0bd.png",
      title: "New arrivals",
      items: [
        {
          title: "Redmi Note 4",
          mrpPrice: "1,295",
          image:
            "https://n2.sdlcdn.com/imgs/f/e/7/Xiaomi-Redmi-Note-4-Plain-SDL895264682-1-c3607.jpg"
        },
        {
          title: "Redmi Note 4",
          mrpPrice: "1,295",
          image:
            "https://n2.sdlcdn.com/imgs/f/e/7/Xiaomi-Redmi-Note-4-Plain-SDL895264682-1-c3607.jpg"
        },
        {
          title: "Redmi Note 4",
          mrpPrice: "1,295",
          image:
            "https://n2.sdlcdn.com/imgs/f/e/7/Xiaomi-Redmi-Note-4-Plain-SDL895264682-1-c3607.jpg"
        },
        {
          title: "Redmi Note 4",
          mrpPrice: "1,295",
          image:
            "https://n2.sdlcdn.com/imgs/f/e/7/Xiaomi-Redmi-Note-4-Plain-SDL895264682-1-c3607.jpg"
        },
        {
          title: "Redmi Note 4",
          mrpPrice: "1,295",
          image:
            "https://n2.sdlcdn.com/imgs/f/e/7/Xiaomi-Redmi-Note-4-Plain-SDL895264682-1-c3607.jpg"
        }
      ]
    };
    const data = [
      {
        image:
          "https://s7d2.scene7.com/is/image/SamsungUS/S8Plus_Black_Front_032317?$product-details-jpg$",
        title: "Samsung",
        description: "Galaxy S8 Plus",
        price: "1,295"
      },
      {
        image: "https://static.toiimg.com/photo/54060033/.jpg",
        title: "Samsung",
        description: "Galaxy S8 Plus",
        price: "1,295"
      },
      {
        image:
          "https://s7d2.scene7.com/is/image/SamsungUS/S8Plus_Black_Front_032317?$product-details-jpg$",
        title: "Samsung",
        description: "Galaxy S8 Plus",
        price: "1,295"
      },
      {
        image:
          "https://cdn57.androidauthority.net/wp-content/uploads/2017/05/Samsung-galaxy-a3-2017-2.jpg",
        title: "Samsung",
        description: "Galaxy S8 Plus",
        price: "1,295"
      }
    ];
    const data1 = [
      {
        image:
          "https://cdn.media.zagg.com/media/catalog/product/cache/5/image/395x/9df78eab33525d08d6e5fb8d27136e95/s/8/s8-elite_1.png",
        title: "Xqisit ",
        description: "Flap Cover Adour Samsung Galaxy S8",
        price: "1,295"
      },
      {
        image:
          "https://cdn.media.zagg.com/media/catalog/product/cache/5/image/395x/9df78eab33525d08d6e5fb8d27136e95/s/8/s8-elite_1.png",
        title: "Xqisit ",
        description: "Flap Cover Adour Samsung Galaxy S8",
        price: "1,295"
      },
      {
        image:
          "https://cdn.media.zagg.com/media/catalog/product/cache/5/image/395x/9df78eab33525d08d6e5fb8d27136e95/s/8/s8-elite_1.png",
        title: "Xqisit ",
        description: "Flap Cover Adour Samsung Galaxy S8",
        price: "1,295"
      },
      {
        image:
          "https://cdn.media.zagg.com/media/catalog/product/cache/5/image/395x/9df78eab33525d08d6e5fb8d27136e95/s/8/s8-elite_1.png",
        title: "Xqisit ",
        description: "Flap Cover Adour Samsung Galaxy S8",
        price: "1,295"
      }
    ];
    const data2 = [
      {
        image:
          "https://cdn.media.zagg.com/media/catalog/product/cache/5/image/395x/9df78eab33525d08d6e5fb8d27136e95/s/8/s8-elite_1.png",
        title: "Samsung ",
        description: "Galaxy Tab A 10.1-Inch Wi-Fi + Cellular",
        price: "1,295"
      },
      {
        image:
          "https://cdn.media.zagg.com/media/catalog/product/cache/5/image/395x/9df78eab33525d08d6e5fb8d27136e95/s/8/s8-elite_1.png",
        title: "Xqisit ",
        description: "Flap Cover Adour Samsung Galaxy S8",
        price: "1,295"
      },
      {
        image:
          "https://cdn.media.zagg.com/media/catalog/product/cache/5/image/395x/9df78eab33525d08d6e5fb8d27136e95/s/8/s8-elite_1.png",
        title: "Xqisit ",
        description: "Flap Cover Adour Samsung Galaxy S8",
        price: "1,295"
      },
      {
        image:
          "https://cdn.media.zagg.com/media/catalog/product/cache/5/image/395x/9df78eab33525d08d6e5fb8d27136e95/s/8/s8-elite_1.png",
        title: "Xqisit ",
        description: "Flap Cover Adour Samsung Galaxy S8",
        price: "1,295"
      }
    ];
    return (
      <div className={styles.base}>
        <div className={styles.brandGallery}>
          <StoresLocationBanner
            brandLocation={[
              {
                headingText: "Sony",
                descriptionText: "The all new Sony Xperia XZ1",
                image:
                  "http://www.newdaypictures.com/images/Column%20Images/CAMERA%20HIRE%20IMAGES/Sony-HXR-NX3D1E_3D.gif"
              },
              {
                headingText: "Sony",
                descriptionText: "The all new Sony Xperia XZ1",
                image:
                  "http://www.newdaypictures.com/images/Column%20Images/CAMERA%20HIRE%20IMAGES/Sony-HXR-NX3D1E_3D.gif"
              },
              {
                headingText: "Sony",
                descriptionText: "The all new Sony Xperia XZ1",
                image:
                  "http://www.newdaypictures.com/images/Column%20Images/CAMERA%20HIRE%20IMAGES/Sony-HXR-NX3D1E_3D.gif"
              },
              {
                headingText: "Sony",
                descriptionText: "The all new Sony Xperia XZ1",
                image:
                  "http://www.newdaypictures.com/images/Column%20Images/CAMERA%20HIRE%20IMAGES/Sony-HXR-NX3D1E_3D.gif"
              },
              {
                headingText: "Sony",
                descriptionText: "The all new Sony Xperia XZ1",
                image:
                  "http://www.newdaypictures.com/images/Column%20Images/CAMERA%20HIRE%20IMAGES/Sony-HXR-NX3D1E_3D.gif"
              }
            ]}
          />
        </div>
        <div className={styles.brandListHolder}>
          <ShopByBrands
            onClick={val => this.selectBrandList(val)}
            label="View More"
            brandHeader="Shop by brand"
            brandLists={[
              { list: "Acer" },
              { list: "Apple" },
              { list: "Asus" },
              { list: "BlackBerry" },
              { list: "Coolpad" },
              { list: "Croma" },
              { list: "Gionee" },
              { list: "Google" },
              { list: "Acer" },
              { list: "Acer" },
              { list: "Acer" },
              { list: "Acer" },
              { list: "Acer" }
            ]}
          />
        </div>
        <LatestCollections
          arrowNextClick={() => this.arrowNextClick()}
          heading="Shop all latest collection"
        />
        <div className={styles.productDetailsHolder}>
          <Carousel
            header="Recently viewed adidas®"
            withFooter={true}
            buttonText="See all"
            seeAll={() => this.seeAllExclusiveBrand()}
          >
            {data &&
              data.map((datum, i) => {
                return (
                  <ProductModule
                    key={i}
                    isWhite={false}
                    productImage={datum.image}
                    title={datum.title}
                    price={datum.price}
                    description={datum.description}
                    onDownload={datum.onDownload}
                  />
                );
              })}
          </Carousel>
        </div>
        <CuratedFeature
          header="Curated Smartphones"
          onClick={() => this.CuratedFeatureSelect()}
          curatedFeature={[
            {
              header: "The leaders pack",
              text: "The phones to know about.",
              imageUrl:
                "https://www.dailydot.com/wp-content/uploads/822/0a/ae13abee93e129cabd295af9133f55d3.jpg"
            },
            {
              header: "Globetrotters",
              text: "Perfect mobile companions for trips of a lifetime.",
              imageUrl:
                "https://www.windowscentral.com/sites/wpcentral.com/files/styles/large_wm_blb/public/field/image/2015/11/Lumia-950-windows-10-mobile-hero.jpg?itok=4Vj6c6wx"
            },
            {
              header: "Underdog performers on Android",
              text: "These shouldn’t be so good for the money.",
              imageUrl:
                "https://www.windowscentral.com/sites/wpcentral.com/files/styles/large_wm_blb/public/field/image/2015/11/Lumia-950-windows-10-mobile-hero.jpg?itok=4Vj6c6wx"
            },
            {
              header: "Timely buys",
              text: "New releases make these ‘just old’ models great value.",
              imageUrl:
                "https://www.dailydot.com/wp-content/uploads/822/0a/ae13abee93e129cabd295af9133f55d3.jpg"
            }
          ]}
        />
        <div className={styles.productDetailsHolder}>
          <Carousel
            header="Accessories for Samsung S8"
            withFooter={true}
            buttonText="See all"
            seeAll={() => this.seeAllAccessoriesBrand()}
          >
            {data1 &&
              data1.map((datum, i) => {
                return (
                  <ProductModule
                    key={i}
                    isWhite={false}
                    productImage={datum.image}
                    title={datum.title}
                    price={datum.price}
                    description={datum.description}
                    onDownload={datum.onDownload}
                  />
                );
              })}
          </Carousel>
        </div>
        <div className={styles.colourSelectHolder}>
          <SelectColourWithCarousel
            heading="What is your favourite mobile phone colour ?"
            buttonText=""
            multipleColour={[
              {
                colour: "#c0c0c0"
              },
              {
                colour: "#000000",
                selected: true
              },
              {
                colour: "#dfba69"
              },
              {
                colour: "#f0ccbc"
              },
              {
                colour: "#cf2926"
              },
              {
                colour: "#c0c0c0"
              },
              {
                colour: "#000000"
              },
              {
                colour: "#dfba69"
              },
              {
                colour: "#f0ccbc"
              },
              {
                colour: "#cf2926"
              }
            ]}
          />
        </div>
        <div className={styles.newBrandHolder}>
          <div className={styles.newBrandHedaer}>{newBrandName}</div>
          <div className={styles.newBrandImageHolder}>
            <ShopCollection
              title="Now connected by a love for music"
              btnText={`shop ${newBrand}`}
              onClick={() => this.getNewBrands()}
              backgroundColor={"#ffffff"}
              color="#000000"
              logo="https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Apple_Store_logo.svg/1024px-Apple_Store_logo.svg.png"
              image="https://www.naijatechguide.com/wp-content/uploads/2016/09/iphone-7-plus-featured.jpg"
            />
          </div>
        </div>
        <div className={styles.productDetailsHolder}>
          <Carousel
            header="Related Samsung products"
            withFooter={true}
            buttonText="See all"
            seeAll={() => this.seeAllProduct()}
          >
            {data2 &&
              data2.map((datum, i) => {
                return (
                  <ProductModule
                    key={i}
                    isWhite={false}
                    productImage={datum.image}
                    title={datum.title}
                    price={datum.price}
                    description={datum.description}
                    onDownload={datum.onDownload}
                  />
                );
              })}
          </Carousel>
        </div>
        <CuratedFeature
          header="Curated Smartphones"
          onClick={() => this.CuratedSmartPhone()}
          curatedFeature={[
            {
              header: "Phone features",
              text: "The phones to know about.",
              imageUrl:
                "https://www.dailydot.com/wp-content/uploads/822/0a/ae13abee93e129cabd295af9133f55d3.jpg"
            },
            {
              header: "Globetrotters",
              text: "Perfect mobile companions for trips of a lifetime.",
              imageUrl:
                "https://www.windowscentral.com/sites/wpcentral.com/files/styles/large_wm_blb/public/field/image/2015/11/Lumia-950-windows-10-mobile-hero.jpg?itok=4Vj6c6wx"
            },
            {
              header: "Underdog performers on Android",
              text: "These shouldn’t be so good for the money.",
              imageUrl:
                "https://www.windowscentral.com/sites/wpcentral.com/files/styles/large_wm_blb/public/field/image/2015/11/Lumia-950-windows-10-mobile-hero.jpg?itok=4Vj6c6wx"
            },
            {
              header: "Timely buys",
              text: "New releases make these ‘just old’ models great value.",
              imageUrl:
                "https://www.dailydot.com/wp-content/uploads/822/0a/ae13abee93e129cabd295af9133f55d3.jpg"
            }
          ]}
        />
        <div className={styles.productDetailsHolder}>
          <ThemeProductWidget feedComponentData={feedComponentData} />
        </div>
        <div className={styles.footer}>
          <MobileFooterBase />
        </div>
      </div>
    );
  }
}
