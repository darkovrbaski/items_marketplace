variable "name" {
  type        = string
  description = "the name of your stack"
  default     = "items-marketplace"
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


variable "stripe_secret_key" {
  type = string
  description = "The stripe secret key"
  sensitive = true
}
  
variable "stripe_endpoint_secret" {
  type = string
  description = "The stripe endpoint secret"
  sensitive = true
}
  