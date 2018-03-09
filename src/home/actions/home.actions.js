import {
  SUCCESS,
  REQUESTING,
  ERROR,
  FAILURE,
  GET_FEED_DATA_FOR_BLP,
  GET_FEED_DATA_FOR_CLP
} from "../../lib/constants";
import each from "lodash/each";
import {
  MSD_NUM_RESULTS,
  MAD_UUID,
  MSD_WIDGET_LIST,
  MSD_WIDGET_PLATFORM,
  MSD_API_KEY
} from "../../lib/config.js";
export const HOME_FEED_REQUEST = "HOME_FEED_REQUEST";
export const HOME_FEED_SUCCESS = "HOME_FEED_SUCCESS";
export const HOME_FEED_FAILURE = "HOME_FEED_FAILURE";
export const HOME_FEED_NULL_DATA_SUCCESS = "HOME_FEED_NULL_DATA_SUCCESS";
export const COMPONENT_DATA_REQUEST = "COMPONENT_DATA_REQUEST";
export const COMPONENT_DATA_SUCCESS = "COMPONENT_DATA_SUCCESS";
export const COMPONENT_DATA_FAILURE = "COMPONENT_DATA_FAILURE";
export const SINGLE_SELECT_REQUEST = "SINGLE_SELECT_REQUEST";
export const SINGLE_SELECT_SUCCESS = "SINGLE_SELECT_SUCCESS";
export const SINGLE_SELECT_FAILURE = "SINGLE_SELECT_FAILURE";
export const MULTI_SELECT_SUBMIT_REQUEST = "MULTI_SELECT_SUBMIT_REQUEST";
export const MULTI_SELECT_SUBMIT_SUCCESS = "MULTI_SELECT_SUBMIT_SUCCESS";
export const MULTI_SELECT_SUBMIT_FAILURE = "MULTI_SELECT_SUBMIT_FAILURE";
export const HOME_FEED_PATH = "homepage";
export const SINGLE_SELECT_SUBMIT_PATH = "submitSingleSelectQuestion";
export const MULTI_SELECT_SUBMIT_PATH = "submitMultiSelectQuestion";
export const GET_ITEMS_REQUEST = "GET_SALE_ITEMS_REQUEST";
export const GET_ITEMS_SUCCESS = "GET_SALE_ITEMS_SUCCESS";
export const GET_ITEMS_FAILURE = "GET_SALE_ITEMS_FAILURE";
const ADOBE_TARGET_HOME_FEED_MBOX_NAME = "mboxPOCTest1";
const urlForBLP = "";
const mockDataForBLP = [
  {
    componentName: "landingPageTitleComponent",
    landingPageTitleComponent: {
      type: "Landing Page Title",
      componentId: "LandingPageTitle",
      title: "Adidas"
    }
  },
  {
    componentName: "landingPageHeaderComponent",
    landingPageHeaderComponent: {
      type: "Landing Page Header Component",
      componentId: "LandingPageHeaderComponent",
      items: [
        {
          title: "Life more beautiful with Westside",
          webURL: "https://www.tatacliq.com/womens-clothing/c-msh10",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto:good/v1518509399/westside_banner_pdxy8s.png",
          brandLogo:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/v1518509587/westside_logo_beotcg.png"
        }
      ]
    }
  },

  {
    componentName: "landingPageHierarchyComponent",
    landingPageHierarchyComponent: {
      title: "Shop Adidas",
      type: "Landing Page Hierarchy",
      componentId: "LandingPageHierarchy",
      items: [
        {
          title: "Womens",
          items: [
            {
              title: "Kurtis",
              webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
            },
            {
              title: "Leggings",
              webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
            }
          ]
        },
        {
          title: "Mens",
          items: [
            {
              title: "Shirts",
              webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
            },
            {
              title: "Trousers",
              webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
            }
          ]
        },
        {
          title: "Kids",
          webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
        }
      ]
    }
  },
  {
    componentName: "curatedListingStripComponent",
    curatedListingStripComponent: {
      componentId: "CuratedListingStrip",
      startHexCode: "#fffffff",
      title: "Shop latest collections from Adidas",
      type: "Curated Listing Strip Component",
      webURL: "www.tatacliq.com"
    }
  },
  {
    recentlyViewedComponent: {
      brandLogo: "",
      btnText: "See all",
      componentId: "AutomatedBrandProductCarousel",
      title: "Recently Viewed Product",
      description: "",
      imageURL: "//localhost:9001/medias/sys_master/images/8796152725534.png",
      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "//www.brandattic.com/media/catalog/product/cache/1/thumbnail/400x/040ec09b1e35df139433887a97daa66f/J/M/JMT001_BLUEMULTI_1_12.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "TAG Heuer Carrera",

          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        }
      ],
      type: "Recently viewed product",
      webURL: ""
    },
    componentName: "recentlyViewedComponent"
  },
  {
    componentName: "singleBannerComponent",

    singleBannerComponent: {
      componentId: "MonoBLPBanner",

      items: [
        {
          btnText: "Shop the range",

          hexCode: "#ffffff",

          imageURL:
            "//localhost:9001/medias/sys_master/images/8796152725534.png",

          title: "A seamless swim. Own the water",

          webURL: ""
        }
      ],

      title: "All new Stella McCartney",

      type: "Single Banner Component"
    }
  },
  {
    recentlyViewedComponent: {
      brandLogo: "",
      btnText: "See all",
      componentId: "AutomatedBrandProductCarousel",
      title: "Best Sellers",
      description: "",
      imageURL: "//localhost:9001/medias/sys_master/images/8796152725534.png",
      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "//www.brandattic.com/media/catalog/product/cache/1/thumbnail/400x/040ec09b1e35df139433887a97daa66f/J/M/JMT001_BLUEMULTI_1_12.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "TAG Heuer Carrera",

          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        }
      ],
      type: "Recently viewed product",
      webURL: ""
    },
    componentName: "recentlyViewedComponent"
  }
];
const mockDataForCLP = [
  {
    componentName: "landingPageTitleComponent",
    landingPageTitleComponent: {
      type: "Landing Page Title",
      componentId: "LandingPageTitle",
      title: "Adidas"
    }
  },
  {
    componentName: "heroBannerComponent",
    heroBannerComponent: {
      componentId: "HeroBanner",

      items: [
        {
          brandLogo:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/v1518509587/westside_logo_beotcg.png",

          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto:good/v1518509399/westside_banner_pdxy8s.png",

          title: "HeroBannerEle title",

          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },

        {
          brandLogo:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/v1518509398/clarks_logo_miifki.png",

          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto:good/v1518509399/Banner_02_uomx6k.png",

          title: "HeroBannerEle1 title",

          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        }
      ],

      type: "Hero Banner Component"
    }
  },
  {
    componentName: "landingPageHierarchyComponent",
    landingPageHierarchyComponent: {
      title: "Mobile",
      type: "Landing Page Hierarchy",
      componentId: "LandingPageHierarchy",
      items: [
        {
          title: "Womens",
          items: [
            {
              title: "Kurtis",
              webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
            },
            {
              title: "Leggings",
              webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
            }
          ]
        },
        {
          title: "Mens",
          items: [
            {
              title: "Shirts",
              webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
            },
            {
              title: "Trousers",
              webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
            }
          ]
        },
        {
          title: "Kids",
          webURL: "https://www.tatacliq.com/womens-clothing/c-msh10"
        }
      ]
    }
  },
  {
    componentName: "curatedListingStripComponent",
    curatedListingStripComponent: {
      componentId: "CuratedListingStrip",
      startHexCode: "#fffffff",
      title: "Shop latest collections from Adidas",
      type: "Curated Listing Strip Component",
      webURL: "www.tatacliq.com"
    }
  },
  {
    recentlyViewedComponent: {
      brandLogo: "",
      btnText: "See all",
      componentId: "AutomatedBrandProductCarousel",
      title: "Exclusive Mobile ",
      description: "",
      imageURL: "//localhost:9001/medias/sys_master/images/8796152725534.png",
      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "//www.brandattic.com/media/catalog/product/cache/1/thumbnail/400x/040ec09b1e35df139433887a97daa66f/J/M/JMT001_BLUEMULTI_1_12.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "TAG Heuer Carrera",

          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        }
      ],
      type: "Recently viewed product",
      webURL: ""
    },
    componentName: "recentlyViewedComponent"
  },
  {
    componentName: "curatedProductsComponent",

    curatedProductsComponent: {
      btnText: "See all",

      componentId: "CuratedProductsWidget",

      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "//www.brandattic.com/media/catalog/product/cache/1/thumbnail/400x/040ec09b1e35df139433887a97daa66f/J/M/JMT001_BLUEMULTI_1_12.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Gia",

          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        }
      ],

      title: "Products in spotlight",

      type: "Curated Products Component",

      webURL: "www.tatacliq.com"
    }
  }
];

