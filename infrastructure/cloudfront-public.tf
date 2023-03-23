resource "aws_cloudfront_distribution" "cf_public" {
  origin {
    domain_name              = aws_s3_bucket.b.bucket_regional_domain_name
    origin_id                = aws_s3_bucket.b.id
    origin_access_control_id = aws_cloudfront_origin_access_control.cf_oac.id
    origin_path              = "/${var.default_s3_content["public_images"]}"
  }

  enabled         = true
  is_ipv6_enabled = true
  price_class     = "PriceClass_All"

  default_cache_behavior {
    allowed_methods  = ["HEAD", "GET"]
    cached_methods   = ["HEAD", "GET"]
    target_origin_id = aws_s3_bucket.b.id

    viewer_protocol_policy = "redirect-to-https"
    compress               = true
    cache_policy_id        = data.aws_cloudfront_cache_policy.cp.id
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }
}
