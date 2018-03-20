//TODO why are some cookies session and why are some timestamped?
// What is the difference?
export function createCookie(name, value, days) {
  let expires;
  if (days) {
    let date = new Date();
    date.setSeconds(date.getSeconds() + days);
    expires = `; expires=${date}`;
  } else {
    expires = "";
  }
  document.cookie = `${name}=${value + expires}`;
}

export function getCookie(cookieName) {
  let match = document.cookie.match(new RegExp(cookieName + `=([^;]+)`));
  if (match) return match[1];
}

export function deleteCookie(cookieName) {
  document.cookie =
    cookieName + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
}
