const express = require("express");
const app = express();
app.get("*.js", function(req, res, next) {
  req.url = req.url + ".gz";
  res.set("Content-Encoding", "gzip");
  next();
});

app.use(express.static("build"));

app.get("/*", (req, res) => {
  res.sendFile(__dirname + "/build/index.html");
});
app.listen(3000, () => console.log("Server running on port 3000"));
