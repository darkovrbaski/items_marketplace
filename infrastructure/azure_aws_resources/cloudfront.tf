resource "aws_cloudfront_distribution" "cf" {
  origin {
    domain_name              = aws_s3_bucket.b.bucket_regional_domain_name
    origin_id                = aws_s3_bucket.b.id
    origin_access_control_id = aws_cloudfront_origin_access_control.cf_oac.id
    origin_path              = "/${var.default_s3_content["images"]}"
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

    trusted_key_groups = [
      aws_cloudfront_key_group.kg.id
    ]
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

resource "aws_cloudfront_public_key" "k" {
  encoded_key = file("public_key.pem")
  name        = "${var.name}-key"
}

resource "aws_cloudfront_key_group" "kg" {
  items = [aws_cloudfront_public_key.k.id]
  name  = "${var.name}-key-group"
}

resource "aws_cloudfront_origin_access_control" "cf_oac" {
  name                              = "${var.bucket_name}.s3.${var.aws_region}.amazonaws.com"
  origin_access_control_origin_type = "s3"
  signing_behavior                  = "always"
  signing_protocol                  = "sigv4"
}

data "aws_cloudfront_cache_policy" "cp" {
  name = "Managed-CachingOptimized"
}
