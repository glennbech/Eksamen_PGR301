resource "aws_ecr_repository" "stwe001" {
  name = "stwe001"

  image_scanning_configuration {
    scan_on_push = false
  }
}

resource "aws_ecr_repository" "stwe001_sensur" {
  name = "stwe001-sensor"

  image_scanning_configuration {
    scan_on_push = false
  }
}