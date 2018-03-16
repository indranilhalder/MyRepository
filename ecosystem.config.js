module.exports = {
  apps: [
    {
      name: "tata-cliq-frontend",
      script: "index.js",
      watch: true,
      env: {
        NODE_ENV: "production",
        REACT_APP_GOOGLE_CLIENT_ID:
          "367761167032-apbr4v0nndom1cafs9inrrnkk7iag5be.apps.googleusercontent.com",
        REACT_APP_FACEBOOK_CLIENT_ID: "1444012285724567",
        REACT_APP_RECAPTCHA_SITE_KEY:
          "6LfpAk0UAAAAACmNvkmNNTiHlgAcu0DxKXC9oESm",
        REACT_APP_RECAPTCHA_SECRET_KEY:
          "6LfpAk0UAAAAAJkyk-73xj7GEtIBw3KMD6vpXqJW",
        REACT_APP_MSD_API_KEY: "a7e46b8a87c52ab85d352e9"
      }
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
