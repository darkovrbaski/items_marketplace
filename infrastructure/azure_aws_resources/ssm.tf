resource "aws_ssm_parameter" "s" {
  name  = "${var.name}-cloudfront-private-key"
  type  = "SecureString"
  tier  = "Standard"
  value = file("private_key.pem")
}