const mockDataForBrand = [
  {
    componentName: "offersWidgetComponent",
    offersComponent: {
      componentId: "OffersWidget",

      items: [
        {
          btnText: "Shop Now",

          discountText: "Upto 40% Off",

          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_500/v1517296816/Exclusive_Offers_1.1_y20hgz.jpg",

          title: "Beautiful watches at amazing prices",

          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          btnText: "Shop Now",

          discountText: "Upto 25% Off",

          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_500/v1517296809/Exclusive_offers_1.2_tqv5mt.jpg",

          title: "Beautiful watches at amazing prices",

          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        }
      ],

      title: "Exclusive offers",

      type: "Offers Component"
    }
  },

  {
    componentName: "flashSalesComponent",

    flashSalesComponent: {
      backgroundHexCode: "#AA6786",

      backgroundImageURL:
        "//localhost:9001/medias/sys_master/images/8796152725534.png",

      btnText: "Shop All",

      componentId: "FlashSales",

      description: "Grab these offers",

      endDate: "8/03/2018 12:59:59",

      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good:good,w_148/v1516182985/flash_sale_product_2_seyo73.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Alba Black and White T-shirt",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        },
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 800,

            formattedValue: "800"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good:good,w_148/v1516182985/flash_sale_product_2_seyo73.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 1000,

            formattedValue: "1000"
          },

          prdId: "987654342",

          title: "Westside Black and White T-shirt",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        }
      ],

      offers: [
        {
          description: "Adidas men's casual wear",

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good:good,w_148/v1516182985/flash_sale_product_2_seyo73.jpg",

          title: "Under Rs.999",

          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          description: "Westside men's casual wear",

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good:good,w_148/v1516182985/flash_sale_product_2_seyo73.jpg",

          title: "Under Rs.1999",

          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        }
      ],

      startDate: "7/3/2018 0:0:0",

      title: "Exclusive Offers",

      type: "Flash Sales Component",

      webURL: "www.tatacliq.com"
    }
  },

  {
    componentName: "contentComponent",

    contentComponent: {
      componentId: "ContentWidget",

      items: [
        {
          btnText: "Read More",

          description:
            "The new trends thats catching up this summer. This is a dummy text that is acting as a placeholder",

          imageURL: "https://via.placeholder.com/350x150",

          title: "Parisian Chic",

          webURL: "www.tatacliq.com"
        },
        {
          btnText: "Read More",

          description:
            "The new trends thats catching up this summer. This is a dummy text that is acting as a placeholder",

          imageURL: "https://via.placeholder.com/350x150",

          title: "Indian Wear",

          webURL: "www.tatacliq.com"
        },
        {
          btnText: "Read More",

          description:
            "The new trends thats catching up this summer. This is a dummy text that is acting as a placeholder",

          imageURL: "https://via.placeholder.com/350x150",

          title: "Western Chic",

          webURL: "www.tatacliq.com"
        }
      ],

      title: "Travel inspirations",

      type: "Content Component"
    }
  },

  {
    bannerProductCarouselComponent: {
      btnText: "See all",

      componentId: "BannerProductCarousel",

      description: "The Canon DSLR Series",

      imageURL:
        "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto,w_359/v1516181437/Banner_product_Carousal_image_jv2ftp.png",

      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181423/product_3_zm671o.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Canon EOS 7D Mark III with DSLR Camera",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        },
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 800,

            formattedValue: "800"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181423/product_3_zm671o.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 1000,

            formattedValue: "1000"
          },

          prdId: "987654342",

          title: "BannerProd Element Title",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        },
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 800,

            formattedValue: "800"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181423/product_3_zm671o.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 1000,

            formattedValue: "1000"
          },

          prdId: "987654342",

          title: "BannerProd Element Title",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        },
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 800,

            formattedValue: "800"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181423/product_3_zm671o.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 1000,

            formattedValue: "1000"
          },

          prdId: "987654342",

          title: "BannerProd Element Title",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        }
      ],

      title: "BannerProductCarousel title",

      type: "Banner Product Carousel Component",

      webURL: "www.tatacliq.com"
    },

    componentName: "bannerProductCarouselComponent"
  },

  {
    componentName: "videoProductCarouselComponent",

    videoProductCarouselComponent: {
      brandLogo:
        "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto,w_140/v1516185081/TAG_HEUER_logo_z7nrfj.png",

      btnText: "See all",

      componentId: "VideoProductCarousel",

      description: "The Canon DSLR Series",

      imageURL:
        "http://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_400/v1516181429/Video_product_carousal_image_npic8w.jpg",

      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181423/product_3_zm671o.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Canon EOS 200D with DSLR Camera",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        },
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181423/product_3_zm671o.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Canon EOS 200D with DSLR Camera",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        },
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181423/product_3_zm671o.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Canon EOS 200D with DSLR Camera",

          webURL:
            "https://www.tatacliq.com/electronics-mobile-phones/p-mp000000001955259"
        }
      ],

      title: "product video",

      type: "Video Product Carousel Component",

      videoURL: "",

      webURL: "www.tatacliq.com"
    }
  },

  {
    componentName: "themeOffersComponent",

    themeOffersComponent: {
      backgroundHexCode: "#AA6786",

      backgroundImageURL: "",

      btnText: "See All",

      componentId: "ThemeOffers",

      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181425/product_4_zbgnnp.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Alba Black and White T-shirt",

          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        },
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181425/product_4_zbgnnp.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Alba Black and White T-shirt",

          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        }
      ],

      offers: [
        {
          description: "Adidas women's casual wear",

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181425/product_4_zbgnnp.jpg",

          title: "Under Rs.500",

          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          description: "Adidas women's casual wear",

          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good,w_148/v1516181425/product_4_zbgnnp.jpg",

          title: "Under Rs.500",

          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        }
      ],

      title: "Treat this Diwali with 25% off",

      type: "Theme Offers Component",

      webURL: "www.tatacliq.com"
    }
  },

  // {
  //   componentName: "multiClickComponent",

  //   multiClickComponent: {
  //     brandLogo: "",

  //     btnText:
  //       "http://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:best,w_800/v1517228341/Utsa_Background_jtbatt.jpg",

  //     componentId: "ThemeProductWidget",

  //     imageURL: "",

  //     items: [
  //       {
  //         discountedPrice: {
  //           currencyIso: "INR",

  //           currencySymbol: "₹",

  //           doubleValue: 900,

  //           formattedValue: "900"
  //         },

  //         imageURL:
  //           "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good:good,w_148/v1516182985/flash_sale_product_2_seyo73.jpg",

  //         mrpPrice: {
  //           currencyIso: "INR",

  //           currencySymbol: "₹",

  //           doubleValue: 2000,

  //           formattedValue: "2000"
  //         },

  //         prdId: "987654342",

  //         title: "Anarkali Kurta",

  //         webURL:
  //           "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
  //       }
  //     ],

  //     title: "New arrivals",

  //     type: "Multi Click Component"
  //   }
  // },

  {
    bannerSeparatorComponent: {
      componentId: "BannerSeparator",

      description: "Save it for later with our save feature",

      endHexCode: "#AA5470",

      iconImageURL:
        "//localhost:9001/medias/sys_master/images/8796152725534.png",

      startHexCode: "#AA5423",

      title: "Seen something you like?",

      type: "Banner Separator Component",

      webURL: ""
    },

    componentName: "bannerSeparatorComponent"
  },

  {
    automatedBannerProductCarouselComponent: {
      brandLogo: "",

      btnText: "See all",

      componentId: "AutomatedBrandProductCarousel",

      description: "",

      imageURL: "//localhost:9001/medias/sys_master/images/8796152725534.png",

      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "//www.brandattic.com/media/catalog/product/cache/1/thumbnail/400x/040ec09b1e35df139433887a97daa66f/J/M/JMT001_BLUEMULTI_1_12.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "TAG Heuer Carrera",

          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        }
      ],

      type: "Automated Banner Product Carousel Component",

      webURL: ""
    },

    componentName: "automatedBannerProductCarouselComponent"
  },

  {
    componentName: "curatedListingStripComponent",

    curatedListingStripComponent: {
      componentId: "CuratedListingStrip",

      startHexCode: "#fffffff",

      title: "Shop latest collections from Adidas",

      type: "Curated Listing Strip Component",

      webURL: "www.tatacliq.com"
    }
  },

  {
    componentName: "subBrandsBannerComponent",

    subBrandsBannerComponent: {
      componentId: "SubBrandBannerBLP",

      items: [
        {
          brandLogo: "",

          imageURL:
            "//localhost:9001/medias/sys_master/images/8796152725534.png",

          webURL: "www.tatacliq.com"
        }
      ],

      title: "Brands from Westside",

      type: "Sub Brands Banner Component"
    }
  },

  {
    componentName: "curatedProductsComponent",

    curatedProductsComponent: {
      btnText: "See all",

      componentId: "CuratedProductsWidget",

      items: [
        {
          discountedPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 900,

            formattedValue: "900"
          },

          imageURL:
            "//www.brandattic.com/media/catalog/product/cache/1/thumbnail/400x/040ec09b1e35df139433887a97daa66f/J/M/JMT001_BLUEMULTI_1_12.jpg",

          mrpPrice: {
            currencyIso: "INR",

            currencySymbol: "₹",

            doubleValue: 2000,

            formattedValue: "2000"
          },

          prdId: "987654342",

          title: "Gia",

          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        }
      ],

      title: "Products in spotlight",

      type: "Curated Products Component",

      webURL: "www.tatacliq.com"
    }
  },

  {
    componentName: "twoByTwoBannerComponent",

    twoByTwoBannerComponent: {
      componentId: "SmartFilterWidget",

      items: [
        {
          description: "The phones to know about",

          imageURL:
            "//localhost:9001/medias/sys_master/images/8796152725534.png",

          title: "The leaders pack",

          webURL: "www.tatacliq.com"
        }
      ],

      title: "Curated Smartphones",

      type: "Two by Two Banner Component"
    }
  },

  {
    componentName: "msdComponent",

    msdComponent: {
      componentId: "MsdComponent",

      details: false,

      num_results: 10,

      subType: "Auto Fresh From Brands Component",

      type: "MSD Component"
    }
  },

  {
    adobeTargetComponent: {
      componentId: "AdobeTarget",

      mbox: "test1",

      type: "Adobe Target Component"
    },

    componentName: "adobeTargetComponent"
  },

  {
    brandsTabAZListComponent: {
      items: [
        {
          brands: [
            {
              brandName: "Nike",

              webURL: "https://www.tatacliq.com/nike/mbh-123"
            },

            {
              brandName: "Puma",

              webURL: "https://www.tatacliq.com/puma/mbh-123"
            }
          ],

          items: [
            {
              heroBannerComponent: {
                items: [
                  {
                    brandLogo: "",

                    imageURL:
                      "//localhost:9001/medias/sys_master/images/8796152725534.png",

                    title: "HeroBannerEle title",

                    webURL: "www.tatacliq.com"
                  },

                  {
                    brandLogo: "",

                    imageURL:
                      "//localhost:9001/medias/sys_master/images/8796152725534.png",

                    title: "HeroBannerEle1 title",

                    webURL: "TataCliq1.com"
                  }
                ],

                type: "Hero Banner Component"
              }
            }
          ],

          subType: "Men"
        },

        {
          brands: [
            {
              brandName: "Nike",

              webURL: "https://www.tatacliq.com/nike/mbh-123"
            },

            {
              brandName: "Puma",

              webURL: "https://www.tatacliq.com/puma/mbh-123"
            }
          ],

          items: [
            {
              heroBannerComponent: {
                items: [
                  {
                    brandLogo: "",

                    imageURL:
                      "//localhost:9001/medias/sys_master/images/8796152725534.png",

                    title: "HeroBannerEle title",

                    webURL: "www.tatacliq.com"
                  },

                  {
                    brandLogo: "",

                    imageURL:
                      "//localhost:9001/medias/sys_master/images/8796152725534.png",

                    title: "HeroBannerEle1 title",

                    webURL: "TataCliq1.com"
                  }
                ],

                type: "Hero Banner Component"
              }
            }
          ],

          subType: "Women"
        }
      ],

      type: "Brands Tab AZ List Component"
    },

    componentName: "brandsTabAZListComponent"
  },

  {
    componentName: "topCategoriesComponent",
    topCategoriesComponent: {
      componentId: "TopCategoriesWidget",
      items: [
        {
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520424149/topcategories1_pylxl9.png",
          title: "Top and Tees",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520424149/topcategories3_t8uz8c.png",
          title: "Top and Tees",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520424242/topcategories2_l2oi9o.png",
          title: "Top and Tees",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        }
      ],
      title: "Top Categories",
      type: "Top Categories Component"
    }
  },

  {
    componentName: "twoByTwoBannerComponent",
    twoByTwoBannerComponent: {
      componentId: "SmartFilterWidget",
      items: [
        {
          description: "The phones to know about",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520431059/2by2_vd7tqq.png",
          title: "The leaders pack",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          description: "The phones to know about",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520431059/2by2_vd7tqq.png",
          title: "The leaders pack",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          description: "The phones to know about",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520431059/2by2_vd7tqq.png",
          title: "The leaders pack",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          description: "The phones to know about",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520431059/2by2_vd7tqq.png",
          title: "The leaders pack",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        }
      ],
      title: "Curated Smartphones",
      type: "Two by Two Banner Component"
    }
  },

  {
    componentName: "subBrandsBannerComponent",
    subBrandsBannerComponent: {
      componentId: "SubBrandBannerBLP",
      items: [
        {
          brandLogo: "",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520421689/subbrand_via4xp.jpg",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          brandLogo: "",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520421689/subbrand_via4xp.jpg",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          brandLogo: "",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520421689/subbrand_via4xp.jpg",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        },
        {
          brandLogo: "",
          imageURL:
            "http://res.cloudinary.com/dka5wc5e4/image/upload/q_auto/v1520421689/subbrand_via4xp.jpg",
          webURL: "https://www.tatacliq.com/electronics-mobile-phones/c-msh1210"
        }
      ],
      title: "Brands from Westside",
      type: "Sub Brands Banner Component"
    }
  },
  {
    componentName: "curatedProductsComponent",
    curatedProductsComponent: {
      btnText: "See all",
      componentId: "CuratedProductsWidget",
      items: [
        {
          discountedPrice: {
            currencyIso: "INR",
            currencySymbol: "₹",
            doubleValue: 900,
            formattedValue: "900"
          },
          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good:good,w_148/v1516182985/flash_sale_product_2_seyo73.jpg",
          mrpPrice: {
            currencyIso: "INR",
            currencySymbol: "₹",
            doubleValue: 2000,
            formattedValue: "2000"
          },
          prdId: "987654342",
          title: "Gia",
          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        },
        {
          discountedPrice: {
            currencyIso: "INR",
            currencySymbol: "₹",
            doubleValue: 900,
            formattedValue: "900"
          },
          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good:good,w_148/v1516182985/flash_sale_product_2_seyo73.jpg",
          mrpPrice: {
            currencyIso: "INR",
            currencySymbol: "₹",
            doubleValue: 2000,
            formattedValue: "2000"
          },
          prdId: "987654342",
          title: "Gia",
          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        },
        {
          discountedPrice: {
            currencyIso: "INR",
            currencySymbol: "₹",
            doubleValue: 900,
            formattedValue: "900"
          },
          imageURL:
            "https://res.cloudinary.com/dka5wc5e4/image/upload/c_scale,q_auto:good:good,w_148/v1516182985/flash_sale_product_2_seyo73.jpg",
          mrpPrice: {
            currencyIso: "INR",
            currencySymbol: "₹",
            doubleValue: 2000,
            formattedValue: "2000"
          },
          prdId: "987654342",
          title: "Gia",
          webURL:
            "https://localhost:9002/blue-westsport-swimwear-womens/p-987654342"
        }
      ],
      title: "Products in spotlight",
      type: "Curated Products Component",
      webURL: "www.tatacliq.com"
    }
  }
];
export function getItemsRequest(positionInFeed) {
  return {
    type: GET_ITEMS_REQUEST,
    positionInFeed,
    status: REQUESTING
  };
}
export function getItemsSuccess(positionInFeed, items) {
  return {
    type: GET_ITEMS_SUCCESS,
    status: SUCCESS,
    items,
    positionInFeed
  };
}
export function getItemsFailure(positionInFeed, errorMsg) {
  return {
    type: GET_ITEMS_FAILURE,
    errorMsg,
    status: FAILURE
  };
}
export function getItems(positionInFeed, itemIds, isPdp: false) {
  return async (dispatch, getState, { api }) => {
    dispatch(getItemsRequest(positionInFeed));
    try {
      let productCodes;
      each(itemIds, itemId => {
        productCodes = `${itemId},${productCodes}`;
      });
      const url = `v2/mpl/products/productInfo?productCodes=${productCodes}`;
      const result = await api.get(url);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(getItemsSuccess(positionInFeed, resultJson.results));
    } catch (e) {
      dispatch(getItemsFailure(positionInFeed, e.message));
    }
  };
}
export function multiSelectSubmitRequest(positionInFeed) {
  return {
    type: MULTI_SELECT_SUBMIT_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}
export function multiSelectSubmitFailure(positionInFeed, errorMsg) {
  return {
    type: MULTI_SELECT_SUBMIT_FAILURE,
    status: ERROR,
    error: errorMsg
  };
}
export function multiSelectSubmitSuccess(resultJson, positionInFeed) {
  return {
    type: MULTI_SELECT_SUBMIT_SUCCESS,
    status: SUCCESS,
    data: resultJson,
    positionInFeed
  };
}
export function multiSelectSubmit(values, questionId, positionInFeed) {
  return async (dispatch, getState, { api }) => {
    dispatch(multiSelectSubmitRequest(positionInFeed));
    try {
      const result = await api.postMock(SINGLE_SELECT_SUBMIT_PATH, {
        optionId: values,
        questionId
      });
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(multiSelectSubmitSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(multiSelectSubmitFailure(e.message, positionInFeed));
    }
  };
}
export function singleSelectRequest(positionInFeed) {
  return {
    type: SINGLE_SELECT_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}
export function singleSelectFailure(error, positionInFeed) {
  return {
    type: SINGLE_SELECT_FAILURE,
    status: ERROR,
    error,
    positionInFeed
  };
}
export function singleSelectSuccess(resultJson, positionInFeed) {
  return {
    type: SINGLE_SELECT_SUCCESS,
    status: SUCCESS,
    data: resultJson,
    positionInFeed
  };
}
export function selectSingleSelectResponse(value, questionId, positionInFeed) {
  return async (dispatch, getState, { api }) => {
    dispatch(singleSelectRequest(positionInFeed));
    try {
      const result = await api.postMock(SINGLE_SELECT_SUBMIT_PATH, {
        questionId,
        optionId: [value]
      });
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(singleSelectSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(singleSelectFailure(e.message, positionInFeed));
    }
  };
}
export function homeFeedRequest() {
  return {
    type: HOME_FEED_REQUEST,
    status: REQUESTING
  };
}
export function homeFeedSuccess(data, feedType) {
  return {
    type: HOME_FEED_SUCCESS,
    status: SUCCESS,
    data,
    feedType
  };
}
export function homeFeedFailure(error) {
  return {
    type: HOME_FEED_FAILURE,
    status: ERROR,
    error
  };
}
export function homeFeed(feedType: null) {
  return async (dispatch, getState, { api }) => {
    dispatch(homeFeedRequest());
    try {
      let url, result, feedTypeRequest;
      if (feedType) {
        result = await api.get(`v2/mpl/cms/defaultpage?pageId=${feedType}`);
        feedTypeRequest = "blp";
      } else {
        url = ADOBE_TARGET_HOME_FEED_MBOX_NAME;
        result = await api.postAdobeTargetUrl(null, url, null, null, true);
        feedTypeRequest = "home";
      }

      //TODO this needs to be cleaned up.
      const resultJson = await result.json();
      if (resultJson.errors) {
        dispatch(homeFeedSuccess([], feedTypeRequest));
      }
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson}`);
      }
      let parsedResultJson = JSON.parse(resultJson.content);
      parsedResultJson = parsedResultJson.items;
      dispatch(homeFeedSuccess(parsedResultJson, feedTypeRequest));
    } catch (e) {
      dispatch(homeFeedFailure(e.message));
    }
  };
}
export function componentDataRequest(positionInFeed) {
  return {
    type: COMPONENT_DATA_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}
export function componentDataSuccess(data, positionInFeed, isMsd: false) {
  return {
    type: COMPONENT_DATA_SUCCESS,
    status: SUCCESS,
    data,
    positionInFeed,
    isMsd
  };
}
export function componentDataFailure(positionInFeed, error) {
  return {
    type: COMPONENT_DATA_FAILURE,
    status: ERROR,
    positionInFeed,
    error
  };
}
export function getComponentData(positionInFeed, fetchURL, postParams: null) {
  return async (dispatch, getState, { api }) => {
    dispatch(componentDataRequest(positionInFeed));
    try {
      let postData;
      let result;
      if (postParams && postParams.widgetPlatform === MSD_WIDGET_PLATFORM) {
        postData = {
          ...postParams,
          api_key: MSD_API_KEY,
          num_results: MSD_NUM_RESULTS,
          mad_uuid: MAD_UUID,
          widget_list: MSD_WIDGET_LIST //TODO this is going to change.
        };
        result = await api.post(fetchURL, postData, true);
        let resultJson = await result.json();
        if (resultJson.status === "FAILURE") {
          throw new Error(`${resultJson.message}`);
        }
        dispatch(componentDataSuccess(resultJson, positionInFeed, true));
      } else {
        result = await api.postAdobeTargetUrl(
          fetchURL,
          postParams && postParams.mbox ? postParams.mbox : null,
          null,
          null,
          false
        );
        const resultJson = await result.json();
        if (resultJson.status === "FAILURE") {
          throw new Error(`${resultJson.message}`);
        }
        let parsedResultJson = JSON.parse(resultJson.content);
        parsedResultJson = parsedResultJson.items[0];
        dispatch(componentDataSuccess(parsedResultJson, positionInFeed));
      }
    } catch (e) {
      dispatch(componentDataFailure(positionInFeed, e.message));
    }
  };
}
