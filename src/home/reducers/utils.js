// here we are changing our return items array  in same sequence of itemsIds
export function transformFetchingItemsOrder(itemsIds, itemsObj) {
  console.log("TRANSFORM FETCHING");
  console.log(itemsIds);
  let orderedItems = [];

  itemsIds.forEach(itemId => {
    let findObj = itemsObj.find(item => {
      return item.productListingId === itemId;
    });
    orderedItems.push(findObj);
  });

  orderedItems = orderedItems.filter(Boolean);
  console.log("ORDERED ITEMS");
  console.log(orderedItems);
  return orderedItems;
}
