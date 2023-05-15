output "cloudfront_public_domain_name" {
  value = aws_cloudfront_distribution.cf_public.domain_name
  description = "The domain name of the CloudFront distribution"
}

output "cloudfront_private_domain_name" {
  value = aws_cloudfront_distribution.cf.domain_name
  description = "The domain name of the CloudFront distribution"
}

output "cloudfront_private_key_pair_id" {
  value = aws_cloudfront_public_key.k.id
  description = "The id of the CloudFront public key"
}

output "s3_bucket_name" {
  value = aws_s3_bucket.b.id
  description = "The name of the S3 bucket"
}

output "private_key_name" {
  value = aws_ssm_parameter.s.name
  description = "The name of the SSM parameter"
}

output "frontend_url" {
  value = "https://${azurerm_static_site.ss.default_host_name}"
  description = "The url of the frontend"
}

output "frontend_cdn_url" {
  value = "https://${azurerm_cdn_endpoint.cdn-ep.fqdn}"
  description = "The url of the CDN"
}

output "container_group_url" {
  value = azurerm_container_group.acg.ip_address
  description = "The url of the container group"
}

output "container_group_port" {
  value = var.app_port
  description = "The port of the container group"
}
