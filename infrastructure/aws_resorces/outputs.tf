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
