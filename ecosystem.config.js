module.exports = {
  apps: [
    {
      name: "tata-cliq-frontend",
      script: "index.js",
      watch: true
    }
  ],
  deploy: {
    production: {
      user: "ubuntu",
      host: "54.147.12.99",
      key: "~/.ssh/ORACLE-HYBRIS.pem",
      ref: "origin/master",
      repo: "git@github.com:XelpmocDesignandTechPvtLtd/tata-cliq-frontend.git",
      ssh_options: ["StrictHostKeyChecking=no", "PasswordAuthentication=no"],
      path: "/home/ubuntu/tata-cliq-frontend",
      "post-deploy": "yarn install && yarn run pre-build"
    }
  }
};
