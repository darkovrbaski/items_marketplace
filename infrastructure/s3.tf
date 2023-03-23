resource "aws_s3_bucket" "b" {
  bucket        = var.bucket_name
  force_destroy = true
}

resource "aws_s3_object" "default_s3_content" {
  for_each = var.default_s3_content
  bucket   = aws_s3_bucket.b.id
  key      = "${each.value}/"
}

resource "aws_s3_bucket_public_access_block" "bp" {
  bucket = aws_s3_bucket.b.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_bucket_policy" "allow_access_cloudfront" {
  bucket = aws_s3_bucket.b.id
  policy = data.aws_iam_policy_document.allow_access_cloudfront.json
}

data "aws_iam_policy_document" "allow_access_cloudfront" {
  statement {
    sid    = "AllowCloudFrontServicePrincipal"
    effect = "Allow"

    principals {
      type        = "Service"
      identifiers = ["cloudfront.amazonaws.com"]
    }

    actions = [
      "s3:GetObject",
    ]

    resources = [
      "${aws_s3_bucket.b.arn}/images/*",
    ]

    condition {
      test     = "StringEquals"
      variable = "aws:SourceArn"

      values = [
        aws_cloudfront_distribution.cf.arn,
      ]
    }
  }

  statement {
    sid    = "AllowCloudFrontServicePrincipal_PublicImages"
    effect = "Allow"

    principals {
      type        = "Service"
      identifiers = ["cloudfront.amazonaws.com"]
    }

    actions = [
      "s3:GetObject",
    ]

    resources = [
      "${aws_s3_bucket.b.arn}/public_images/*",
    ]

    condition {
      test     = "StringEquals"
      variable = "aws:SourceArn"

      values = [
        aws_cloudfront_distribution.cf_public.arn,
      ]
    }
  }
}
