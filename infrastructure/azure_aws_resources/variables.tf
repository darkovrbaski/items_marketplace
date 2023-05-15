variable "name" {
  type        = string
  description = "the name of your stack"
  default     = "items-marketplace"
}

variable "name-az" {
  type        = string
  description = "the name of your stack"
  default     = "ItemsMarketplace"
}

variable "bucket_name" {
  type        = string
  description = "the name of your bucket"
  default     = "items-marketplace-images"
}

variable "aws_region" {
  type        = string
  default     = "eu-west-3"
  description = "The AWS region to deploy to"
}

variable "default_s3_content" {
  description = "The default content of the s3 bucket upon creation of the bucket"
  type        = map(string)
  default = {
    images        = "images"
    public_images = "public_images"
  }
}

variable "azure_location" {
  type        = string
  default     = "West Europe"
  description = "The Azure location to deploy to"
}

variable "app_port" {
  type        = number
  default     = 8443
  description = "The port the container will listen on"
}

variable "http_port" {
  type        = number
  default     = 8080
  description = "The port the container will listen on"
}

variable "aws_access_key_id_az" {
  type        = string
  description = "The AWS access key id"
  sensitive   = true
  validation {
    condition     = can(regex("^[A-Z0-9]{20}$", var.aws_access_key_id_az))
    error_message = "The AWS access key id must be 20 characters long and contain only uppercase letters and numbers"
  }
}

variable "aws_secret_access_key_az" {
  type        = string
  description = "The AWS secret access key"
  sensitive   = true
  validation {
    condition     = can(regex("^[A-Za-z0-9/+=]{40}$", var.aws_secret_access_key_az))
    error_message = "The AWS secret access key must be 40 characters long and contain only letters, numbers, and the following characters: /+= "
  }
}

variable "postgres_db_url" {
  type        = string
  description = "The postgres database url"
  sensitive   = true
  validation {
    condition     = can(regex("^jdbc:postgresql://[a-zA-Z0-9.]+(:[0-9]+)?/[a-zA-Z0-9]+$", var.postgres_db_url))
    error_message = "The postgres database url must be in the following format: jdbc:postgresql://host:port/database"
  }
}

variable "postgres_db_username" {
  type        = string
  description = "The postgres database username"
  sensitive   = true
}

variable "postgres_db_password" {
  type        = string
  description = "The postgres database password"
  sensitive   = true
}

variable "jwt_secret" {
  type        = string
  default     = "2D4B614E645267556B58703273357638792F423F4528482B4D6251655368566D"
  description = "The JWT secret"
  sensitive   = true
}

variable "stripe_secret_key" {
  type        = string
  description = "The stripe secret key"
  sensitive   = true
}

variable "stripe_endpoint_secret" {
  type        = string
  description = "The stripe endpoint secret"
  sensitive   = true
}

variable "image_name" {
  type        = string
  description = "The name of the image"
  default     = "items-marketplace-image:latest"
}
  
