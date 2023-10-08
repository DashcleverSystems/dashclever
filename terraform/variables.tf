variable "app_name" {
  type = string
}

variable "security_logging_level" {
    default = "INFO"
    type    = string
}

variable "openapi_enabled" {
    default = true
    type    = bool
}
