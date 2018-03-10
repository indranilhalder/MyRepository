const facetdatacategory = {
  category: true,
  filters: [
    {
      categoryCode: "MSH12",
      categoryName: "Electronics",
      categoryType: "department",
      childFilters: [
        {
          categoryCode: "MSH1210",
          categoryName: "Mobile Phones",
          categoryType: "category",
          childFilters: [
            {
              categoryCode: "MSH1210100",
              categoryName: "Smartphones",
              categoryType: "category",
              level: "L3",
              quantity: 10,
              ranking: "0",
              selected: false
            },
            {
              categoryCode: "MSH1210101",
              categoryName: "Smartphone accessories",
              categoryType: "category",
              level: "L3",
              quantity: 5,
              ranking: "0",
              selected: false
            },
            {
              categoryCode: "MSH1210102",
              categoryName: "iPhone",
              categoryType: "category",
              level: "L3",
              quantity: 14,
              ranking: "0",
              selected: false
            },
            {
              categoryCode: "MSH1210103",
              categoryName: "Feature Phones",
              categoryType: "category",
              level: "L3",
              quantity: 2,
              ranking: "0",
              selected: false
            }
          ],
          level: "L2",
          quantity: 31,
          ranking: "0",
          selected: true
        }
      ],
      level: "L1",
      quantity: 584,
      ranking: "0",
      selected: false
    }
  ],
  key: "category",
  multiSelect: true,
  name: "Category",
  priority: 17100,
  selected: true,
  visible: true
};
