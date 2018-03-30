// here we are changing our return items array  in same sequence of itemsIds
export function transformFetchingItemsOrder(itemsIds, itemsObj) {
  let orderedItems = [];
  itemsIds.forEach(itemId => {
    let findObj = itemsObj.find(item => {
      return item.productListingId === itemId;
    });
    orderedItems.push(findObj);
  });
  return orderedItems;
}
