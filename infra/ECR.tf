resource "aws_ecr_repository" "stwe001" {
  name = "stwe001"

  image_scanning_configuration {
    scan_on_push = false
  }
}