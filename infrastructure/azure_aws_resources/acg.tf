resource "azurerm_container_group" "acg" {
  name                = "${var.name-az}Acg"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  ip_address_type     = "Public"
  os_type             = "Linux"
  dns_name_label      = var.name-az

  image_registry_credential {
    server   = azurerm_container_registry.acr.login_server
    username = azurerm_container_registry.acr.admin_username
    password = azurerm_container_registry.acr.admin_password
  }

  container {
    name   = "container"
    image  = "${azurerm_container_registry.acr.login_server}/${var.image_name}"
    cpu    = "0.25"
    memory = "0.5"

    ports {
      port     = var.app_port
      protocol = "TCP"
    }
    ports {
      port     = var.http_port
      protocol = "TCP"
    }

    secure_environment_variables = {
      "AWS_ACCESS_KEY_ID"      = var.aws_access_key_id_az
      "AWS_SECRET_ACCESS_KEY"  = var.aws_secret_access_key_az
      "POSTGRES_DB_URL"        = var.postgres_db_url
      "POSTGRES_DB_USERNAME"   = var.postgres_db_username
      "POSTGRES_DB_PASSWORD"   = var.postgres_db_password
      "JWT_SECRET"             = var.jwt_secret
      "STRIPE_SECRET_KEY"      = var.stripe_secret_key
      "STRIPE_ENDPOINT_SECRET" = var.stripe_endpoint_secret
      "SSL_PASSWORD"           = var.ssl_password
    }

    environment_variables = {
      "AWS_REGION"                = var.aws_region
      "APP_PORT"                  = var.app_port
      "HTTP_PORT"                 = var.http_port
      "FRONTEND_URL"              = "https://${azurerm_static_site.ss.default_host_name}"
      "FRONTEND_CDN_URL"          = "https://im.darkovrbaski.me/"
      "S3_BUCKET_NAME"            = aws_s3_bucket.b.id
      "CLOUDFRONT_DOMAIN_PUBLIC"  = aws_cloudfront_distribution.cf_public.domain_name
      "CLOUDFRONT_DOMAIN_PRIVATE" = aws_cloudfront_distribution.cf.domain_name
      "KEY_PAIR_ID"               = aws_cloudfront_public_key.k.id
      "PRIVATE_KEY_NAME"          = aws_ssm_parameter.s.name
    }
  }

  depends_on = [null_resource.push_image_to_acr]
}
