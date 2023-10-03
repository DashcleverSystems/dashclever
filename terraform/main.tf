terraform {
    required_providers {
        heroku = {
            source  = "heroku/heroku"
            version = "~> 5.0"
        }
    }

    backend "pg" {}
}

resource "heroku_app" "app_dev" {
    name   = "${var.app_name}"
    region = "eu"
}

resource "heroku_formation" "dev_formation" {
    app_id   = heroku_app.app_dev.id
    quantity = 1
    size     = "eco"
    type     = "web"
}

resource "heroku_addon" "dev_db" {
    app_id = heroku_app.app_dev.id
    plan   = "heroku-postgresql:mini"
}

resource "heroku_build" "dashclever_backend_build" {
    app_id     = heroku_app.app_dev.id
    buildpacks = [
        "https://github.com/heroku/heroku-buildpack-nodejs.git",
        "https://github.com/heroku/heroku-buildpack-gradle.git",
    ]

    source {
        path = "../tar/app.tar.gz"
    }
}

resource "heroku_config" "conf" {
    vars = {
        SECURITY_LOGGING_LEVEL = var.security_logging_level
        GRADLE_TASK            = "stage --stacktrace"
    }
}

resource "heroku_app_config_association" "dev_conf_assoc" {
    app_id = heroku_app.app_dev.id
    vars   = heroku_config.conf.vars
}

#resource "heroku_app" "app_prd" {
#  name   = "${local.app_name}-prd"
#  region = "eu"
#}

#resource "heroku_formation" "prd_formation" {
#  app_id   = heroku_app.app_prd.id
#  quantity = 1
#  size     = "eco"
#  type     = "web"
#}

#resource "heroku_pipeline" "cd" {
#  name = "${local.app_name}-cd"
#}

#resource "heroku_pipeline_coupling" "dev_stage" {
#  app_id   = heroku_app.app_dev.id
#  pipeline = heroku_pipeline.cd.id
#  stage    = "staging"
#}

#resource "heroku_pipeline_coupling" "prd_stage" {
#  app_id   = heroku_app.app_prd.id
#  pipeline = heroku_pipeline.cd.id
#  stage    = "production"
#}

#resource "heroku_addon" "prd_db" {
#  app_id = heroku_app.app_prd.id
#  plan   = "heroku-postgresql:mini"
#}

