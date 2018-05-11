const express = require("express");
const app = express();
app.get("*.js", function(req, res, next) {
  if (req.url === "/service-worker.js") {
  } else {
    req.url = req.url + ".gz";
    res.set("Content-Encoding", "gzip");
  }

  res.set("Content-Type", "application/javascript");
  next();
});

app.get("*.css", function(req, res, next) {
  req.url = req.url + ".gz";
  res.set("Content-Encoding", "gzip");
  res.set("Content-Type", "text/css");
  next();
});

app.use(express.static("build"));

app.get("/*", (req, res) => {
  res.sendFile(__dirname + "/build/index.html");
});
app.listen(3000, () => console.log("Server running on port 3000"));
