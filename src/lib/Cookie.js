export function createCookie(name, value, days) {
  let expires;
  if (days) {
    let date = new Date();
    date.setSeconds(date.getSeconds() + days);
    expires = `; expires=${date}`;
  } else {
    expires = "";
  }
  document.cookie = name + "=" + value + expires;
}

export function getCookie(c_name) {
  let match = document.cookie.match(new RegExp(c_name + `=([^;]+)`));
  if (match) return match[1];
}
