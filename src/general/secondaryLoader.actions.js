export const SHOW_LOADER = "SHOW_LOADER";
export const HIDE_LOADER = "HIDE_LOADER";
export function showSecondaryLoader(type, ownProps) {
  return {
    type: SHOW_LOADER,
    secondaryLoaderDisplay: true
  };
}

export function hideModal() {
  return {
    type: HIDE_LOADER,
    secondaryLoaderDisplay: false
  };
}
